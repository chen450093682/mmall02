package com.dxs.seckill.vo;

public class Runnable  implements java.lang.Runnable {
    @Override
    public void run() {
        System.out.println("qq");
        System.out.println(Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        for (int i = 0; i <20 ; i++) {
            Runnable runnable=new Runnable();

            new Thread(runnable,"线程").start();
            new Thread(runnable,"线程"+i).start();
        }
    }
}
