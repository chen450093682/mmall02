package com.dxs.seckill.vo;

public class Singletion1 {

    private static final  Singletion1 singletion1=new Singletion1();

    private  Singletion1(){}


    public static  Singletion1 getSingletion1(){
        return singletion1;
    }


}
