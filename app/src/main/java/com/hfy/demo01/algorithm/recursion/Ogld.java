package com.hfy.demo01.algorithm.recursion;

/**
 * 递归算法：使用 欧几里得定律求 两数的最大公约数
 * 欧几里得定律:若a>b，那么a和b的最大公约数= b 和 a%b 的最大公约数
 * @author hufeiyang
 * @data 2021/2/28
 * @Description:
 */
public class Ogld {

    public static void main(String args[]){
        int a = 24;
        int b = 12;

        int gcd = gcd(6, 9);
        System.out.println(a+"和"+b+"的最大公约数："+gcd);
    }

    /**
     * 欧几里得定律 求最大公约数
     * @param a
     * @param b
     * @return
     */
    public static int gcd(int a, int b) {

        if (b>a){
            int c;
            c = a;
            a = b;
            b = c;
        }

        if (b==0) {
            return a;
        }
        return gcd(b, a%b);
    }
}
