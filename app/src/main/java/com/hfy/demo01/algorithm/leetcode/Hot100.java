package com.hfy.demo01.algorithm.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * @author hufeiyang
 * @data 2021/4/10
 * @Description:
 */
public class Hot100 {

    public static void main(String[] args) {
//        int[][] ints1 = new int[][]{};
//        int [][] ints2 = new int[5][5];
//
//        int[] a = new int[]{4,2,4,7,3,26,6,14,6,8};
//
////        bubbleSort(a);
////        selectSort(a);
////        insertSort(a);
//
//        for (int i : a) {
//            System.out.print(i+" ");
//        }

//        String[] s = new String[]{"eat","tea","tan","ate","nat","bat"};
//        List<List<String>> lists = groupAnagrams(s);
//        System.out.println("lists = " + lists);
//
//        Queue<String> q = new LinkedList<>();
//        q.

//        char[][] board = new char[][]{{"A","B","C","E"},["S","F","C","S"],["A","D","E","E"}};

//        boolean exist = exist(board, "ABCCED");
//        System.out.println("exist = " + exist);

//        "q".substring()
    }


    private static String sort(String string){
        if(string == null){
            return null;
        }

        char[] c = string.toCharArray();

        Arrays.sort(c);
        return Arrays.toString(c);
    }






    public static boolean canJump(int[] nums) {
        //最远能触达的index
        int maxReachIndex = 0;
        for(int i = 0; i < nums.length ; i++){
            maxReachIndex = Math.max(i + nums[i], maxReachIndex);
            System.out.println("maxReachIndex = " + maxReachIndex);
            if(maxReachIndex >= nums.length - 1){
                return true;
            }
        }

        return false;
    }

    public static boolean isValid(String s) {
        if (s == null || s.length() < 2) {
            return false;
        }

        //遇到 第一个右括号时 ，和它的左边必须匹配，
        //即，当前 最后的左括号 要先匹配 遇到的右括号 -- 栈

        Stack<Character> stack = new Stack<>();

        //遍历：遇到左括号 压入栈，遇到右括号 就和栈顶 的左括号匹配，直到遍历匹配完毕  栈为空 ，则ok
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur == '(' || cur == '[' || cur == '{') {
                stack.push(cur);
                System.out.println("push:" + cur);
            } else {
                //遇到右括号
                if (!stack.empty() && isPair(stack.peek(), cur)) {
                    //配对则弹出
                    System.out.println("cur:" + cur);
                    System.out.println("pop:" + stack.peek());
                    stack.pop();
                } else {
                    return false;
                }
            }
        }

        return stack.empty();
    }

    private static boolean isPair(char left, char right) {
        switch (left) {
            case '(':
                if (right == ')') return true;
                break;
            case '[':
                if (right == ']') return true;
                break;
            case '{':
                if (right == '}') return true;
                break;
        }
        return false;
    }

    private void fun(int[] nums) {

        if (nums.length < 2) {
            return;
        }
        boolean has = false;
        for (int i = nums.length - 1; i > 0; i--) {
            for (int j = nums.length - 2; j >= 0; j--) {
                if (nums[i] > nums[j]) {
                    int temp = nums[j];
                    nums[j] = nums[i];
                    nums[i] = temp;
                    has = true;
                    break;
                }
            }
            if (has = true) {
                break;
            }
        }
        if (!has) {
            Arrays.sort(nums);
        }
    }


    public static boolean exist(char[][] board, String word) {

        char[] chars = word.toCharArray();

        boolean[] record = new boolean[word.length()];

        int curI = 0;
        int curJ = 0;
        for(int i = 0; i < board.length ;i++){
            for(int j = 0;j < board[0].length;j++){
                if(board[i][j] == chars[0]){
                    curI = i;
                    curJ = j;
                    record[0] = true; //存在首字母
                }
            }
        }

        for(int i = 1; i < word.length() -1 ;i++){
            boolean hasNext = false;
            if(curJ - 1 >=0 && board[curI][curJ - 1] == chars[i]){
                hasNext = true;
                curJ = curJ - 1;
            }else if(curJ + 1 < board[0].length && chars[i] == board[curI][curJ + 1] ){
                hasNext = true;
                curJ =curJ + 1;
            }else if(curI + 1 < board.length && board[curI + 1][curJ] == chars[i]){
                hasNext = true;
                curI = curI + 1;
            }

            record[i] = record[i-1] && hasNext;
        }

        return record[word.length() - 1];
    }

}


