package com.lansoft.generator.custom;

import com.intellij.openapi.util.text.StringUtil;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;
import tk.mybatis.mapper.generator.FalseMethodPlugin;
import tk.mybatis.mapper.generator.MapperCommentGenerator;

import java.util.*;

/**
 * mybatis 自定义生成插件
 *
 * @Author 郭伟东
 * @Date 2021/2/7  17:09
 */
public class MapperCustomPlugin extends FalseMethodPlugin {
    private final Set<String> mappers = new HashSet<String>();
    private boolean caseSensitive = false;
    private boolean useMapperCommentGenerator = true;
    //开始的分隔符，例如mysql为`，sqlserver为[
    private String beginningDelimiter = "";
    //结束的分隔符，例如mysql为`，sqlserver为]
    private String endingDelimiter = "";
    //数据库模式
    private String schema;
    //注释生成器
    private CommentGeneratorConfiguration commentCfg;
    //强制生成注解
    private boolean forceAnnotation;
    //是否使用通用mapper
    private boolean isTkMapper = false;

    //是否需要生成Data注解
    private boolean needsData = false;
    //是否需要生成Getter注解
    private boolean needsGetter = false;
    //是否需要生成Setter注解
    private boolean needsSetter = false;
    //是否需要生成ToString注解
    private boolean needsToString = false;
    //是否需要生成Accessors(chain = true)注解
    private boolean needsAccessors = false;
    //是否需要生成EqualsAndHashCode注解
    private boolean needsEqualsAndHashCode = false;
    //是否生成字段名常量
    private boolean generateColumnConsts = false;
    //是否生成默认的属性的静态方法
    private boolean generateDefaultInstanceMethod = false;
    //是否生成swagger注解,包括 @ApiModel和@ApiModelProperty
    private boolean needsSwagger = false;

    public String getDelimiterName(String name) {
        StringBuilder nameBuilder = new StringBuilder();
        if (StringUtility.stringHasValue(schema)) {
            nameBuilder.append(schema);
            nameBuilder.append(".");
        }
        nameBuilder.append(beginningDelimiter);
        nameBuilder.append(name);
        nameBuilder.append(endingDelimiter);
        return nameBuilder.toString();
    }

