package com.hfy.demo01.algorithm.recursion;

/**
 * 递归思想：汉诺塔
 * @author hufeiyang
 * @data 2021/2/28
 * @Description:
 */
public class HanoiTower {

    private static int mCount = 0;

    public static void main(String args[]){

        HanoiTower.hanoi(3, 'A', 'B', 'C');
        System.out.println("总共需要："+ mCount +"步");
    }

    /**
     * 把num个的盘子从 from柱子 借助 depend柱子 移动到 to柱子
     * @param num
     * @param from
     * @param depend
     * @param to
     * @return 移动的次数
     */
    public static int hanoi(int num, char from, char depend, char to) {
        if (num==1) {
            move(num, from, to);
            return mCount;
        }

        hanoi(num-1,from, to, depend);
        move(num, from, to);
        hanoi(num-1,depend, from, to);

        return mCount;
    }

    private static int move(int num, char from, char to) {
        System.out.println("第"+ mCount +"步："+"把第"+num+"个盘子 "+from+"----->"+to);
        mCount++;
        return mCount;
    }
}