package com.lansoft.generator.custom;

import com.intellij.jam.view.DeleteHandler;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 * @author guowd
 * @date 2021/6/6  5:49
 */
@Deprecated
public class CommentCustomGenerator implements CommentGenerator {
    private Properties properties = new Properties();
    private String author = "author";
    private boolean isTkMapper = false;
    private IntrospectedTable introspectedTable;
    private boolean suppressDate = false;
    private boolean suppressAllComments = false;
    private boolean addRemarkComments = false;
    private String beginningDelimiter = "";
    private String endingDelimiter = "";
    private boolean forceAnnotation;
    private boolean needsSwagger;
    private SimpleDateFormat dateFormat;

    public CommentCustomGenerator() {
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
    }

    @Override
    public void addComment(XmlElement xmlElement) {
        String tableName = this.introspectedTable.getFullyQualifiedTable().toString();
        if ("resultMap".equals(xmlElement.getName())) {
            xmlElement.addElement(new TextElement("<!--@mbg.generated  " + this.getDateString() + "-->"));
            StringBuilder sb = new StringBuilder();
            sb.append("<!--");
            sb.append("@Table ");
            sb.append(tableName);
            sb.append("-->");
            xmlElement.addElement(new TextElement(sb.toString()));
        }

    }

    @Override
    public void addRootComment(XmlElement rootElement) {
    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable, Set<FullyQualifiedJavaType> imports) {
    }

    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        this.suppressDate = StringUtility.isTrue(properties.getProperty("suppressDate"));
        this.suppressAllComments = StringUtility.isTrue(properties.getProperty("suppressAllComments"));
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
        this.author = properties.getProperty("author");
        String isTkMapper = properties.getProperty("isTkMapper");
        if (StringUtility.stringHasValue(isTkMapper)) {
            this.isTkMapper = "TRUE".equalsIgnoreCase(isTkMapper);
        }

        String beginningDelimiter = properties.getProperty("beginningDelimiter");
        if (StringUtility.stringHasValue(beginningDelimiter)) {
            this.beginningDelimiter = beginningDelimiter;
        }

        String endingDelimiter = properties.getProperty("endingDelimiter");
        if (StringUtility.stringHasValue(endingDelimiter)) {
            this.endingDelimiter = endingDelimiter;
        }

        String forceAnnotation = properties.getProperty("forceAnnotation");
        if (StringUtility.stringHasValue(forceAnnotation)) {
            this.forceAnnotation = "TRUE".equalsIgnoreCase(forceAnnotation);
        }

        String needsSwagger = properties.getProperty("needsSwagger");
        if (StringUtility.stringHasValue(needsSwagger)) {
            this.needsSwagger = "TRUE".equalsIgnoreCase(needsSwagger);
        }

