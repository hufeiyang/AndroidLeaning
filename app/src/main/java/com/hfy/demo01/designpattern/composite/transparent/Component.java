package com.hfy.demo01.designpattern.composite.transparent;

import java.util.ArrayList;

/**
 * Component，抽象构件
 * @author bytedance
 * @date 4/20/22
 * @desc 透明组合模式
 */
public abstract class Component {
    protected String name;

    public Component(String name) {
        this.name = name;
    }
    public abstract void print();

    public abstract void add(Component c);

    public abstract void remove(Component c);

    public abstract Component getChild(int i);

    public abstract ArrayList<Component> getChildren();

}
