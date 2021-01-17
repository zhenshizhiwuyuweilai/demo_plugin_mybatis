package com.lansoft.dialog;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.EditorTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * mybatis 逆向工程
 *
 * @Author 郭伟东
 * @Date 2021/1/17  17:11
 */
@Deprecated
public class MybatisPlusGeneratorDialog extends DialogWrapper {

    public MybatisPlusGeneratorDialog() {
        super(true);
        setTitle("Mybatis 逆向工程");
        setSize(700,800);
        init();
    }

    EditorTextField tfTitle;
    EditorTextField tfMark;

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        tfTitle = new EditorTextField("笔记的标题");
        tfMark = new EditorTextField("笔记的内容");
        //设置宽高
        tfMark.setPreferredSize(new Dimension(200, 200));
        panel.add(tfTitle, BorderLayout.NORTH);
        panel.add(tfMark, BorderLayout.CENTER);
        return panel;
//        final MybatisGeneratorMainUI mainUI = new MybatisGeneratorMainUI();
//        mainUI.setVisible(true);
//        mainUI.setPreferredSize(new Dimension(700, 800));
//        return mainUI;
    }

    @Override
    protected JComponent createSouthPanel() {
        JPanel panel = new JPanel();
        JButton button = new JButton("添加笔记到列表");
        button.addActionListener(e -> {
            String title = tfTitle.getText();
            String mark = tfMark.getText();
//            String fileType = DataCenter.FILE_NAME.substring(DataCenter.FILE_NAME.lastIndexOf(".") + 1);
//            NoteData noteData = new NoteData(title,mark, DataCenter.SELECTED_TEXT,DataCenter.FILE_NAME,fileType);
//            DataCenter.NOTE_LIST.add(noteData);
//
//            String[] convert = DataConvert.convert(noteData);
//            DataCenter.TABLE_MODEL.addRow(convert);
        });
        panel.add(button);
        return panel;
    }
}
