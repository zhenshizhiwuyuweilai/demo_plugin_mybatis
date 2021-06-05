package com.lansoft.generator;

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

import java.util.*;

/**
 * @author guowd
 * @date 2021/6/6  2:35
 */
public class MybatisPlusGenerator implements MybatisGenerator {
    public MybatisPlusGenerator() {
    }

    public static void build(MybatisConfig mybatisConfig) {
        MybatisPlusGenerator generator = new MybatisPlusGenerator();
        generator.generatorCode(mybatisConfig);
    }

    @Override
    public void generatorCode(MybatisConfig mybatisConfig) {
        AutoGenerator mpg = new AutoGenerator();
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(mybatisConfig.getProject().getBasePath() + "/src/main/java");
        gc.setAuthor(mybatisConfig.getAuthor());
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);
        gc.setFileOverride(true);
        mpg.setGlobalConfig(gc);
        PackageConfig pc = new PackageConfig();
        pc.setParent(null);
        pc.setEntity(mybatisConfig.getModelPackage());
        pc.setMapper(mybatisConfig.getMapperPackage());
        mpg.setPackageInfo(pc);
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };
        cfg.setFileOutConfigList(this.fileOutConfig(mybatisConfig));
        mpg.setCfg(cfg);
        TemplateConfig templateConfig = new TemplateConfig();
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
        StrategyConfig strategy = new StrategyConfig();
        if (mybatisConfig.isUpperCamelCase()) {
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);
            strategy.setControllerMappingHyphenStyle(true);
        }

        strategy.setEntityLombokModel(mybatisConfig.isLombok());
        strategy.setRestControllerStyle(mybatisConfig.isRestController());
        strategy.setInclude((String[])mybatisConfig.getTableNameList().toArray(new String[0]));
        strategy.setTablePrefix(new String[]{mybatisConfig.getTableNamePrefix()});
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        Map<LocalDataSource, String> dataSourceMap = this.getDataSourceConfig(mybatisConfig.getPsiElements());
        Iterator var9 = dataSourceMap.keySet().iterator();

        while(var9.hasNext()) {
            LocalDataSource dataSource = (LocalDataSource)var9.next();
            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setDriverName(dataSource.getDriverClass());
            dsc.setUrl(dataSource.getUrl());
            dsc.setUsername(dataSource.getUsername());
            String password = dataSourceMap.get(dataSource);
            if (password == null) {
                Messages.showMessageDialog("获取数据库密码失败！请配置密码！", "Custom-Mybatis-Generator：异常信息提示", Messages.getInformationIcon());
                return;
            }

            dsc.setPassword(password);
            mpg.setDataSource(dsc);
            mpg.execute();
        }

    }

    public Map<LocalDataSource, String> getDataSourceConfig(PsiElement[] psiElements) {
        Map<LocalDataSource, String> map = new HashMap(5);
        if (psiElements != null) {
            DatabaseCredentials databaseCredentials = DatabaseCredentials.getInstance();
            PsiElement[] var4 = psiElements;
            int var5 = psiElements.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                PsiElement psiElement = var4[var6];
                DbTableImpl table = (DbTableImpl)psiElement;
                DbDataSourceImpl dataSource = table.getDataSource();
                LocalDataSource delegate = (LocalDataSource)((LocalDataSource)dataSource.getDelegate());
                OneTimeString password = databaseCredentials.getPassword(delegate);
                if (password != null) {
                    if (!map.containsKey(delegate)) {
                        map.put(delegate, password.toString());
                    }
                } else if (!map.containsKey(delegate)) {
                    map.put(delegate, null);
                }
            }
        }

        return map;
    }

    public List<FileOutConfig> fileOutConfig(final MybatisConfig mybatisConfig) {
        String templateModelPath = "/templates/entity.java.ftl";
        String templateMapperPath = "/templates/mapper.java.ftl";
        String templateXmlPath = "/templates/mapper.xml.ftl";
        List<FileOutConfig> focList = new ArrayList();
        focList.add(new FileOutConfig(templateModelPath) {
            public String outputFile(TableInfo tableInfo) {
                StringBuilder src = new StringBuilder("/src/main/java/");
                if (StringUtils.isNotBlank(mybatisConfig.getModelFolder())) {
                    src = new StringBuilder(mybatisConfig.getModelFolder());
                    if (StringUtils.isNotBlank(mybatisConfig.getModelPackage())) {
                        String[] split = mybatisConfig.getModelPackage().split("\\.");
                        String[] var4 = split;
                        int var5 = split.length;

                        for(int var6 = 0; var6 < var5; ++var6) {
                            String s = var4[var6];
                            src.append("/").append(s);
                        }
                    } else {
                        src.append("/com/lansoft/entity");
                    }
                }

                String mapperPath = src + "/";
                mapperPath = mapperPath + tableInfo.getEntityName() + ".java";
                return mapperPath;
            }
        });
        focList.add(new FileOutConfig(templateMapperPath) {
            public String outputFile(TableInfo tableInfo) {
                StringBuilder src = new StringBuilder("/src/main/java/");
                if (StringUtils.isNotBlank(mybatisConfig.getMapperFolder())) {
                    src = new StringBuilder(mybatisConfig.getMapperFolder());
                    if (StringUtils.isNotBlank(mybatisConfig.getMapperPackage())) {
                        String[] split = mybatisConfig.getMapperPackage().split("\\.");
                        String[] var4 = split;
                        int var5 = split.length;

                        for(int var6 = 0; var6 < var5; ++var6) {
                            String s = var4[var6];
                            src.append("/").append(s);
                        }
                    } else {
                        src.append("/com/lansoft/mapper");
                    }
                }

                String mapperPath = src + "/";
                mapperPath = mapperPath + tableInfo.getEntityName() + "Mapper" + ".java";
                return mapperPath;
            }
        });
        focList.add(new FileOutConfig(templateXmlPath) {
            public String outputFile(TableInfo tableInfo) {
                StringBuilder mapperSrc = new StringBuilder("/src/main/resources/mybatis/mapper/");
                if (StringUtils.isNotBlank(mybatisConfig.getXmlFolder())) {
                    mapperSrc = new StringBuilder(mybatisConfig.getXmlFolder());
                    if (StringUtils.isNotBlank(mybatisConfig.getXmlPackage())) {
                        String[] split = mybatisConfig.getXmlPackage().split("\\.");
                        String[] var4 = split;
                        int var5 = split.length;

                        for(int var6 = 0; var6 < var5; ++var6) {
                            String s = var4[var6];
                            mapperSrc.append("/").append(s);
                        }
                    } else {
                        mapperSrc.append("/mybatis");
                    }
                }

                String mapperPath = mapperSrc.toString() + "/";
                mapperPath = mapperPath + tableInfo.getEntityName() + "Mapper" + ".xml";
                return mapperPath;
            }
        });
        return focList;
    }
}
