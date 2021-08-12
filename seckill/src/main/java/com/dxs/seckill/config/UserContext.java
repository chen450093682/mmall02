package com.dxs.seckill.config;

import com.dxs.seckill.pojo.User;

/**
 * @program: SecondKillMall
 * @description:
 * @author: aaa
 * @create: 2021-06-02 19:41
 **/
public class UserContext {
    private static ThreadLocal<User> userHolder=new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }
}
