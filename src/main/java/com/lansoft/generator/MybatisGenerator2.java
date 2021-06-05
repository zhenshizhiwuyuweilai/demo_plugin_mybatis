package com.lansoft.generator;

import com.lansoft.model.MybatisConfig;
import com.lansoft.utils.FileUtil;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis逆向生成代码
 *
 * @Author 郭伟东
 * @Date 2021/1/24  14:21
 */
public class MybatisGenerator2 {

    /**
     *
     * @param mybatisConfig
     */
    public static void build(MybatisConfig mybatisConfig) {
        final MybatisGenerator2 generator = new MybatisGenerator2();
        generator.generatorCode(mybatisConfig);
    }
//
//    public MybatisGenerator(MybatisConfig mybatisConfig) {
//        generatorCode(mybatisConfig);
//    }

    //idea中持久化配置数据方法
//String configJson = PropertiesComponent.getInstance().getValue(GEN_CONFIG);
    public  void generatorCode(MybatisConfig mybatisConfig) {
        try {
            List<String> warnings = new ArrayList<String>();
            boolean overwrite = true;
            Configuration config = new Configuration();
            final Context context = new Context(ModelType.FLAT);

            //配置数据源
            jdbcConfig(context);
            modelConfig(context);
            mapperConfig(context);
            xmlConfig(context);
            pluginConfig(context);

            context.addProperty("beginningDelimiter", "`");
            context.addProperty("endingDelimiter", "`");
            context.addProperty("autoDelimitKeywords", "true");
            context.addProperty("javaFileEncoding", "UTF-8");

            final JavaTypeResolverConfiguration typeResolver = new JavaTypeResolverConfiguration();
            //默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，为 true时把JDBC DECIMAL 和
            //NUMERIC 类型解析为java.math.BigDecimal
            typeResolver.addProperty("forceBigDecimals", "false");
            context.setJavaTypeResolverConfiguration(typeResolver);
            //PluginConfiguration
           /* <commentGenerator>
            <property name="suppressDate" value="true"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>*/
            final CommentGeneratorConfiguration commentConfiguration = new CommentGeneratorConfiguration();
            commentConfiguration.setConfigurationType("com.lansoft.generator.custom.MyCustomCommentGenerator");
            commentConfiguration.addProperty("suppressDate", "false");
            commentConfiguration.addProperty("suppressAllComments", "false");
            commentConfiguration.addProperty("addRemarkComments", "true");
            commentConfiguration.addProperty("author", "作者");

            context.setCommentGeneratorConfiguration(commentConfiguration);

            final TableConfiguration configuration = new TableConfiguration(context);
            configuration.setTableName("SPECIAL_DIC_BUSIID");
//            configuration.setInsertStatementEnabled(false);
//            configuration.setSelectByPrimaryKeyStatementEnabled(false);
//            configuration.setSelectByExampleStatementEnabled(false);
//            configuration.setUpdateByPrimaryKeyStatementEnabled(false);
//            configuration.setDeleteByPrimaryKeyStatementEnabled(false);
//            configuration.setDeleteByExampleStatementEnabled(false);
//            configuration.setCountByExampleStatementEnabled(false);
//            configuration.setUpdateByExampleStatementEnabled(false);
            context.addTableConfiguration(configuration);

            PluginConfiguration pluginConfiguration = new PluginConfiguration();

            //MyBatis3Simple
            context.setTargetRuntime("tk.mybatis.mapper.generator.TkMyBatis3SimpleImpl");
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

    /**
     * 配置数据源
     *
     * @param context mybatis generator的上下文
     */
    public  void jdbcConfig(Context context) {
        //配置数据源
        final JDBCConnectionConfiguration jdbcConfig = new JDBCConnectionConfiguration();
        jdbcConfig.setDriverClass("oracle.jdbc.driver.OracleDriver");
        jdbcConfig.setConnectionURL("jdbc:oracle:thin:@59.110.229.141:1521/iomdb");
        jdbcConfig.setUserId("wfadmin");
        jdbcConfig.setPassword("Oracle_123");
        //解决mysql驱动升级到8.0后不生成指定数据库代码的问题
        jdbcConfig.addProperty("nullCatalogMeansCurrent", "true");
        //设置oracle和mysql
        //支持oracle获取注释
        jdbcConfig.addProperty("remarksReporting", "true");
        //支持mysql获取注释
        jdbcConfig.addProperty("useInformationSchema", "true");
        context.setJdbcConnectionConfiguration(jdbcConfig);
    }

    /**
     * 配置model
     *
     * @param context mybatis generator的上下文
     */
    public  void modelConfig(Context context) {
        final JavaModelGeneratorConfiguration configuration = new JavaModelGeneratorConfiguration();
        final String src = "D:/project/test_project/empty/src/main/java";
        configuration.setTargetProject(src);
        configuration.setTargetPackage("com.lansoft.entity4");
        FileUtil.mkdirs(src);
        context.setJavaModelGeneratorConfiguration(configuration);
    }

    /**
     * 配置mapper接口
     *
     * @param context mybatis generator的上下文
     */
    public  void mapperConfig(Context context) {
        final JavaClientGeneratorConfiguration configuration = new JavaClientGeneratorConfiguration();
        final String src = "D:/project/test_project/empty/src/main/java";
        configuration.setTargetProject(src);
        configuration.setTargetPackage("com.lansoft.mapper4");
        configuration.setConfigurationType("XMLMAPPER");
        FileUtil.mkdirs(src);
        context.setJavaClientGeneratorConfiguration(configuration);
    }

    /**
     * 配置mapperXml
     *
     * @param context mybatis generator的上下文
     */
    public  void xmlConfig(Context context) {
        final SqlMapGeneratorConfiguration configuration = new SqlMapGeneratorConfiguration();
        final String src = "D:/project/test_project/empty/src/main/resources";
        configuration.setTargetProject(src);
        configuration.setTargetPackage("mybatis.mapper4");
        FileUtil.mkdirs(src);
        context.setSqlMapGeneratorConfiguration(configuration);
    }

    private  void pluginConfig(Context context) {
        PluginConfiguration pluginConfiguration = new PluginConfiguration();

        context.addPluginConfiguration(pluginConfiguration);

//        pluginConfiguration.setConfigurationType("org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin");
//        pluginConfiguration.setConfigurationType("tk.mybatis.mapper.generator.MapperPlugin");
        pluginConfiguration.setConfigurationType("com.lansoft.generator.custom.MapperCustomPlugin");
        //设置是否使用通用mapper
        pluginConfiguration.addProperty("isTkMapper", "false");
        //设置数据库区分大小写
        pluginConfiguration.addProperty("caseSensitive", "true");
//        pluginConfiguration.addProperty("useMapperCommentGenerator","true");
//        pluginConfiguration.addProperty("lombok", "Data");
        //创建静态长量
//        pluginConfiguration.addProperty("generateColumnConsts","true");
        pluginConfiguration.addProperty("mappers", "tk.mybatis.mapper.common.Mapper");

    }



}
