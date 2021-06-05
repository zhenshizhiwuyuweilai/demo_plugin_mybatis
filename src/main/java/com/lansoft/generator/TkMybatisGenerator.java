package com.lansoft.generator;

import com.intellij.credentialStore.OneTimeString;
import com.intellij.database.access.DatabaseCredentials;
import com.intellij.database.dataSource.LocalDataSource;
import com.intellij.database.psi.DbDataSourceImpl;
import com.intellij.database.psi.DbTableImpl;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiElement;
import com.lansoft.model.MybatisConfig;
import com.lansoft.utils.CaseUtils;
import com.lansoft.utils.FileUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.CommentGeneratorConfiguration;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.JavaTypeResolverConfiguration;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PluginConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * @author guowd
 * @date 2021/6/6  2:28
 */
public class TkMybatisGenerator implements MybatisGenerator {
    private boolean isTkMapper;

    public static void build(MybatisConfig mybatisConfig, boolean isTkMapper) {
        TkMybatisGenerator generator = new TkMybatisGenerator(isTkMapper);
        generator.generatorCode(mybatisConfig);
    }

    public TkMybatisGenerator(boolean isTkMapper) {
        this.isTkMapper = isTkMapper;
    }

    @Override
    public void generatorCode(MybatisConfig mybatisConfig) {
        try {
            Configuration config = new Configuration();
            Context context = new Context(ModelType.FLAT);
            this.jdbcConfig(context, mybatisConfig);
            this.modelConfig(context, mybatisConfig);
            this.mapperConfig(context, mybatisConfig);
            this.xmlConfig(context, mybatisConfig);
            this.pluginConfig(context, mybatisConfig);
            this.commentConfig(context, mybatisConfig);
            context.addProperty("beginningDelimiter", "`");
            context.addProperty("endingDelimiter", "`");
            context.addProperty("autoDelimitKeywords", "true");
            context.addProperty("javaFileEncoding", "UTF-8");
            JavaTypeResolverConfiguration typeResolver = new JavaTypeResolverConfiguration();
            typeResolver.addProperty("forceBigDecimals", "false");
            context.setJavaTypeResolverConfiguration(typeResolver);
            String namePrefix = mybatisConfig.getTableNamePrefix();
            if (namePrefix != null && namePrefix.length() > 0 && '^' != namePrefix.charAt(0)) {
                namePrefix = "^" + namePrefix;
            }

            List<String> tableNameList = mybatisConfig.getTableNameList();

            TableConfiguration configuration;
            for(Iterator var7 = tableNameList.iterator(); var7.hasNext(); context.addTableConfiguration(configuration)) {
                String tableName = (String)var7.next();
                configuration = new TableConfiguration(context);
                configuration.setTableName(tableName);
                if (namePrefix != null) {
                    Pattern pattern = Pattern.compile(namePrefix);
                    Matcher matcher = pattern.matcher(tableName);
                    String domainObjectName = matcher.replaceFirst("");
                    domainObjectName = CaseUtils.toCamelCase(domainObjectName, true, new char[]{'_'});
                    configuration.setDomainObjectName(domainObjectName);
                }
            }

            context.setTargetRuntime("tk.mybatis.mapper.generator.TkMyBatis3SimpleImpl");
            context.setId("DBTables");
            config.addContext(context);
            DefaultShellCallback callback = new DefaultShellCallback(true);
            List<String> warnings = new ArrayList();
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(null);
            StringBuilder builder = new StringBuilder();
            warnings.forEach((e) -> {
                builder.append(e);
                builder.append("\n");
            });
            if (warnings.size() > 0) {
                Messages.showInfoMessage(builder.toString(), "Mybatis插件warning信息");
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        }

    }

    private void commentConfig(Context context, MybatisConfig mybatisConfig) {
        CommentGeneratorConfiguration commentConfiguration = new CommentGeneratorConfiguration();
        commentConfiguration.setConfigurationType("com.lansoft.generator.custom.MyCustomCommentGenerator");
        commentConfiguration.addProperty("suppressDate", "false");
        commentConfiguration.addProperty("suppressAllComments", "false");
        commentConfiguration.addProperty("addRemarkComments", "true");
        commentConfiguration.addProperty("author", mybatisConfig.getAuthor());
        context.setCommentGeneratorConfiguration(commentConfiguration);
    }

    private void jdbcConfig(Context context, MybatisConfig mybatisConfig) {
        JDBCConnectionConfiguration jdbcConfig = new JDBCConnectionConfiguration();
        DbTableImpl table = (DbTableImpl)mybatisConfig.getPsiElements()[0];
        DbDataSourceImpl dataSource = table.getDataSource();
        LocalDataSource delegate = (LocalDataSource)((LocalDataSource)dataSource.getDelegate());
        jdbcConfig.setDriverClass(delegate.getDriverClass());
        jdbcConfig.setConnectionURL(delegate.getUrl());
        jdbcConfig.setUserId(delegate.getUsername());
        DatabaseCredentials databaseCredentials = DatabaseCredentials.getInstance();
        OneTimeString password = databaseCredentials.getPassword(delegate);
        if (password == null) {
            Messages.showMessageDialog("获取数据库密码失败！请配置密码！", "Custom-Mybatis-Generator：异常信息提示", Messages.getInformationIcon());
            //弹出密码输入框，输入密码，待完

            password = new OneTimeString("");
        }

        jdbcConfig.setPassword(password.toString());
        jdbcConfig.addProperty("nullCatalogMeansCurrent", "true");
        jdbcConfig.addProperty("remarksReporting", "true");
        jdbcConfig.addProperty("useInformationSchema", "true");
        context.setJdbcConnectionConfiguration(jdbcConfig);
    }

    public Map<LocalDataSource, String> getDataSourceConfig(PsiElement[] psiElements) {
        DatabaseCredentials databaseCredentials = DatabaseCredentials.getInstance();
        DbTableImpl table = (DbTableImpl)psiElements[0];
        DbDataSourceImpl dataSource = table.getDataSource();
        LocalDataSource delegate = (LocalDataSource)((LocalDataSource)dataSource.getDelegate());
        OneTimeString password = databaseCredentials.getPassword(delegate);
        Map<LocalDataSource, String> map = new HashMap(5);
        if (password != null) {
            map.put(delegate, password.toString());
        } else {
            map.put(delegate, null);
        }

        return map;
    }

    private void modelConfig(Context context, MybatisConfig mybatisConfig) {
        JavaModelGeneratorConfiguration configuration = new JavaModelGeneratorConfiguration();
        String src = mybatisConfig.getModelFolder();
        configuration.setTargetProject(src);
        configuration.setTargetPackage(mybatisConfig.getModelPackage());
        FileUtil.mkdirs(src);
        context.setJavaModelGeneratorConfiguration(configuration);
    }

    private void mapperConfig(Context context, MybatisConfig mybatisConfig) {
        JavaClientGeneratorConfiguration configuration = new JavaClientGeneratorConfiguration();
        String src = mybatisConfig.getMapperFolder();
        configuration.setTargetProject(src);
        configuration.setTargetPackage(mybatisConfig.getMapperPackage());
        configuration.setConfigurationType("XMLMAPPER");
        FileUtil.mkdirs(src);
        context.setJavaClientGeneratorConfiguration(configuration);
    }

    private void xmlConfig(Context context, MybatisConfig mybatisConfig) {
        SqlMapGeneratorConfiguration configuration = new SqlMapGeneratorConfiguration();
        String src = mybatisConfig.getXmlFolder();
        configuration.setTargetProject(src);
        configuration.setTargetPackage(mybatisConfig.getXmlPackage());
        FileUtil.mkdirs(src);
        context.setSqlMapGeneratorConfiguration(configuration);
    }

    private void pluginConfig(Context context, MybatisConfig mybatisConfig) {
        PluginConfiguration pluginConfiguration = new PluginConfiguration();
        context.addPluginConfiguration(pluginConfiguration);
        pluginConfiguration.setConfigurationType("com.lansoft.generator.custom.MapperCustomPlugin");
        pluginConfiguration.addProperty("isTkMapper", String.valueOf(this.isTkMapper));
        pluginConfiguration.addProperty("caseSensitive", String.valueOf(mybatisConfig.isUpperCamelCase()));
        pluginConfiguration.addProperty("useMapperCommentGenerator", "true");
        if (mybatisConfig.isLombok()) {
            pluginConfiguration.addProperty("lombok", "Data");
        }

        pluginConfiguration.addProperty("mappers", "tk.mybatis.mapper.common.Mapper");
    }
}