        String dateFormatString = properties.getProperty("dateFormat");
        if (StringUtility.stringHasValue(dateFormatString)) {
            this.dateFormat = new SimpleDateFormat(dateFormatString);
        }

    }

    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append("@mbg.generated");
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }

        String s = this.getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }

        javaElement.addJavaDocLine(sb.toString());
    }

    protected String getDateString() {
        SimpleDateFormat dateFormat;
        if (this.suppressDate) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(new Date());
        } else if (this.dateFormat != null) {
            return this.dateFormat.format(new Date());
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format(new Date());
        }
    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
        StringBuilder sb = new StringBuilder();
        topLevelClass.addJavaDocLine("/**");
        String remarks = introspectedTable.getRemarks();
        if (this.addRemarkComments && StringUtility.stringHasValue(remarks)) {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            String[] var6 = remarkLines;
            int var7 = remarkLines.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                String remarkLine = var6[var8];
                topLevelClass.addJavaDocLine(" * " + remarkLine);
            }
        }

        sb.append(" * table ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        topLevelClass.addJavaDocLine(sb.toString());
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * @author " + this.author);
        topLevelClass.addJavaDocLine(" * @date " + this.getDateString());
        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            innerEnum.addJavaDocLine("/**");
            innerEnum.addJavaDocLine(" * This enum was generated by MyBatis Generator.");
            sb.append(" * This enum corresponds to the database table ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            innerEnum.addJavaDocLine(sb.toString());
            this.addJavadocTag(innerEnum, false);
            innerEnum.addJavaDocLine(" */");
        }
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String column;
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            field.addJavaDocLine("/**");
            column = " * " + introspectedColumn.getRemarks();
            field.addJavaDocLine(column);
            field.addJavaDocLine(" */");
        }

        if (this.isTkMapper) {
            if (field.isTransient()) {
                field.addAnnotation("@Transient");
            }

            for (IntrospectedColumn keyColumn : introspectedTable.getPrimaryKeyColumns()) {
                if (introspectedColumn == keyColumn) {
                    field.addAnnotation("@Id");
                    break;
                }
            }

            column = introspectedColumn.getActualColumnName();
            if (StringUtility.stringContainsSpace(column) || introspectedTable.getTableConfiguration().isAllColumnDelimitingEnabled()) {
                column = introspectedColumn.getContext().getBeginningDelimiter() + column + introspectedColumn.getContext().getEndingDelimiter();
            }

            if (!column.equals(introspectedColumn.getJavaProperty())) {
                field.addAnnotation("@Column(name = \"" + this.getDelimiterName(column) + "\")");
            } else if (!StringUtility.stringHasValue(this.beginningDelimiter) && !StringUtility.stringHasValue(this.endingDelimiter)) {
                if (this.forceAnnotation) {
                    field.addAnnotation("@Column(name = \"" + this.getDelimiterName(column) + "\")");
                }
            } else {
                field.addAnnotation("@Column(name = \"" + this.getDelimiterName(column) + "\")");
            }

            String swaggerAnnotation;
            String remarks;
            if (introspectedColumn.isIdentity()) {
                if ("JDBC".equals(introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement())) {
                    field.addAnnotation("@GeneratedValue(generator = \"JDBC\")");
                } else {
                    field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY)");
                }
            } else if (introspectedColumn.isSequenceColumn()) {
                remarks = introspectedTable.getFullyQualifiedTableNameAtRuntime();
                swaggerAnnotation = MessageFormat.format(introspectedTable.getTableConfiguration().getGeneratedKey().getRuntimeSqlStatement(), remarks, remarks.toUpperCase());
                field.addAnnotation("@GeneratedValue(strategy = GenerationType.IDENTITY, generator = \"" + swaggerAnnotation + "\")");
            }

            if (this.needsSwagger) {
                remarks = introspectedColumn.getRemarks();
                if (remarks == null) {
                    remarks = "";
                }

                swaggerAnnotation = "@ApiModelProperty(value = \"%s\" da )";
                field.addAnnotation("@ApiModelProperty(\"" + remarks.replaceAll("\r", "").replaceAll("\n", "") + "\")");
            }
        }

    }

    public String getDelimiterName(String name) {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(this.beginningDelimiter);
        nameBuilder.append(name);
        nameBuilder.append(this.endingDelimiter);
        return nameBuilder.toString();
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
    }

    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        method.addJavaDocLine("/**");
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" * 获取");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" *");
        }

        sb.setLength(0);
        sb.append(" * @return ");
        sb.append(introspectedColumn.getActualColumnName());
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" - ");
            sb.append(introspectedColumn.getRemarks());
        }

        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */");
    }

    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        method.addJavaDocLine("/**");
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" * 设置");
            sb.append(introspectedColumn.getRemarks());
            method.addJavaDocLine(sb.toString());
            method.addJavaDocLine(" *");
        }

        Parameter parm = (Parameter) method.getParameters().get(0);
        sb.setLength(0);
        sb.append(" * @param ");
        sb.append(parm.getName());
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(" ");
            sb.append(introspectedColumn.getRemarks());
        }

        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */");
    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (!this.suppressAllComments) {
            StringBuilder sb = new StringBuilder();
            innerClass.addJavaDocLine("/**");
            innerClass.addJavaDocLine(" * This class was generated by MyBatis Generator.");
            sb.append(" * This class corresponds to the database table ");
            sb.append(introspectedTable.getFullyQualifiedTable());
            innerClass.addJavaDocLine(sb.toString());
            this.addJavadocTag(innerClass, markAsDoNotDelete);
            innerClass.addJavaDocLine(" */");
        }
    }
}

