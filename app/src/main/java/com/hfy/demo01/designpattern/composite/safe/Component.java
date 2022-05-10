package com.hfy.demo01.designpattern.composite.safe;

/**
 * Component，抽象构件
 * @author bytedance
 * @date 4/20/22
 * @desc 安全组合模式
 */
public abstract class Component {

    public String name;

    public Component(String name) {
        this.name = name;
    }

    /**
     * composite、leaf的公有方法
     */
    public abstract void print();
}
