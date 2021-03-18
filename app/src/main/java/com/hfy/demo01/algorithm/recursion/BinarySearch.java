package com.hfy.demo01.algorithm.recursion;

import com.hfy.demo01.algorithm.sort.QuickSorter;

/**
 * 递归思想：二分法查找
 * @author hufeiyang
 * @data 2021/2/28
 * @Description:
 */
public class BinarySearch {

    public static void main(String args[]){

        int[] a ={10,4,8,4,100,6,9,10,45,30};

        //先排序
        QuickSorter.sort(a,0,a.length-1);

        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }

        int elem = 45;
        int elemIndex = BinarySearch.search(elem, a, 0, a.length - 1);

        for (int i = 0; i < a.length; i++) {
        }
        System.out.println(elem+"的下标是："+elemIndex);
    }


    /**
     * 二分法查找
     * @param elem 要找的元素
     * @param array 所在数组，需要是已经排好序的。
     * @param low
     * @param high
     * @return 要找的元素的下标
     */
    public static int search(int elem, int[] array, int low, int high){

        int mid = (low + high) / 2;

        if (array[mid]==elem){
            return mid;
        }

        if (array[mid]>elem) {
            return search(elem, array, low, mid-1);
        }

        if (array[mid]<elem) {
            return search(elem, array, mid+1, high);
        }

        return -1;
    }
}
