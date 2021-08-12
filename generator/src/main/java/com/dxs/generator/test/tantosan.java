package com.dxs.generator.test;

import java.util.Stack;

public class tantosan {

    public String add(String a,String b){
        int carry=0;
        int sum=0;
        int opa;
        int opb;

        StringBuilder result=new StringBuilder();

        while(a.length()!=b.length()){
            if(a.length()>b.length()){
                b=b+"0";
            }else{
                a=a+"0";
            }
        }
        for(int i=a.length()-1;i>=0;i--){
            opa=a.charAt(i)-'0';
            opb=b.charAt(i)-'0';
            sum=opa+opb+carry;
            if(sum>=3){
                result.append((char)(sum-3+'0'));
                carry=1;
            }
            else{
                result.append((char)(sum+'0'));
                carry=0;
            }
        }
            if(carry==1){
                result.append('1');
            }
        return result.reverse().toString();

    }

    public String dec(String a,String b){
        int carry=0;
        int sum=0;
        int opa;
        int opb;

        StringBuilder result=new StringBuilder();

        while(a.length()!=b.length()){
            if(a.length()>b.length()){
                b=b+"0";
            }else{
                a=a+"0";
            }
        }
        for(int i=a.length()-1;i>=0;i--){
            opa=a.charAt(i)-'0';
            opb=b.charAt(i)-'0';
            sum=opa+opb+carry;
            if(sum>=3){
                result.append((char)(sum-3+'0'));
                carry=1;
            }
            else{
                result.append((char)(sum+'0'));
                carry=0;
            }
        }
        if(carry==1){
            result.append('1');
        }
        return result.reverse().toString();

    }

    public String tantosansuan(int num){
        Stack<Integer> stack=new Stack<>();
        int tmp=0;
        while(num>0){
            tmp=num%3;
            stack.push(tmp);
            num /=3;
        }
        String re ="";
        while(!stack.isEmpty()){
            re+=stack.pop();
        }
        return re;
    }


    public static void main(String[] args) {

        tantosan  y=new tantosan();
        tantosan  x=new tantosan();
        System.out.println(y.tantosansuan(120));
        String a=y.tantosansuan(120);
        System.out.println(x.tantosansuan(120));
        String b=y.tantosansuan(123);
        System.out.print(y.add(a,b));
        System.out.println();
        System.out.println(~-10);


    }

}
