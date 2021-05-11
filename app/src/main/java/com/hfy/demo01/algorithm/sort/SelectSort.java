package com.hfy.demo01.algorithm.sort;

/**
 * 选择排序
 * @author hufeiyang
 * @data 2021/4/30
 * @Description:
 */
public class SelectSort {

    public static void main(String args[]){

        int[] a ={10,4,8,4,100,6,9,45,30};
        SelectSort.sort(a);

        for (int value : a) {
            System.out.println(value);
        }
    }

    /**
     * 选择排序：选择一个数a[i]，与后面数比较，找到min，min与a[i]交换
     * @param a
     */
    private static void sort(int[] a){
        for(int i = 0;i < a.length - 1; i++){
            int minIndex = i; //记录min下标
            for(int j = i + 1; j < a.length;j++){
                if(a[minIndex] > a[j]){
                    minIndex = j;
                }
            }
            //走完，把找到的真正的min，与前面选择的 a[i] 替换。
            int temp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = temp;
        }
    }
}
