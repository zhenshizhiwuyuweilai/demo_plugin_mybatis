package com.lansoft.generator;

import com.alibaba.fastjson.JSON;
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
import com.intellij.database.psi.DbTable;
import com.intellij.database.psi.DbTableImpl;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.lansoft.model.MybatisConfig;
import com.lansoft.service.MybatisPlusGeneratorService;
import org.apache.commons.lang.StringUtils;

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
public class MybatisPlusGenerator {


    public static void generatorCode(MybatisConfig mybatisConfig) {

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(mybatisConfig.getProjectPath() + "/src/main/java");
        gc.setAuthor(mybatisConfig.getAuthor());
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:oracle:thin:@59.110.229.141:1521/iomdb");
//        //dsc.setSchemaName("public");
//        dsc.setDriverName("oracle.jdbc.driver.OracleDriver");
//        dsc.setUsername("wfadmin");
//        dsc.setPassword("Oracle_123");
//        mpg.setDataSource(dsc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
//        pc.setParent("com.baomidou");
        pc.setParent(null);
        pc.setEntity(mybatisConfig.getModelPackage());
        pc.setMapper(mybatisConfig.getMapperPackage());
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
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
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
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
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        if (!mybatisConfig.isRestController()) {
            templateConfig.setController(null);
        }
        if (!mybatisConfig.isService()) {
            templateConfig.setService(null);
        }
        if (!mybatisConfig.isServiceImpl()) {
            templateConfig.setServiceImpl(null);
        }
        templateConfig.setEntity(null);
        templateConfig.setMapper(null);
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        //是否使用驼峰规则
        if (mybatisConfig.isUpperCamelCase()) {
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);
            strategy.setControllerMappingHyphenStyle(true);
        }
//            strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(mybatisConfig.isLombok());
        strategy.setRestControllerStyle(mybatisConfig.isRestController());

        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
        //设置需要生成代码的表名（多个）
        strategy.setInclude(mybatisConfig.getTableNameList().toArray(new String[0]));
        strategy.setTablePrefix(mybatisConfig.getTableNamePrefix() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        final Map<LocalDataSource, String> dataSourceMap = getDataSourceConfig(mybatisConfig.getPsiElements());
        for (LocalDataSource dataSource : dataSourceMap.keySet()) {
            // 数据源配置
            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setUrl(dataSource.getUrl());

            dsc.setDriverName(dataSource.getDriverClass());
            dsc.setUsername(dataSource.getUsername());
            final String password = dataSourceMap.get(dataSource);
            if (password == null) {
                //弹出窗口设置密码（待完成！）
                Messages.showMessageDialog("获取数据库密码失败！", "Custom-Mybatis-Generator：异常信息提示", Messages.getInformationIcon());
                return;
            }
            dsc.setPassword(password);
            mpg.setDataSource(dsc);
            mpg.execute();
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
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
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
}
