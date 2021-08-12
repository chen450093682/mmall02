package com.dxs.seckill.controller;


import com.dxs.seckill.pojo.User;
import com.dxs.seckill.rabbitmq.MQSender;
import com.dxs.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author aaa
 * @since 2021-05-15
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private MQSender mqSender;

    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user){
        return RespBean.success(user);
    }

    // /**
    //  * 测试MQ发送消息
    //  */
    // @RequestMapping("/mq")
    // @ResponseBody
    // public void mq(){
    //     mqSender.send("hello");
    // }
    //
    // /**
    //  * Fanout模式
    //  */
    // @RequestMapping("/mq/fanout")
    // @ResponseBody
    // public void mq01(){
    //     mqSender.send("hello");
    // }
    //
    // /**
    //  * Direct模式
    //  */
    // @RequestMapping("/mq/direct01")
    // @ResponseBody
    // public void mq02(){
    //     mqSender.send01("hello red");
    // }
    //
    // @RequestMapping("/mq/direct02")
    // @ResponseBody
    // public void mq03(){
    //     mqSender.send02("hello green");
    // }
    //
    // /**
    //  * Topic模式
    //  */
    // @RequestMapping("/mq/topic01")
    // @ResponseBody
    // public void mq04(){
    //     mqSender.send03("hello red");
    // }
    //
    // @RequestMapping("/mq/topic02")
    // @ResponseBody
    // public void mq05(){
    //     mqSender.send04("hello green");
    // }
}
