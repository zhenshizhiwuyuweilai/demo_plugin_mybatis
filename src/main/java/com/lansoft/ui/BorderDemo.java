package com.lansoft.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @Author 郭伟东
 * @Date 2021/1/15  17:48
 */
@Deprecated
public class BorderDemo extends JFrame {
    private JButton buttons[];

    public static void main(String[] args) {
        new BorderDemo();

    }

    public BorderDemo() {
        init();
    }

    public void init() {
        JPanel pan = new JPanel(new GridLayout(0, 2, 5, 10));
        //JPanel pan = new JPanel(new GridLayout(4,2));
        //建立一个空的边界，并指定上下左右的宽度，
        pan.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.buttons = new JButton[8];
        for (int i = 0; i < this.buttons.length; i++)
            this.buttons[i] = new JButton();


        this.buttons[0].setText("线边框");
        //设置为线边框
        this.buttons[0].setBorder(BorderFactory.createLineBorder(Color.red, 3));
        pan.add(this.buttons[0]);

        this.buttons[1].setText("蚀刻边框");
        //设置为蚀刻边框
        this.buttons[1].setBorder(BorderFactory.createEtchedBorder());
        pan.add(this.buttons[1]);

        this.buttons[2].setText("斜面边框(凸)");
        //设置为斜面边框(凸)
        this.buttons[2].setBorder(BorderFactory.createRaisedBevelBorder());
        pan.add(this.buttons[2]);

        this.buttons[3].setText("斜面边框(凹)");
        //设置为斜面边框(凹)
        this.buttons[3].setBorder(BorderFactory.createLoweredBevelBorder());
        pan.add(this.buttons[3]);

        this.buttons[4].setText("标题边框");
        //设置为标题边框
        this.buttons[4].setBorder(BorderFactory.createTitledBorder("标题"));
        pan.add(this.buttons[4]);

        this.buttons[5].setText("标签边框(右)");
        TitledBorder tb = BorderFactory.createTitledBorder("标题");
        tb.setTitleJustification(TitledBorder.RIGHT);

        this.buttons[5].setBorder(tb);
        pan.add(this.buttons[5]);

        this.buttons[6].setText("花色边框");
        //设置为花色边框
        this.buttons[6].setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, Color.yellow));
        pan.add(this.buttons[6]);

        this.buttons[7].setText("组合边框");
        Border b1 = BorderFactory.createLineBorder(Color.blue, 2);
        Border b2 = BorderFactory.createEtchedBorder();

        //设置为组合边框
        this.buttons[7].setBorder(BorderFactory.createCompoundBorder(b1, b2));
        pan.add(this.buttons[7]);

        for (int i = 0; i < this.buttons.length; i++)
            pan.add(this.buttons[i]);
        pan.setPreferredSize(new Dimension(500, 500));
        this.add(pan);


        //设置窗口属性
        this.setTitle("组件边框演示");
        this.setVisible(true);
        //this.setSize(500, 600);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
