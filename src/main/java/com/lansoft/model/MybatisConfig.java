package com.lansoft.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.intellij.openapi.project.Project;
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
@Data
public class MybatisConfig {
    /**
     * The Table element.
     */
    @JSONField(serialize = false)
    public PsiElement[] psiElements;

    @JSONField(serialize = false)
    private Project project;

    /**
     * 选中的表名
     */
    private List<String> tableNameList;

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
     */
    private boolean isUpperCamelCase;
    /**
     *
     */
    private String moduleName;

    /**
     *
     */
    private String pluginType;

    public MybatisConfig() {
    }

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
        this.moduleName = (String) ( main.getModuleName().getSelectedItem());
        this.pluginType = (String) main.getPluginType().getSelectedItem();
        this.project = main.getProject();
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

    public Project getProject() {
        return project;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getPluginType() {
        return pluginType;
    }

    public void setPsiElements(PsiElement[] psiElements) {
        this.psiElements = psiElements;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setTableNameList(List<String> tableNameList) {
        this.tableNameList = tableNameList;
    }

    public void setTableNamePrefix(String tableNamePrefix) {
        this.tableNamePrefix = tableNamePrefix;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public void setModelPackage(String modelPackage) {
        this.modelPackage = modelPackage;
    }

    public void setModelFolder(String modelFolder) {
        this.modelFolder = modelFolder;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public void setMapperFolder(String mapperFolder) {
        this.mapperFolder = mapperFolder;
    }

    public void setXmlPackage(String xmlPackage) {
        this.xmlPackage = xmlPackage;
    }

    public void setXmlFolder(String xmlFolder) {
        this.xmlFolder = xmlFolder;
    }

    public void setLombok(boolean lombok) {
        isLombok = lombok;
    }

    public void setRestController(boolean restController) {
        isRestController = restController;
    }

    public void setService(boolean service) {
        isService = service;
    }

    public void setServiceImpl(boolean serviceImpl) {
        isServiceImpl = serviceImpl;
    }

    public void setUpperCamelCase(boolean upperCamelCase) {
        isUpperCamelCase = upperCamelCase;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setPluginType(String pluginType) {
        this.pluginType = pluginType;
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
