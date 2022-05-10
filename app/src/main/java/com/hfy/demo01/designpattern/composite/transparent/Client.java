package com.hfy.demo01.designpattern.composite.transparent;

/**
 * @author bytedance
 * @date 4/20/22
 * @desc
 */
public class Client {
    public static void main(String[] args) {


        Component root = new Composite("根");

        Component branch1 = new Composite("树枝1");
        Component branch2 = new Composite("树枝2");

        Component leaf1 = new Leaf("叶子1");
        Component leaf2 = new Leaf("叶子2");

        branch1.add(leaf1);
        branch1.add(leaf2);

        root.add(branch1);
        root.add(branch2);

        test(root);

    }

    /**
     * 依赖抽象 Component即可
     * @param root
     */
    private static void test(Component root) {
        root.print();
    }
}
