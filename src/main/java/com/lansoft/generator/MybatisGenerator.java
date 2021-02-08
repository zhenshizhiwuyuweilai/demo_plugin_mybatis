package com.lansoft.generator;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.intellij.credentialStore.OneTimeString;
import com.intellij.database.access.DatabaseCredentials;
import com.intellij.database.dataSource.LocalDataSource;
import com.intellij.database.psi.DbDataSourceImpl;
import com.intellij.database.psi.DbTableImpl;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.lansoft.model.MybatisConfig;
import org.apache.commons.lang.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mybatis逆向生成代码
 *
 * @Author 郭伟东
 * @Date 2021/1/24  14:21
 */
public class MybatisGenerator {

//String configJson = PropertiesComponent.getInstance().getValue(GEN_CONFIG);
    public static void generatorCode(MybatisConfig mybatisConfig) {
        try {
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            Configuration config = new Configuration();


            //   ... fill out the config object as appropriate...

            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);

        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取数据源配置信息
     * System.out.println(delegate.getUrl());
     * System.out.println(delegate.getUsername());
     * System.out.println(password);
     *
     * @param psiElements
     * @return
     */
    public static Map<LocalDataSource, String> getDataSourceConfig(PsiElement[] psiElements) {
        Map<LocalDataSource, String> map = new HashMap<>(5);
        if (psiElements != null) {
            final DatabaseCredentials databaseCredentials = DatabaseCredentials.getInstance();
            for (PsiElement psiElement : psiElements) {
                //DbElementImpl
                final DbTableImpl table = (DbTableImpl) psiElement;
                final DbDataSourceImpl dataSource = table.getDataSource();
                final LocalDataSource delegate = (LocalDataSource) (dataSource.getDelegate());
                OneTimeString password = databaseCredentials.getPassword(delegate);
                //当密码不为空时记录数据库密码
                if (password != null) {
                    if (!map.containsKey(delegate)) {
                        map.put(delegate, password.toString());
                    }
                } else {
                    if (!map.containsKey(delegate)) {
                        map.put(delegate, null);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 自定义文件输出配置
     *
     * @param mybatisConfig mybatis 配置
     * @return
     */
    public static List<FileOutConfig> fileOutConfig(MybatisConfig mybatisConfig) {
        // 如果模板引擎是 freemarker
        String templateModelPath = ConstVal.TEMPLATE_ENTITY_JAVA + ".ftl";
        String templateMapperPath = ConstVal.TEMPLATE_MAPPER + ".ftl";
        String templateXmlPath = ConstVal.TEMPLATE_XML + ".ftl";
//        String templateXmlPath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出（model）
        focList.add(new FileOutConfig(templateModelPath) {
            /**
             * 输出文件
             *
             * @param tableInfo
             */
            @Override
            public String outputFile(TableInfo tableInfo) {
                StringBuilder src = new StringBuilder("/src/main/java/");
                if (StringUtils.isNotBlank(mybatisConfig.getModelFolder())) {
                    src = new StringBuilder(mybatisConfig.getModelFolder());
                    if (StringUtils.isNotBlank(mybatisConfig.getModelPackage())) {
                        final String[] split = mybatisConfig.getModelPackage().split("\\.");
                        for (String s : split) {
                            src.append("/").append(s);
                        }
                    } else {
                        src.append("/com/lansoft/entity");
                    }
                }
                String mapperPath = mybatisConfig.getProjectPath() + src + "/";
                mapperPath += tableInfo.getEntityName() + StringPool.DOT_JAVA;
                return mapperPath;
            }
        });

        // 自定义配置会被优先输出（mapper）
        focList.add(new FileOutConfig(templateMapperPath) {
            /**
             * 输出文件
             *
             * @param tableInfo
             */
            @Override
            public String outputFile(TableInfo tableInfo) {
                StringBuilder src = new StringBuilder("/src/main/java/");
                if (StringUtils.isNotBlank(mybatisConfig.getMapperFolder())) {
                    src = new StringBuilder(mybatisConfig.getMapperFolder());
                    if (StringUtils.isNotBlank(mybatisConfig.getMapperPackage())) {
                        final String[] split = mybatisConfig.getMapperPackage().split("\\.");
                        for (String s : split) {
                            src.append("/").append(s);
                        }
                    } else {
                        src.append("/com/lansoft/mapper");
                    }
                }
                String mapperPath = mybatisConfig.getProjectPath() + src + "/";
                mapperPath += tableInfo.getEntityName() + "Mapper" + StringPool.DOT_JAVA;
                return mapperPath;
            }
        });

        // 自定义配置会被优先输出（mapper.xml）
        focList.add(new FileOutConfig(templateXmlPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                StringBuilder mapperSrc = new StringBuilder("/src/main/resources/mybatis/mapper/");
                if (StringUtils.isNotBlank(mybatisConfig.getXmlFolder())) {
                    mapperSrc = new StringBuilder(mybatisConfig.getXmlFolder());
                    if (StringUtils.isNotBlank(mybatisConfig.getXmlPackage())) {
                        final String[] split = mybatisConfig.getXmlPackage().split("\\.");
                        for (String s : split) {
                            mapperSrc.append("/").append(s);
                        }
                    } else {
                        mapperSrc.append("/mybatis");
                    }
                }
                String mapperPath = mybatisConfig.getProjectPath() + mapperSrc + "/";
             /*   if (StringUtils.isNotBlank(pc.getModuleName())) {
                    mapperPath += pc.getModuleName() + "/";
                }*/
                mapperPath += tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                return mapperPath;
            }
        });
        return focList;
    }

    @org.junit.Test
    public void test005() {
        try {
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            Configuration config = new Configuration();
            final Context context = new Context(ModelType.FLAT);
            //配置数据源
            this.jdbcConfig(context);
            this.modelConfig(context);
            this.mapperConfig(context);
            this.xmlConfig(context);
            this.pluginConfig(context);

            context.addProperty("beginningDelimiter", "");
            context.addProperty("endingDelimiter", "");

            //PluginConfiguration
//            commentConfig();


            final TableConfiguration configuration = new TableConfiguration(context);
            configuration.setTableName("INTER_RES_RECEIVE_MAIN");
            configuration.setInsertStatementEnabled(false);
            configuration.setSelectByPrimaryKeyStatementEnabled(false);
            configuration.setUpdateByPrimaryKeyStatementEnabled(false);
            configuration.setDeleteByPrimaryKeyStatementEnabled(false);
            context.addTableConfiguration(configuration);

            context.setTargetRuntime("MyBatis3Simple");
            context.setId("DBTables");
            config.addContext(context);
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            warnings.forEach(System.out::println);
            System.out.println("生成完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void commentConfig() {
        final CommentGeneratorConfiguration commentConfiguration = new CommentGeneratorConfiguration();
        commentConfiguration.addProperty("suppressDate","true");
        commentConfiguration.addProperty("suppressAllComments","true");
        //            context.setCommentGeneratorConfiguration(commentConfiguration);
    }

    /**
     * 配置数据源
     *
     * @param context mybatis generator的上下文
     */
    public void jdbcConfig(Context context) {
        //配置数据源
        final JDBCConnectionConfiguration jdbcConfig = new JDBCConnectionConfiguration();
        jdbcConfig.setDriverClass("oracle.jdbc.driver.OracleDriver");
        jdbcConfig.setConnectionURL("jdbc:oracle:thin:@59.110.229.141:1521/iomdb");
        jdbcConfig.setUserId("wfadmin");
        jdbcConfig.setPassword("Oracle_123");
        //设置oracle和mysql
        //支持oracle获取注释
        jdbcConfig.addProperty("remarksReporting","true");
        //支持mysql获取注释
        jdbcConfig.addProperty("useInformationSchema","true");
        context.setJdbcConnectionConfiguration(jdbcConfig);
    }

    /**
     * 配置model
     *
     * @param context mybatis generator的上下文
     */
    public void modelConfig(Context context) {
        final JavaModelGeneratorConfiguration configuration = new JavaModelGeneratorConfiguration();
        final String src = "D:/project/test_project/empty/src/main/java";
        configuration.setTargetProject(src);
        configuration.setTargetPackage("com.lansoft.entity4");
        mkdirs(src);
        context.setJavaModelGeneratorConfiguration(configuration);
    }

    /**
     * 配置mapper接口
     *
     * @param context mybatis generator的上下文
     */
    public void mapperConfig(Context context) {
        final JavaClientGeneratorConfiguration configuration = new JavaClientGeneratorConfiguration();
        final String src = "D:/project/test_project/empty/src/main/java";
        configuration.setTargetProject(src);
        configuration.setTargetPackage("com.lansoft.mapper4");
        configuration.setConfigurationType("XMLMAPPER");
        mkdirs(src);
        context.setJavaClientGeneratorConfiguration(configuration);
    }

    /**
     * 配置mapperXml
     *
     * @param context mybatis generator的上下文
     */
    public void xmlConfig(Context context) {
        final SqlMapGeneratorConfiguration configuration = new SqlMapGeneratorConfiguration();
        final String src = "D:/project/test_project/empty/src/main/resources";
        configuration.setTargetProject(src);
        configuration.setTargetPackage("mybatis.mapper4");
        mkdirs(src);
        context.setSqlMapGeneratorConfiguration(configuration);
    }

    private void pluginConfig(Context context) {
        PluginConfiguration pluginConfiguration = new PluginConfiguration();

        context.addPluginConfiguration(pluginConfiguration);

//        pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin");
        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        pluginConfiguration.addProperty("caseSensitive","true");
        pluginConfiguration.addProperty("lombok","Data");
        pluginConfiguration.addProperty("generateColumnConsts","true");
        pluginConfiguration.addProperty("mappers","tk.mybatis.mapper.common.Mapper");

    }

    public void mkdirs(String value){
        File dir = new File(value);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (result) {
                System.out.println(("创建目录： [" + value + "]"));
            }
        }
    }

}
