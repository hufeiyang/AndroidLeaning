package com.hfy.demo01.algorithm.dp;

/**
 * 动态规划(dynamic programming)：最长公共子序列
 * @author hufeiyang
 * @data 2021/3/7
 * @Description: android，random，的公共子序列：and，（ 要求 有序 但 不要求连续 ）
 */
public class LCS {
    public static void main(String args[]){

        String m = "android";
        String n = "random";

        int[][] lcs = lcs(m, n);

    }

    private static int[][] lcs(String m, String n) {

        int lengthM = m.length();
        int lengthN = n.length();

        char[] charsM = m.toCharArray();
        char[] charsN = n.toCharArray();

        //公共子序列的长度
        int[][] lcsCount = new int[lengthM][lengthN];
        //公共子序列
//        List<String> lcsString = new ArrayList<>();

        //初始化 首行 首列
        for (int i = 0; i < lengthM; i++) {
            if (charsN[0] == charsM[i]){
                for (int j = i;j < lengthM; j++){
                    lcsCount[j][0] = 1;
                }
                break;
            }
        }

        for (int i = 0; i < lengthN; i++) {
            if (charsM[0] == charsN[i]){
                for (int j = i;j < lengthN; j++){
                    lcsCount[0][j] = 1;
                }
                break;
            }
        }


        for (int i = 1; i < lengthM; i++) {
            for (int j = 1; j < lengthN; j++) {
                if (charsM[i]== charsN[j]) {
                    lcsCount[i][j] = lcsCount[i-1][j-1]+1;
//                    lcsString.add(String.valueOf(charsM[i]));
                }else {
                    lcsCount[i][j] = Math.max(lcsCount[i][j-1],lcsCount[i-1][j]);
                }
            }
        }

        System.out.println(m+" 和 "+n+" 最长公共子序列长度：" + lcsCount[lengthM-1][lengthN-1]);

        return lcsCount;
    }
}
