package com.lansoft.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.intellij.psi.PsiElement;
import com.lansoft.dialog.MybatisGeneratorMainUI;
import lombok.Data;

import java.util.List;

/**
 * mybatis 配置
 *
 * @Author 郭伟东
 * @Date 2021/1/25  14:03
 */
public class MybatisConfig {
    /**
     * The Table element.
     */
    @JSONField(serialize = false)
    public final PsiElement[] psiElements;

    /**
     * 选中的表名
     */
    private final List<String> tableNameList;

    /**
     * 表名前缀
     */
    private String tableNamePrefix;

    /**
     * 作者
     */
    private String author;

    /**
     * 项目根路径
     */
    private String projectPath;

    /**
     * 实体类包路径
     */
    private String modelPackage;

    /**
     * 实体类根据经（文件夹）
     */
    private String modelFolder;

    /**
     * Dao接口的包路径
     */
    private String mapperPackage;

    /**
     * Dao接口根据经（文件夹）
     */
    private String mapperFolder;

    /**
     * mapper.xml 的包路径
     */
    private String xmlPackage;

    /**
     * mapper.xml的根据经（文件夹）
     */
    private String xmlFolder;

    /**
     * 是否生成 lombok
     */
    private boolean isLombok;

    /**
     * 是否生成 RestController
     */
    private boolean isRestController;

    /**
     * 是否生成 Service
     */
    private boolean isService;

    /**
     * 是否生成 ServiceImpl
     */
    private boolean isServiceImpl;

    /**
     * 命名策略:
     *
     */
    private boolean isUpperCamelCase;
//    private String tableNamePrefix;
//    private String tableNamePrefix;
//    private String tableNamePrefix;
//    private String tableNamePrefix;


    public MybatisConfig(MybatisGeneratorMainUI main) {
        this.psiElements = main.getPsiElements();
        this.tableNameList = main.getTableNameList();
        this.tableNamePrefix = main.getTableNamePrefix().getText();
        this.author = main.getAuthor().getText();
        this.projectPath = (String) (main.getProjectPath().getSelectedItem());
        this.modelPackage = (String) (main.getModelPComboBox().getSelectedItem());
        this.modelFolder = main.getModelFText().getText();
        this.mapperPackage = (String) (main.getMapperPComboBox().getSelectedItem());
        this.mapperFolder = main.getMapperFText().getText();
        this.xmlPackage = (String) (main.getXmlPComboBox().getSelectedItem());
        this.xmlFolder = main.getXmlFText().getText();
        this.isLombok = main.getIsLombok().isSelected();
        this.isRestController = main.getIsRestController().isSelected();
        this.isService = main.getIsService().isSelected();
        this.isServiceImpl = main.getIsServiceImpl().isSelected();
        this.isUpperCamelCase = main.getIsUpperCamelCase().isSelected();
    }

    public PsiElement[] getPsiElements() {
        return psiElements;
    }

    public List<String> getTableNameList() {
        return tableNameList;
    }

    public String getTableNamePrefix() {
        return tableNamePrefix;
    }

    public String getAuthor() {
        return author;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getModelPackage() {
        return modelPackage;
    }

    public String getModelFolder() {
        return modelFolder;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public String getMapperFolder() {
        return mapperFolder;
    }

    public String getXmlPackage() {
        return xmlPackage;
    }

    public String getXmlFolder() {
        return xmlFolder;
    }

    public boolean isLombok() {
        return isLombok;
    }

    public boolean isRestController() {
        return isRestController;
    }

    public boolean isService() {
        return isService;
    }

    public boolean isServiceImpl() {
        return isServiceImpl;
    }

    public boolean isUpperCamelCase() {
        return isUpperCamelCase;
    }

    @Override
    public String toString() {
        return "MybatisConfig{" +
                "tableNamePrefix='" + tableNamePrefix + '\'' +
                ", projectPath='" + projectPath + '\'' +
                ", modelPackage='" + modelPackage + '\'' +
                ", modelFolder='" + modelFolder + '\'' +
                ", mapperPackage='" + mapperPackage + '\'' +
                ", mapperFolder='" + mapperFolder + '\'' +
                ", xmlPackage='" + xmlPackage + '\'' +
                ", xmlFolder='" + xmlFolder + '\'' +
                ", isLombok=" + isLombok +
                ", isRestController=" + isRestController +
                ", isService=" + isService +
                ", isServiceImpl=" + isServiceImpl +
                ", isUpperCamelCase=" + isUpperCamelCase +
                '}';
    }
}
