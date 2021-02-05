package com.lansoft.action;

import com.alibaba.fastjson.JSON;
import com.intellij.core.CoreModuleManager;
import com.intellij.credentialStore.OneTimeString;
import com.intellij.database.access.DatabaseCredentials;
import com.intellij.database.access.DbCredentialManager;
import com.intellij.database.autoconfig.DataSourceConfigUtil;
import com.intellij.database.dataSource.DatabaseArtifactManager;
import com.intellij.database.dataSource.LocalDataSource;
import com.intellij.database.dataSource.LocalDataSourceManager;
import com.intellij.database.model.DatabaseSystem;
import com.intellij.database.model.RawConnectionConfig;
import com.intellij.database.psi.*;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.lansoft.model.TableInfo;
import com.lansoft.dialog.MybatisGeneratorMainUI;
import com.lansoft.service.MybatisPlusGeneratorService;
import com.lansoft.service.impl.MybatisPlusGeneratorServiceImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 自定义功能
 * mybatisPlus自定生成
 *
 * @Author 郭伟东
 * @Date 2020/12/14  0:17
 */
public class MybatisPlusGeneratorAction extends AnAction {

    /**
     * DatabaseViewPopupMenu菜单中触发事件
     *
     * @param e
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        PsiElement[] psiElements = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
//        new CoreModuleManager();
//        final Module[] modules = ModuleManager.getInstance(Objects.requireNonNull(e.getProject())).getModules();
//        System.out.println("=====================================");
//        for (Module module : modules) {
//            System.out.println(module);
//            System.out.println(module.getModuleFilePath());
//            final String dirPath = ModuleUtil.getModuleDirPath(module);
//            System.out.println(dirPath);
//DatabaseTools
//        }
//        final DatabaseArtifactManager artifactManager = DatabaseArtifactManager.getInstance();
        /*final LocalDataSourceManager dataSourceManager = LocalDataSourceManager.getInstance(e.getProject());
        final List<LocalDataSource> dataSources = dataSourceManager.getDataSources();
        final DatabaseCredentials databaseCredentials = DatabaseCredentials.getInstance();
        for (LocalDataSource source : dataSources) {
            System.out.println(source.getDatabaseProductName());
            System.out.println(source.getDriverClass());
            System.out.println(source.getSourceName());
            System.out.println(source.getUsername());
            System.out.println(source.getPasswordStorage());
            final OneTimeString password = databaseCredentials.getPassword(source);
            System.out.println("+++++++++++++++++++++==================+++++++++++++++++");
            System.out.println(password);
        }*/
        /*if (psiElements != null) {
            for (PsiElement psiElement : psiElements) {
                //DbElementImpl
                final DbTableImpl table = (DbTableImpl) psiElement;
                final DbDataSourceImpl dataSource = table.getDataSource();
//                System.out.println(dataSource.getName());
                final LocalDataSource delegate = (LocalDataSource)(dataSource.getDelegate());
                System.out.println(delegate.getUrl());
                System.out.println(delegate.getUsername());
                final OneTimeString password = databaseCredentials.getPassword(delegate);
                System.out.println(password);
            }
        }*/
//        DataSourceConfigUtil.buildDataSource();
//        final DatabaseCredentials databaseCredentials = DatabaseCredentials.getInstance();
//        databaseCredentials.getPassword();
//        final PropertiesComponent instance = PropertiesComponent.getInstance();
//        final String[] values = instance.getValues("user-pass");
//        System.out.println(JSON.toJSONString(values));
//        MybatisPlusGeneratorService generatorService = new MybatisPlusGeneratorServiceImpl(psiElements);
//        generatorService.generatorCode(Objects.requireNonNull(e.getProject()).getBasePath());
        //显示通知窗口
//        Messages.showMessageDialog("mybatisPlus生成完成", "测试通知效果", Messages.getInformationIcon());

//        Messages.showMessageDialog(jsonString, "测试通知效果", Messages.getInformationIcon());
//        JBPopupFactory instance = JBPopupFactory.getInstance();
//        instance.createActionGroupPopup()

//        DialogWrapper dialog = new MybatisPlusGeneratorDialog();
//        dialog.show();

//        SettingDialog setting = UIFactory.createSettingDialog(e.getDataContext());
//        setting.setVisible(true);

        DialogWrapper dialog = new MybatisGeneratorMainUI(psiElements,e.getProject());
        dialog.show();

    }
}
