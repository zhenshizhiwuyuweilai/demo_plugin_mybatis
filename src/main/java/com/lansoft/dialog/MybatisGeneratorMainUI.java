package com.lansoft.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @Author 郭伟东
 * @Date 2020/12/14  22:17
 */
public class MybatisGeneratorMainUI extends DialogWrapper {
    private JPanel contentPane;
    private JTextField tableNamePrefix;
    private JLabel modelNameRemovePrefix;
    private JLabel nameDetails;
    private JLabel module;
    private JButton modelPButton;
    private JButton modelFButton;
    private JButton mapperPButton;
    private JButton mapperFButton;
    private JButton xmlPButton;
    private JButton xmlFPButton;
    private JList<String> selectedtableName;
    private JComboBox<String> modelPComboBox;
    private JTextField modelFText;
    private JComboBox<String> mapperPComboBox;
    private JTextField mapperFText;
    private JComboBox<String> xmlPComboBox;
    private JTextField xmlFText;
    private JComboBox<String> modelName;
    private JCheckBox lombokCheckBox;
    private JComboBox<String> lombokComboBox;

    EditorTextField tfTitle;
    EditorTextField tfMark;

    public MybatisGeneratorMainUI() {
        super(true);
        setTitle("Mybatis 逆向工程");
        setSize(700, 800);
        init();
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        return contentPane;
    }

    @Override
    protected void doOKAction() {
        if (this.getOKAction().isEnabled()) {
            System.out.println("===================start=================");
            System.out.println(toString());
            System.out.println("===================end================");

            //关闭窗口
            this.close(0);
        }
    }

    @Override
    public String toString() {
        String str = "MybatisGeneratorMainUI:{" +
                "’modelNameRemovePrefix‘:’" + modelNameRemovePrefix.getText() +
                "‘,’tableNamePrefix’:‘" + tableNamePrefix.getText() +
                "’,‘modelName’:‘" + modelName.getSelectedItem() +
                "‘,’modelPComboBox’:‘" + modelPComboBox.getSelectedItem() +
                "‘,’modelFText‘:’" + modelFText.getText() +
                "’,‘mapperPComboBox‘:’" + mapperPComboBox.getSelectedItem() +
                "‘,mapperFText‘:’" + mapperFText.getText() +
                "’,‘xmlPComboBox’:‘" + xmlPComboBox.getSelectedItem() +
                "’,‘xmlFText’:‘" + xmlFText.getText() +
                "‘,’lombokCheckBox‘:’" + lombokCheckBox.isSelected() +
                "’,‘lombokComboBox’:‘" + lombokComboBox.getSelectedItem() + "}";

        return str;
    }

    //    @Nullable
//    @Override
//    protected JComponent createCenterPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        tfTitle = new EditorTextField("笔记的标题");
//        tfMark = new EditorTextField("笔记的内容");
//        //设置宽高
//        tfMark.setPreferredSize(new Dimension(200, 200));
//        panel.add(tfTitle, BorderLayout.NORTH);
//        panel.add(tfMark, BorderLayout.CENTER);
//        return panel;
////        return this;
//    }
}
