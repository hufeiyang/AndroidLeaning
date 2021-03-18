package com.hfy.demo01.algorithm.sort;

import java.util.ArrayList;

/**
 * 基数排序（适用于正整数，当然也可以处理）
 * https://blog.csdn.net/qq_27124771/article/details/87697681
 * @author hufeiyang
 * @data 2021/2/28
 * @Description:
 */
public class RadixSorter {

    public static void main(String args[]){

        int[] a ={10,4,8,4,100,0,68,9,45,30,53,247};

        RadixSorter.sort(a);

        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    public static void sort(int[] array){
        if (array == null || array.length<2) {
            return;
        }

        int length = array.length;

        // 找到 最大数
        int max = 0;
        for (int i = 0; i < length; i++) {
            max = Math.max(max,array[i]);
        }

        //按最大数 算出最大位数
        int times = 0;
        while (max>0){
            max = max/10;
            times++;
        }

        //用于存 每次循环的 二位数组（也可以理解是桶）
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayLists.add(new ArrayList<Integer>());
        }

        //按个位、十位、百位 等，分别循环
        for (int i = 0; i < times; i++) {

            //按位数值放入对应下标的桶中
            for (int j = 0; j < length; j++) {
                //取出对应位数的值（例如 i为1时，1、13、27，取得就是1、3、7）
                int x = (int) (array[j] % Math.pow(10, i + 1) / Math.pow(10, i));
                //放入对应位置的桶
                if (x>=0) { //因为是下标，只能为正整数
                    arrayLists.get(x).add(array[j]);
                }
            }

            //从桶中取出来按顺序放回去
            int index = 0;
            for (int k = 0; k < 10; k++) {
                ArrayList<Integer> integers = arrayLists.get(k);
                for (Integer integer : integers){
                    array[index++] = integer;
                }
                integers.clear();
            }
        }

    }
}
