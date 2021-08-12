package com.dxs.seckill.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dxs.seckill.pojo.SeckillMessage;
import com.dxs.seckill.pojo.SeckillOrder;
import com.dxs.seckill.pojo.User;
import com.dxs.seckill.service.IGoodsService;
import com.dxs.seckill.service.IOrderService;
import com.dxs.seckill.utils.JsonUtil;
import com.dxs.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @program: SecondKillMall
 * @description: 消息消费者
 * @author: aaa
 * @create: 2021-05-31 14:41
 **/
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;

    /**
     * 下单操作
     */
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message){
        log.info("接收到消息："+message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        if (goodsVo.getStockCount()<1){
            return;
        }

        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断重复抢购
        SeckillOrder seckillOrder = (SeckillOrder)valueOperations.get("order:" + user.getId() + ":" + goodsId);
        if (seckillOrder!=null) {
            return;
        }

        //下单操作
        orderService.seckill(user,goodsVo);
    }
}
