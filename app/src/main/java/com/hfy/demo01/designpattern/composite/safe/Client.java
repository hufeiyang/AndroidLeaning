package com.hfy.demo01.designpattern.composite.safe;

/**
 * @author bytedance
 * @date 4/20/22
 * @desc
 */
public class Client {
    public static void main(String[] args) {
        //依赖了具体实现类
        Composite root = new Composite("根");

        Composite branch1 = new Composite("树枝1");
        Composite branch2 = new Composite("树枝2");

        Leaf leaf1 = new Leaf("叶子1");
        Leaf leaf2 = new Leaf("叶子2");


        branch1.add(leaf1);
        branch1.add(leaf2);

        root.add(branch1);
        root.add(branch2);

        test(root);

    }

    /**
     * 依赖具体实现类: Composite才有add方法，而Component没有
     * @param root
     */
    private static void test(Composite root) {
        root.print();
    }
}
