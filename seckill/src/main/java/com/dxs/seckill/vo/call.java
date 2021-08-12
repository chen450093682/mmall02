package com.dxs.seckill.vo;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class call implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        for (int i = 0; i < 20; i++) {
            System.out.println(Thread.currentThread().getName());
        }

        return 1;
    }

    public static void main(String[] args) {
        call call = new call();
        FutureTask<Integer> integerFuture = new FutureTask<>(call);

        for (int i = 0; i <20 ; i++) {
            new Thread(integerFuture,"aaa").start();
        }


    }


}
