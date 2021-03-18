package com.hfy.demo01.algorithm.recursion;

/**
 * 递归思想：阶层：n!:n*(n-1)*....*2*1
 * @author hufeiyang
 * @data 2021/2/28
 * @Description:
 */
public class CalNFact {
    public static void main(String args[]){

        int number = 5;
        int result = CalNFact.cal(number);
        System.out.println(number+"的阶乘："+ result);
    }


    public static int cal(int num) {

        if (num==1){
            return 1;
        }
        return num*cal(num-1);
    }
}
