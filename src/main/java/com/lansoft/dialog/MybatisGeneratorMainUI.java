package com.lansoft.dialog;

import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.lansoft.generator.MybatisPlusGenerator;
import com.lansoft.model.MybatisConfig;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @Author 郭伟东
 * @Date 2020/12/14  22:17
 */
public class MybatisGeneratorMainUI extends DialogWrapper {
    private JLabel tablePrefix;
    private JLabel nameDetails;
    private JLabel projectPathLabel;
    private JButton modelPButton;
    private JButton modelFButton;
    private JButton mapperPButton;
    private JButton mapperFButton;
    private JButton xmlPButton;
    private JButton xmlFPButton;

    private JPanel contentPane;
    private final PsiElement[] psiElements;
    private final Project project;
    /**
     * 选中的表名
     */
    private final ArrayList<String> tableNameList = new ArrayList<>();

    private JTextField tableNamePrefix;
    private JTextField author;
    private JComboBox<String> modelPComboBox;
    private JTextField modelFText;
    private JComboBox<String> mapperPComboBox;
    private JTextField mapperFText;
    private JComboBox<String> xmlPComboBox;
    private JTextField xmlFText;
    private JComboBox<String> projectPath;
    private JCheckBox isLombok;
    private JComboBox<String> lombokComboBox;
    private JCheckBox isRestController;
    private JCheckBox isServiceImpl;
    private JCheckBox isService;
    private JCheckBox isUpperCamelCase;
    private JList<String> selectedtableName;


    public MybatisGeneratorMainUI(PsiElement[] psiElements, Project project) {
        super(true);
        this.psiElements = psiElements;
        this.project = project;
        readTableName(psiElements);
        //初始化数据（回显数据）
        initializeData();
        setTitle("Mybatis 逆向工程");
        setSize(700, 800);
        init();
    }

    /**
     * 初始化数据
     */
    public void initializeData() {
        //回显选中的表名到左侧的列表中
        listShow(psiElements);
        //初始化默认
        projectPath.setSelectedItem(project.getBasePath());
        modelPComboBox.setSelectedItem("com.lansoft.entity");
        modelFText.setText("/src/main/java");
        mapperPComboBox.setSelectedItem("com.lansoft.mapper");
        mapperFText.setText("/src/main/java");
        xmlPComboBox.setSelectedItem("mybatis.mapper");
        xmlFText.setText("/src/main/resources");
        isLombok.setSelected(true);
        isUpperCamelCase.setSelected(true);
    }

    /**
     * 获取到选中的表名存起来备用
     *
     * @param psiElements 触发事件收到的参数
     */
    private void readTableName(PsiElement[] psiElements) {
        for (PsiElement psiElement : psiElements) {
            final DbTable dbTable = (DbTable) psiElement;
            tableNameList.add(dbTable.getName());
        }
    }

    /**
     * 回显选中的表名到左侧的列表中
     *
     * @param psiElements 触发事件收到的参数
     */
    public void listShow(PsiElement[] psiElements) {
        selectedtableName.setModel(new AbstractListModel<String>() {
            @Override
            public int getSize() {
                return psiElements.length;
            }

            @Override
            public String getElementAt(int i) {
                return ((DbTable) psiElements[i]).getName();
            }
        });
    }

    /**
     * 将容器返回给idea
     * 在idea中显示当前窗口
     * 必须
     *
     * @return
     */
    @Override
    protected JComponent createCenterPanel() {
        //将容器返回给idea
        return contentPane;
    }


    @Override
    protected void doOKAction() {
        //保存用户设置的mybatis 配置
        final MybatisConfig mybatisConfig = new MybatisConfig(this);
        //实行生成操作
        MybatisPlusGenerator.generatorCode(mybatisConfig);
        //显示通知窗口
        Messages.showMessageDialog("Mybatis生成完成", "Mybatis通知效果", Messages.getInformationIcon());
//        if (this.getOKAction().isEnabled()) {
//            System.out.println("===================start=================");
//            System.out.println(toString());
//            System.out.println("===================end================");
//        }
        //关闭窗口
        this.close(0);
    }

    @Override
    public String toString() {
        String str = "MybatisGeneratorMainUI:{" +
                "’tablePrefix‘:’" + tablePrefix.getText() +
                "‘,’tableNamePrefix’:‘" + tableNamePrefix.getText() +
                "’,‘projectPath’:‘" + projectPath.getSelectedItem() +
                "‘,’modelPComboBox’:‘" + modelPComboBox.getSelectedItem() +
                "‘,’modelFText‘:’" + modelFText.getText() +
                "’,‘mapperPComboBox‘:’" + mapperPComboBox.getSelectedItem() +
                "‘,mapperFText‘:’" + mapperFText.getText() +
                "’,‘xmlPComboBox’:‘" + xmlPComboBox.getSelectedItem() +
                "’,‘xmlFText’:‘" + xmlFText.getText() +
                "‘,’isLombok‘:’" + isLombok.isSelected() +
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


    public PsiElement[] getPsiElements() {
        return psiElements;
    }

    public JTextField getTableNamePrefix() {
        return tableNamePrefix;
    }

    public JLabel getTablePrefix() {
        return tablePrefix;
    }

    public JTextField getAuthor() {
        return author;
    }

    public JComboBox<String> getModelPComboBox() {
        return modelPComboBox;
    }

    public JTextField getModelFText() {
        return modelFText;
    }

    public JComboBox<String> getMapperPComboBox() {
        return mapperPComboBox;
    }

    public JTextField getMapperFText() {
        return mapperFText;
    }

    public JComboBox<String> getXmlPComboBox() {
        return xmlPComboBox;
    }

    public JTextField getXmlFText() {
        return xmlFText;
    }

    public JComboBox<String> getProjectPath() {
        return projectPath;
    }

    public JCheckBox getIsLombok() {
        return isLombok;
    }

    public JCheckBox getIsRestController() {
        return isRestController;
    }

    public JCheckBox getIsServiceImpl() {
        return isServiceImpl;
    }

    public JCheckBox getIsService() {
        return isService;
    }

    public JCheckBox getIsUpperCamelCase() {
        return isUpperCamelCase;
    }

    public ArrayList<String> getTableNameList() {
        return tableNameList;
    }
}
