package com.hfy.demo01.designpattern.composite.safe;

/**
 * Leaf，叶子构件
 * @author bytedance
 * @date 4/20/22
 * @desc
 */
public class Leaf extends Component {
    public Leaf(String name) {
        super(name);
    }

    @Override
    public void print() {
        System.out.println(name);
    }
}
