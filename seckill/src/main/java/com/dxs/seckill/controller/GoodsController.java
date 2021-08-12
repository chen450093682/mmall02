package com.dxs.seckill.controller;

import com.dxs.seckill.config.WebConfig;
import com.dxs.seckill.pojo.User;
import com.dxs.seckill.service.IGoodsService;
import com.dxs.seckill.service.IUserService;
import com.dxs.seckill.vo.DetailVo;
import com.dxs.seckill.vo.GoodsVo;
import com.dxs.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @program: SecondKillMall
 * @description: 商品
 * @author: aaa
 * @create: 2021-05-15 19:47
 **/
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    //渲染引擎
    @Autowired
    private ThymeleafViewResolver resolver;

    /**
     * 跳转到商品列表页
     * @param model
     * @return
     */
    @RequestMapping(value = "/toList" ,produces = "text/html;charset=utf-8")
    @ResponseBody
    //增加解析参数User的resolver，直接在里面判断是否合法
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        // if (StringUtils.isEmpty(ticket)) {
        //     return "login";
        // }
        // // User user = (User) session.getAttribute(ticket);
        // User user = userService.getUserByCookie(ticket, request, response);
        // if (user==null) {
        //     return "login";
        // }
        // System.out.println(user);
        // if (user==null){
        //     return "login";
        // }

        //通过redis缓存界面，从而达到优化
        //redis获取html界面
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html=(String)valueOperations.get("goodsList");
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        //如果为空 ，手动渲染，存入redis，并返回
        model.addAttribute("user",user);
        model.addAttribute("goodsList", goodsService.findGoodVo());

        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = resolver.getTemplateEngine().process("goodsList", webContext);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsList",html, 60,TimeUnit.SECONDS);
        }
        return html;
    }

    /**
     * 详情获取
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value =  "/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model,User user,@PathVariable("goodsId") Long goodsId, HttpServletRequest request, HttpServletResponse response){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String)valueOperations.get("goodsDetail:" + goodsId);
        if (!StringUtils.isEmpty(html)) {
            return html;
        }

        model.addAttribute("user",user);

        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();

        //秒杀状态
        int seckillStatus=0;
        //秒杀倒计时
        int remainSeconds=0;
        if (nowDate.before(startDate)){
            //未开始
            remainSeconds=(int)((startDate.getTime()-nowDate.getTime())/1000);
        }else if (nowDate.after(endDate)){
            //已经结束
            seckillStatus=2;
            remainSeconds=-1;
        }else {
            //进行中
            seckillStatus=1;
            remainSeconds=0;
        }
        model.addAttribute("remainSeconds",remainSeconds);
        model.addAttribute("goods",goodsVo);
        model.addAttribute("secKillStatus",seckillStatus);

        WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
        html = resolver.getTemplateEngine().process("goodsDetail", webContext);
        if (!StringUtils.isEmpty(html)) {
            valueOperations.set("goodsDetail:"+goodsId,html, 60,TimeUnit.SECONDS);
        }
        return html;
    }

    /**
     * 详情获取
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value =  "/toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(User user, @PathVariable("goodsId") Long goodsId){
        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();

        //秒杀状态
        int seckillStatus=0;
        //秒杀倒计时
        int remainSeconds=0;
        if (nowDate.before(startDate)){
            //未开始
            remainSeconds=(int)((startDate.getTime()-nowDate.getTime())/1000);
        }else if (nowDate.after(endDate)){
            //已经结束
            seckillStatus=2;
            remainSeconds=-1;
        }else {
            //进行中
            seckillStatus=1;
            remainSeconds=0;
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSeckillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }
}
