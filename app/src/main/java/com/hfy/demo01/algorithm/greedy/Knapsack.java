package com.hfy.demo01.algorithm.greedy;

import com.hfy.demo01.algorithm.sort.QuickSorter;

import java.util.HashMap;

/**
 * 贪心算法：部分背包问题
 * https://blog.csdn.net/hy6688_/article/details/15427943
 * @author hufeiyang
 * @data 2021/3/1
 * @Description:
 */
public class Knapsack {

    public static void main(String args[]){

        // 有三个物品，其重量分别为{20,30,10}，价值分别为{60,120,50}，背包的容量为50
        int capacity = 50;
        int[] w = {20,30,10};
        int[] v = {60,120,50};

        Knapsack.putMaxValueToKnapsack(w,v,capacity);

    }

    public static void putMaxValueToKnapsack(int[] w, int[] v, int capacity) {
        if (w == null || w.length==0) {
            return;
        }

        if (v == null || v.length==0){
            return;
        }

        int length = w.length;
        //性价比
        int[] p = new int[length];
        HashMap<Integer, Integer> priceIndex = new HashMap<>();
        for (int i = 0; i < length; i++) {
            int price = v[i] / w[i];
            p[i] = price;
            priceIndex.put(price, i);
        }

        //按性价比升序
        QuickSorter.sort(p, 0, p.length-1);

        int maxV = 0;
        //循环：先取性价比高的，直到装满
        for (int i = p.length-1; i >= 0; i--) {

            int index = priceIndex.get(p[i]);

            if (w[index]>capacity) {
                maxV += p[index]*capacity;
                System.out.println("拿w"+ index +"一部分："+capacity+",价值："+ p[index]*capacity);
                capacity -= capacity;
            }else {
                maxV += v[index];
                System.out.println("拿w"+ index +",价值："+v[index]);
                capacity -= w[index];
            }

            if (capacity<=0) {
                System.out.println("拿满，价值："+ maxV);
                break;
            }
        }

    }
}
