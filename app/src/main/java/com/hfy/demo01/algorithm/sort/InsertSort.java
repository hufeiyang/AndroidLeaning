package com.hfy.demo01.algorithm.sort;

/**
 * 插入排序
 * @author hufeiyang
 * @data 2021/4/30
 * @Description:
 */
public class InsertSort {

    public static void main(String args[]){

        int[] a ={10,4,8,4,100,6,9,45,30};
        InsertSort.sort(a);

        for (int value : a) {
            System.out.println(value);
        }
    }

    /**
     * 插入排序：前部分是有序的，遍历无须的部分，位置i与 前面的作比较，找到该插入的位置。过程中，位置i和目标位置中间的全部后移一位
     * @param a
     */
    private static void sort(int[] a){
        //从1开始，即开始时把0位置当做有序部分
        for(int i = 1;i < a.length; i++){
            int temp = a[i];
            //向前比较
            int j = i - 1;
            for(;j >= 0;j--){
                if(temp < a[j]){ //比你小，就要后移一位
                    a[j+1] = a[j];
                }else{
                    break;//不小，直接break，不用在向前比较了
                }
            }
            //遍历完后：j+1,就是直接temp要插入的位置
            a[j+1] = temp;
        }
    }
}