    /**
     * 生成的Mapper接口
     *
     * @param interfaze
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (isTkMapper) {
            //获取实体类
            FullyQualifiedJavaType entityType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
            //import接口
            for (String mapper : mappers) {
                interfaze.addImportedType(new FullyQualifiedJavaType(mapper));
                interfaze.addSuperInterface(new FullyQualifiedJavaType(mapper + "<" + entityType.getShortName() + ">"));
            }
            //import实体类
            interfaze.addImportedType(entityType);
        }
        return true;
    }

    /**
     * 处理实体类的包和@Table注解
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    private void processEntityClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        //引入JPA注解
        topLevelClass.addImportedType("javax.persistence.*");
        //lombok扩展开始
        //如果需要Data，引入包，代码增加注解
        if (this.needsData) {
            topLevelClass.addImportedType("lombok.Data");
            topLevelClass.addAnnotation("@Data");
        }
        //如果需要Getter，引入包，代码增加注解
        if (this.needsGetter) {
            topLevelClass.addImportedType("lombok.Getter");
            topLevelClass.addAnnotation("@Getter");
        }
        //如果需要Setter，引入包，代码增加注解
        if (this.needsSetter) {
            topLevelClass.addImportedType("lombok.Setter");
            topLevelClass.addAnnotation("@Setter");
        }
        //如果需要ToString，引入包，代码增加注解
        if (this.needsToString) {
            topLevelClass.addImportedType("lombok.ToString");
            topLevelClass.addAnnotation("@ToString");
        }
        //如果需要Getter，引入包，代码增加注解
        if (this.needsAccessors) {
            topLevelClass.addImportedType("lombok.experimental.Accessors");
            topLevelClass.addAnnotation("@Accessors(chain = true)");
        }
        //如果需要Getter，引入包，代码增加注解
        if (this.needsEqualsAndHashCode) {
            topLevelClass.addImportedType("lombok.EqualsAndHashCode");
            topLevelClass.addAnnotation("@EqualsAndHashCode");
        }
        //lombok扩展结束
        // region swagger扩展
        if (this.needsSwagger) {
            //导包
            topLevelClass.addImportedType("io.swagger.annotations.ApiModel");
            topLevelClass.addImportedType("io.swagger.annotations.ApiModelProperty");
            //增加注解(去除注释中的转换符)
            String remarks = introspectedTable.getRemarks();
            if (remarks == null) {
                remarks = "";
            }
            topLevelClass.addAnnotation("@ApiModel(\"" + remarks.replaceAll("\r", "").replaceAll("\n", "") + "\")");
        }
        // endregion swagger扩展
        String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        //如果包含空格，或者需要分隔符，需要完善
        if (StringUtility.stringContainsSpace(tableName)) {
            tableName = context.getBeginningDelimiter()
                    + tableName
                    + context.getEndingDelimiter();
        }
        //是否忽略大小写，对于区分大小写的数据库，会有用
        if (isTkMapper) {
            if (caseSensitive && !topLevelClass.getType().getShortName().equals(tableName)) {
                topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
            } else if (!topLevelClass.getType().getShortName().equalsIgnoreCase(tableName)) {
                topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
            } else if (StringUtility.stringHasValue(schema)
                    || StringUtility.stringHasValue(beginningDelimiter)
                    || StringUtility.stringHasValue(endingDelimiter)) {
                topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
            } else if (forceAnnotation) {
                topLevelClass.addAnnotation("@Table(name = \"" + getDelimiterName(tableName) + "\")");
            }
        }
        if (generateColumnConsts) {
            for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                Field field = new Field(introspectedColumn.getActualColumnName().toUpperCase(),new FullyQualifiedJavaType(String.class.getName()));
                field.setVisibility(JavaVisibility.PUBLIC);
                field.setStatic(true);
                field.setFinal(true);
                field.setInitializationString("\"" + introspectedColumn.getJavaProperty() + "\"");
                context.getCommentGenerator().addClassComment(topLevelClass, introspectedTable);
                topLevelClass.addField(field);
                //增加字段名常量,用于pageHelper
                Field columnField = new Field("DB_" + introspectedColumn.getActualColumnName().toUpperCase(),new FullyQualifiedJavaType(String.class.getName()));
                columnField.setVisibility(JavaVisibility.PUBLIC);
                columnField.setStatic(true);
                columnField.setFinal(true);
                columnField.setInitializationString("\"" + introspectedColumn.getActualColumnName() + "\"");
                topLevelClass.addField(columnField);
            }
        }
        if (generateDefaultInstanceMethod) {
            Method defaultMethod = new Method("defaultInstance");
            defaultMethod.setStatic(true);
            defaultMethod.setVisibility(JavaVisibility.PUBLIC);
            defaultMethod.setReturnType(topLevelClass.getType());
            defaultMethod.addBodyLine(String.format("%s instance = new %s();", topLevelClass.getType().getShortName(), topLevelClass.getType().getShortName()));
            for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
                String shortName = introspectedColumn.getFullyQualifiedJavaType().getShortName();
                List<String> supportType = Arrays.asList("Byte", "Short", "Character", "Integer", "Long", "Float", "Double", "String", "BigDecimal", "BigInteger");
                if (!supportType.contains(shortName)) {
                    continue;
                }
                if (introspectedColumn.getDefaultValue() != null) {
                    String defaultValue = introspectedColumn.getDefaultValue();
                    //去除前后'',如 '123456' -> 123456
                    if (defaultValue.startsWith("'") && defaultValue.endsWith("'")) {
                        if (defaultValue.length() == 2) {
                            defaultValue = "";
                        } else {
                            defaultValue = defaultValue.substring(1, defaultValue.length() - 1);
                        }
                    }
                    //暂不支持时间类型默认值识别,不同数据库表达式不同
                    if ("Boolean".equals(shortName)) {
                        if ("0".equals(defaultValue)) {
                            defaultValue = "false";
                        } else if ("1".equals(defaultValue)) {
                            defaultValue = "true";
                        }
                    }
                    //通过 new 方法转换
                    defaultMethod.addBodyLine(String.format("instance.%s = new %s(\"%s\");", introspectedColumn.getJavaProperty(), shortName, defaultValue));
                }

            }
            defaultMethod.addBodyLine("return instance;");
            topLevelClass.addMethod(defaultMethod);
        }
    }

    /**
     * 如果需要生成Getter注解，就不需要生成get相关代码了
     */
    @Override
    public boolean modelGetterMethodGenerated(Method method,
                                              TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable,
                                              ModelClassType modelClassType) {

        return !(this.needsData || this.needsGetter);
    }

