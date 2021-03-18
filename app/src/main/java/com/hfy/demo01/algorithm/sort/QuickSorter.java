package com.hfy.demo01.algorithm.sort;

/**
 * 快速排序
 * 讲解：https://zhuanlan.zhihu.com/p/93129029
 * @author hufeiyang
 * @data 2021/2/27
 * @Description:
 */
public class QuickSorter {

    private static final String TAG = "QuickSorter";

    public static void sort(int[] origin, int low, int high){

        int i = low;
        int j = high;

        if (i<j){

            //基准数据
            int tmp = origin[low];

            //③不断重复①和②,知道low>=high时(其实是low=high),low或high的位置就是该基准数据在数组中的正确索引位置.
            while (i < j){

                //①先从队尾开始向前扫描: 当low < high时,如果a[high] > tmp,则high–,
                // 但如果a[high] < tmp,则将high的值赋值给low,即arr[low] = a[high], 同时要转换数组扫描的方式,即需要从队首开始向队尾进行扫描了
                while (i < j && origin[j]>=tmp){
                    j--;
                }
                origin[i] = origin[j];

                //同理,当从队首开始向队尾进行扫描时,如果a[low] < tmp,则low++,
                // 但如果a[low] > tmp了,则就需要将low位置的值赋值给high位置,即arr[low] = arr[high],同时将数组扫描方式换为由队尾向队首进行扫描.
                while (i < j && origin[i]<=tmp){
                    i++;
                }
                origin[j] = origin[i];
            }

            //把
            origin[i]=tmp;

            sort(origin, low, i-1);
            sort(origin, i+1, high);
        }
    }

    public static void main(String args[]){
        int[] a ={10,4,8,4,100,6,9,45,30};
        QuickSorter.sort(a, 0, a.length-1);

        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
}
