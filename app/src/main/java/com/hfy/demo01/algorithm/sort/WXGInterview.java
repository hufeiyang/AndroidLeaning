package com.hfy.demo01.algorithm.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WXGInterview {

    //微信一面算法题：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target 的两两数对，并返回它们的数组下标
    //这是当时写的错的，整体思路是对的，但处理有问题：
//    public static void main(String[] args) {
//
//        int[] nums = new int[]{ 1,1,1,1,1};
//        int target = 2;
//        List<List<Integer>> result = findNum(nums, target);
//
//        for (List<Integer> list: result) {
//            for (Integer index : list) {
//                System.out.print(index);
//            }
//            System.out.println();
//        }
//    }
//
//    public static List<List<Integer>> findNum(int[] nums, int target){
//
//        int length = nums.length;
//
//        List<List<Integer>> result = new ArrayList<>();
//
//        Map<Integer, List<Integer>> map = new HashMap<>();
//
//        for(int i=0; i<length;i++){
//            if(map.containsKey(target-nums[i])){
//                List<Integer> indexList  = map.get(target-nums[i]);
//                for (Integer index: indexList) {
//                    List<Integer> temp = new ArrayList<>();
//                    temp.add(i);
//                    temp.add(index);
//                    result.add(temp);
//                }
//            }else{ //这里不应该放在else里面的，哎！
//                List<Integer> list  = map.get(nums[i]);
//                if(list==null){
//                    list = new ArrayList<>();
//                }
//                list.add(i);
//                map.put(nums[i], list);
//            }
//        }
//
//        return result;
//    }

    //这是在上面的修改：
    public static void main(String[] args) {
//        int[] nums = new int[]{ 1,1,1,1,1};
//        int target = 2;
        int[] nums = new int[]{1, 1, 2, 2, 3};
        int target = 4;
        List<List<Integer>> result = findNum(nums, target);

        for (List<Integer> list : result) {
            for (Integer index : list) {
                System.out.print(index);
            }
            System.out.println();
        }
    }

    public static List<List<Integer>> findNum(int[] nums, int target) {

        int length = nums.length;

        List<List<Integer>> result = new ArrayList<>();

        Map<Integer, List<Integer>> map = new HashMap<>();

        for (int i = 0; i < length; i++) {
            if (map.containsKey(target - nums[i])) {
                List<Integer> indexList = map.get(target - nums[i]);
                for (Integer index : indexList) {
                    List<Integer> temp = new ArrayList<>();
                    temp.add(index);
                    temp.add(i);
                    result.add(temp);
                }
            }

            List<Integer> list = map.get(nums[i]);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(i);
            map.put(nums[i], list);
        }

        return result;
    }


    //这是chatGPT的答案：
//    public static void main(String[] args) {
//        int[] nums = {1, 1, 1, 1, 1};
//        int target = 2;
//        List<int[]> pairs = findPairs(nums, target);
//        for (int[] pair : pairs) {
//            System.out.println(Arrays.toString(pair));
//        }
//    }
//
//
//    public static List<int[]> findPairs(int[] nums, int target) {
//        Map<Integer, List<Integer>> indexMap = new HashMap<>();
//        List<int[]> result = new ArrayList<>();
//
//        for (int i = 0; i < nums.length; i++) {
//            int complement = target - nums[i];
//            if (indexMap.containsKey(complement)) {
//                List<Integer> complementIndices = indexMap.get(complement);
//                for (int index : complementIndices) {
//                    result.add(new int[]{index, i});
//                }
//            }
//            // Add current index to the map
//            if (indexMap.containsKey(nums[i])) {
//                indexMap.get(nums[i]).add(i);
//            } else {
//                List<Integer> indices = new ArrayList<>();
//                indices.add(i);
//                indexMap.put(nums[i], indices);
//            }
//        }
//
//        return result;
//    }
}