    /**
     * 如果需要生成Setter注解，就不需要生成set相关代码了
     */
    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn,
                                              IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !(this.needsData || this.needsSetter);
    }

    /**
     * 生成基础实体类
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * 生成实体类注解KEY对象
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return true;
    }

    /**
     * 生成带BLOB字段的对象
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        processEntityClass(topLevelClass, introspectedTable);
        return false;
    }


    @Override
    public void setContext(Context context) {
        super.setContext(context);
        //设置默认的注释生成器
        useMapperCommentGenerator = !"FALSE".equalsIgnoreCase(context.getProperty("useMapperCommentGenerator"));
        if (useMapperCommentGenerator) {
            final String configurationType = context.getCommentGeneratorConfiguration().getConfigurationType();
            if (StringUtil.isEmpty(configurationType)) {
                commentCfg = new CommentGeneratorConfiguration();
                commentCfg.setConfigurationType(MapperCommentGenerator.class.getCanonicalName());
                context.setCommentGeneratorConfiguration(commentCfg);
            }else {
                commentCfg = context.getCommentGeneratorConfiguration();

            }
        }
        //支持oracle获取注释#114
        context.getJdbcConnectionConfiguration().addProperty("remarksReporting", "true");
        //支持mysql获取注释
        context.getJdbcConnectionConfiguration().addProperty("useInformationSchema", "true");
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String mappers = getProperty("mappers");
        if (StringUtility.stringHasValue(mappers)) {
            for (String mapper : mappers.split(",")) {
                this.mappers.add(mapper);
            }
        } else {
            if (isTkMapper) {
                throw new RuntimeException("Mapper插件缺少必要的mappers属性!");
            }
        }
        this.caseSensitive = Boolean.parseBoolean(this.properties.getProperty("caseSensitive"));
        this.forceAnnotation = getPropertyAsBoolean("forceAnnotation");
        this.beginningDelimiter = getProperty("beginningDelimiter", "");
        this.endingDelimiter = getProperty("endingDelimiter", "");
        this.schema = getProperty("schema");
        //lombok扩展
        String lombok = getProperty("lombok");
        if (lombok != null && !"".equals(lombok)) {
            this.needsData = lombok.contains("Data");
            //@Data 优先级高于 @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode
            this.needsGetter = !this.needsData && lombok.contains("Getter");
            this.needsSetter = !this.needsData && lombok.contains("Setter");
            this.needsToString = !this.needsData && lombok.contains("ToString");
            this.needsEqualsAndHashCode = !this.needsData && lombok.contains("EqualsAndHashCode");
            this.needsAccessors = lombok.contains("Accessors");
        }
        //swagger扩展
        String swagger = getProperty("swagger", "false");
        if ("true".equalsIgnoreCase(swagger)) {
            this.needsSwagger = true;
        }
        if (useMapperCommentGenerator) {
            commentCfg.addProperty("beginningDelimiter", this.beginningDelimiter);
            commentCfg.addProperty("endingDelimiter", this.endingDelimiter);
            String forceAnnotation = getProperty("forceAnnotation");
            if (StringUtility.stringHasValue(forceAnnotation)) {
                commentCfg.addProperty("forceAnnotation", forceAnnotation);
            }
            commentCfg.addProperty("needsSwagger", this.needsSwagger + "");
        }
        this.generateColumnConsts = getPropertyAsBoolean("generateColumnConsts");
        this.generateDefaultInstanceMethod = getPropertyAsBoolean("generateDefaultInstanceMethod");
        this.isTkMapper = getPropertyAsBoolean("isTkMapper");
        commentCfg.addProperty("isTkMapper", isTkMapper + "");
    }

    protected String getProperty(String key) {
        return this.properties.getProperty(key);
    }

    protected String getProperty(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    protected Boolean getPropertyAsBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientSelectAllMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean sqlMapSelectAllElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean providerGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean providerApplyWhereMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean providerInsertSelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

    @Override
    public boolean providerUpdateByPrimaryKeySelectiveMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return !isTkMapper;
    }

}
