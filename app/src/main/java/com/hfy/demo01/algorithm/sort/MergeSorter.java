package com.hfy.demo01.algorithm.sort;

/**
 * 归并排序
 * 讲解：https://www.cnblogs.com/chengxiao/p/6194356.html
 * @author hufeiyang
 * @data 2021/2/27
 * @Description:
 */
public class MergeSorter {

    public static void main(String args[]){
        int[] a ={10,4,8,4,100,6,9,45,30};
        MergeSorter.sort(a, 0, a.length-1);

        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    public static void sort(int[] origin, int low, int high) {

        if (high<=low)return;

        int mid = (low+high)/2;
        sort(origin, low, mid);
        sort(origin, mid + 1, high);

        int[] tmp = new int[origin.length];// TODO: 2021/2/27 可以外部传入，避免频繁开辟空间

        int i = low;
        int j = mid+1;
        int tmpIndex = 0;

        while (i<=mid && j<=high){
            if (origin[i]<origin[j]) {
                tmp[tmpIndex++]=origin[i++];
            }else {
                tmp[tmpIndex++]=origin[j++];
            }
        }

        //左边剩余的赋值过去
        while (i<=mid){
            tmp[tmpIndex++] = origin[i++];
        }
        while (j<=high){
            tmp[tmpIndex++] = origin[j++];
        }

        //排好序的赋值过去
        tmpIndex = 0;
        while (low<=high){
            origin[low++]=tmp[tmpIndex++];
        }
    }
}
