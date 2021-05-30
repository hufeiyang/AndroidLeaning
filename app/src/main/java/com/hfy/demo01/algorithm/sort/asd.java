package com.hfy.demo01.algorithm.sort;

/**
 * @author hufeiyang
 * @data 2021/5/12
 * @Description:
 */
public class asd {


    public void sort(int[] arr, int low, int high){
        if(low >= high){
            return;
        }

        int temp = arr[low];
        int left = low;
        int right = high;
        while(left < right){
            while(left < right && arr[right] >= temp){
                right--;
            }
            arr[left] = arr[right];

            while(left < right && arr[left] <= temp){
                left++;
            }
            arr[right] = arr[left];
        }

        arr[left] = temp;

        sort(arr, low, left-1);
        sort(arr, left+1, right);
    }
}
