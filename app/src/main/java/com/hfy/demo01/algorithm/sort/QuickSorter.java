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

    public static void main(String[] args){
        int[] a ={10,4,8,4,100,6,9,45,30};
//        QuickSorter.sort(a, 0, a.length-1);
        QuickSorter.quickSort(a,0,a.length -1);

        for (int value : a) {
            System.out.println(value);
        }
    }

    //快速排序：从冒泡排序演变，使用分治思想，比冒泡高效，所以叫快速排序。
    //选择一个基准b，小于b的放左边、大于b放右边。 a<b<c ,那么 a<c 是必然的，就不需要进行比较了。可用填坑法完成。
    //所以，只需要分别在操作左右部分（就是递归了）
    private static void quickSort(int[] a, int low, int high){
        if(low >= high){
            return;
        }
        int i = low;
        int j = high;

        int temp = a[i]; //取第一个为基准，比较其他所有数（此位置就看做一个坑位）
        while(i < j){
            //右侧找小于temp的,大于就左移
            while(i < j && a[j] >= temp){
                j--;
            }
            a[i] = a[j];//此时a[j] < temp，放到坑位中

            while(i < j && a[i] <= temp){ //左侧找大于temp的
                i++;
            }
            a[j] = a[i];//此时a[j] < temp，放到坑位中
        }
        a[i] = temp; //这里i=j,也是坑位，把基准数放入即可。这样就完成了 小于temp的在左边，大于temp的放右边

        //然后，再递归 左右两边即可‘
        quickSort(a,low,i-1);
        quickSort(a,i+1,high);
    }


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

}
