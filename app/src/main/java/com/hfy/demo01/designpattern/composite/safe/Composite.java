package com.hfy.demo01.designpattern.composite.safe;

import java.util.ArrayList;

/**
 * @author bytedance
 * @date 4/20/22
 * @desc
 */
public class Composite extends Component {

    private ArrayList<Component> children = new ArrayList<>();

    public Composite(String name) {
        super(name);
    }

    @Override
    public void print() {
        //先打印自己
        System.out.println(name);
        //再遍历打印children
        for (Component child : children) {
            child.print();
        }
    }


    public void add(Component c) {
        children.add(c);
    }

    public void remove(Component c) {
        children.remove(c);
    }

    public Component getChild(int i) {
        return children.get(i);
    }

    public ArrayList<Component> getChildren() {
        return children;
    }

}
