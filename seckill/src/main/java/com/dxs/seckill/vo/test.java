package com.dxs.seckill.vo;

import java.util.concurrent.atomic.AtomicLong;

public class test extends Thread {
     private  AtomicLong  aLong;
    @Override
    public void run() {
        for (int i = 0; i <20 ; i++) {
            System.out.print(i+"");
        }
    }


    public static void main(String[] args)

            
    {
        for (int i = 0; i <20 ; i++) {
            new test().start();
            new test().start();
        }

    }
}
