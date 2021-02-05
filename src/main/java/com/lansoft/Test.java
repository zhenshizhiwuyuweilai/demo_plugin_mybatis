package com.lansoft;

import javax.swing.*;

/**
 * @Author 郭伟东
 * @Date 2021/1/25  9:42
 */
public class Test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Java列表框组件示例");
        JPanel jp = new JPanel();    //创建面板
        JLabel label1 = new JLabel("证件类型：");    //创建标签
        String[] items = new String[]{"身份证", "驾驶证", "军官证"};
        JList list = new JList(items);    //创建JList
        jp.add(label1);
        jp.add(list);
        frame.add(jp);
        frame.setBounds(300, 200, 400, 100);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
