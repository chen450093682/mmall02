package com.dxs.seckill.vo;

import org.apache.commons.lang3.builder.ToStringExclude;

public class singletion {

    private  static  volatile singletion singletion;

     private  singletion(){

    }


    public static  synchronized singletion getSingletion(){


         if(singletion==null){
             synchronized (singletion.class) {
                 if(singletion==null) {
                     singletion = new singletion();
                 }
             }
         }
         return singletion;
    }
}
