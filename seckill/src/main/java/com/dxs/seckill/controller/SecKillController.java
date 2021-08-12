package com.dxs.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dxs.seckill.config.AccessLimit;
import com.dxs.seckill.execption.GlobalException;
import com.dxs.seckill.pojo.*;
import com.dxs.seckill.rabbitmq.MQSender;
import com.dxs.seckill.service.IGoodsService;
import com.dxs.seckill.service.IOrderService;
import com.dxs.seckill.service.ISeckillOrderService;
import com.dxs.seckill.utils.JsonUtil;
import com.dxs.seckill.vo.GoodsVo;
import com.dxs.seckill.vo.RespBean;
import com.dxs.seckill.vo.RespBeanEnum;
import com.wf.captcha.ArithmeticCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.rmi.runtime.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @program: SecondKillMall
 * @description: 秒杀订单
 * @author: aaa
 * @create: 2021-05-16 20:48
 **/
@Slf4j
@Controller
@RequestMapping("/seckill")
public class SecKillController implements InitializingBean {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private ISeckillOrderService seckillOrderService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    //@Autowired
    //private RedisScript<Long> redisScript;

    private Map<Long, Boolean> emptyStockMap=new HashMap<>();

    /**
     * 秒杀
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/doSeckill2")
    public String doSecKill2(Model model, User user,Long goodsId){
        if (user==null) {
            return "login";
        }
        model.addAttribute("user",user);

        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        //判断库存
        if (goodsVo.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK);
            return "secKillFail";
        }
        //判断重复抢购
        // SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().
        //         eq("user_id", user.getId()).eq("goods_id", goodsId));
        SeckillOrder seckillOrder = (SeckillOrder)redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);

        if (seckillOrder!=null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATED_ERROR);
            return "secKillFail";
        }

        Order order=orderService.seckill(user,goodsVo);
        model.addAttribute("order",order);
        model.addAttribute("goods",goodsVo);
        return "orderDetail";
    }


    /**
     * 秒杀
     * QPS：711.8
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/{path}/doSeckill",method = RequestMethod.POST)
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path,User user, Long goodsId){
        if (user==null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();

        boolean check=orderService.checkPath(user,goodsId,path);
        if (!check){
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }

        //判断重复抢购
        SeckillOrder seckillOrder = (SeckillOrder)valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder!=null) {
            System.out.println("11111111");
            return RespBean.error(RespBeanEnum.REPEATED_ERROR);
        }

        //通过内存标记，减少redis访问
        if (emptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //预减库存
        Long stock = valueOperations.decrement("seckillGoods:" + goodsId);
        //Long stock = (Long) redisTemplate.execute(redisScript, Collections.singletonList("seckillGoods" + goodsId));
        if (stock<0){
            emptyStockMap.put(goodsId,true);
            valueOperations.increment("seckillGoods:" + goodsId);

        }

        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);

        /*GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        //判断库存
        if (goodsVo.getStockCount()<1){
            model.addAttribute("errmsg", RespBeanEnum.EMPTY_STOCK);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().
                eq("user_id", user.getId()).eq("goods_id", goodsId));

        if (seckillOrder!=null) {
            model.addAttribute("errmsg", RespBeanEnum.REPEATED_ERROR);
            return RespBean.error(RespBeanEnum.REPEATED_ERROR);
        }

        Order order=orderService.seckill(user,goodsVo);
        return RespBean.success(order);*/
    }

    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return orderId:成功   -1:失败  0:排队中
     */
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId){
        if (user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }

        Long orderId=seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }

    /**
     * 根据user和goodsId生成唯一请求路径
     * @param user
     * @param goodsId
     * @return
     */
    @AccessLimit(second=5,maxCount=5,needLogin=true)
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getPath(User user, Long goodsId, String captcha, HttpServletRequest request){
        if (user==null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        // //限制访问次数
        // ValueOperations valueOperations = redisTemplate.opsForValue();
        // String uri = request.getRequestURI();
        // Integer count = (Integer) valueOperations.get(uri + ":" + user.getId());
        // if (count==null) {
        //     valueOperations.set(uri+":"+user.getId(), 1,5,TimeUnit.SECONDS);
        // }else if(count<5){
        //     valueOperations.increment(uri+":"+user.getId());
        // }else {
        //     return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REAHCED);
        // }

        boolean check=orderService.checkCaptcha(user,goodsId,captcha);
        if (!check){
            return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        }
        String str=orderService.createPath(user,goodsId);
        return RespBean.success(str);
    }

    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void verifyCode(User user, Long goodsId, HttpServletResponse response){
        if (user==null||goodsId<0) {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        //设置图片类型
        response.setContentType("image/jpg");
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        //生成验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 32, 3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId, captcha.text(),5, TimeUnit.MINUTES);
        try {
            captcha.out(response.getOutputStream());
        } catch (IOException e) {
            log.error("验证码生成失败",e.getMessage());
        }
    }

    /**
     * 系统初始化，把商品库存加载到Redis
     *  @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        list.forEach(goodsVo -> {
            emptyStockMap.put(goodsVo.getId(),false);
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(), goodsVo.getStockCount());
        });
    }
}
