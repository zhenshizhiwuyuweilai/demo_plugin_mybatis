package com.lansoft.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.intellij.database.psi.DbTable;
import com.intellij.psi.PsiElement;
import com.intellij.spring.model.utils.SpringBeanUtils;
import com.intellij.spring.model.xml.util.SpringUtilElement;
import com.lansoft.model.TableInfo;
import com.lansoft.service.MybatisPlusGeneratorService;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis逆向生成代码
 *
 * @Author 郭伟东
 * @Date 2021/1/24  14:21
 */
public class MybatisPlusGeneratorServiceImpl implements MybatisPlusGeneratorService {

    private PsiElement[] psiElements;

    public MybatisPlusGeneratorServiceImpl(PsiElement[] psiElements) {
        this.psiElements = psiElements;
    }

    public void generatorCode() {
        ArrayList<String> list = new ArrayList<>();
        for (PsiElement psiElement : psiElements) {
//            TableInfo tableInfo = new TableInfo((DbTable) psiElement);
//            String tableName = tableInfo.getTableName();
            final DbTable dbTable = (DbTable) psiElement;
            list.add(dbTable.getName());
        }
        String jsonString = JSON.toJSONString(list);
        System.out.println(jsonString);

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        mpg.setGlobalConfig(gc);
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("郭伟东");
        gc.setOpen(false);
        gc.setBaseResultMap(true);
        gc.setBaseColumnList(true);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:oracle:thin:@59.110.229.141:1521/iomdb");
        //dsc.setSchemaName("public");
        dsc.setDriverName("oracle.jdbc.driver.OracleDriver");
        dsc.setUsername("wfadmin");
        dsc.setPassword("Oracle_123");
        mpg.setDataSource(dsc);
        // 包配置
        PackageConfig pc = new PackageConfig();
        //pc.setModuleName(scanner("模块名"));
        pc.setParent("com.baomidou");
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(com.baomidou.mybatisplus.generator.config.po.TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                String mapperPath = projectPath + "/src/main/resources/mapper/";
                if (StringUtils.isNotBlank(pc.getModuleName())) {
                    mapperPath += pc.getModuleName() + "/";
                }
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
        // templateConfig.setService();
        // templateConfig.setController();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//            strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);

        // 公共父类
        //strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        strategy.setSuperEntityColumns("id");
        //设置需要生成代码的表名（多个）
        strategy.setInclude(list.toArray(new String[0]));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
