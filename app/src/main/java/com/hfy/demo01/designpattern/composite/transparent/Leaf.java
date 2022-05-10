package com.hfy.demo01.designpattern.composite.transparent;

import java.util.ArrayList;

/** Leaf
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

    @Override
    public void add(Component c) {
        throw new UnsupportedOperationException("leaf 没有子节点！");
    }

    @Override
    public void remove(Component c) {
        throw new UnsupportedOperationException("leaf 没有子节点！");
    }

    @Override
    public Component getChild(int i) {
        throw new UnsupportedOperationException("leaf 没有子节点！");
    }

    @Override
    public ArrayList<Component> getChildren() {
        throw new UnsupportedOperationException("leaf 没有子节点！");
    }
}
