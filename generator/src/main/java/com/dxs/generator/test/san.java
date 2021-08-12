package com.dxs.generator.test;


public class san {
    private int TtS(int inNum, int index) throws Exception {

        if(inNum < index || index <= 1){
            throw new Exception("不支持的运算");
        }
        String outNum = "";

        int maxIndex = getMaxIndex(inNum, index, 0, 0);
        int[] result = new int[maxIndex];

        for (int i = 0; i < maxIndex; i++) {
            result[i] = inNum % index;
            inNum = inNum / index;
        }

        for (int i = 0; i < result.length; i++) {
            outNum += result[i];
        }
        return Integer.parseInt(outNum);
    }

    /**
     * 计算最高位数
     * inNum:输入的数字
     * index：进制数
     * countNum：累积次数
     * total：累积和
     */
    private int getMaxIndex(int inNum, int index, int countNum, int total){

        int countTotal = total;
        int count = countNum;

        int pow_ = (int)Math.pow(index, ++countTotal);
        while(inNum - pow_ >= 0){
            inNum-=pow_;
            count+=pow_;
            if(inNum - count < 0){
                break;
            }
            return getMaxIndex(inNum, index, count, countTotal);
        }
        return countTotal+1;
    }


    public static void main(String[] args) throws Exception {

        san ts = new san();
        System.out.println(ts.TtS(100,3));

        //输出结果：10201
        //小结：有返回值的递归函数注意return自身
    }
}
