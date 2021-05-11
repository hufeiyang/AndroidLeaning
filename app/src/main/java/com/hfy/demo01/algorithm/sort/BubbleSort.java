package com.hfy.demo01.algorithm.sort;

/**
 * 冒泡排序
 * @author hufeiyang
 * @data 2021/4/30
 * @Description:
 */
public class BubbleSort {

    public static void main(String args[]){

        int[] a ={10,4,8,4,100,6,9,45,30};
        BubbleSort.sort(a);

        for (int value : a) {
            System.out.println(value);
        }
    }

    /**
     * 冒泡排序：相邻比较，每次找出剩余中的最大值
     * @param a
     */
    private static void sort(int[] a){
        if(a == null){
            return;
        }
        for(int i = 0;i < a.length - 1; i++){ //i，表示遍历第几趟，第i趟遍历完，就找到i+1的最大值放在了右侧
            //a.length - 1 - i，表示第i趟时，要比较的次数，
            // 因为在此趟前，已经找到了 i+1个在右侧了，所以不需要在再比较了。
            for(int j = 0; j < a.length - 1 - i;j++){
                if(a[j] > a[j+1]){
                    int temp = a[j+1];
                    a[j+1] = a[j];
                    a[j] = temp;
                }
            }
        }
    }

}
