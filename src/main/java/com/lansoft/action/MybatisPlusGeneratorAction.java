package com.lansoft.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.lansoft.dialog.MybatisGeneratorMainUI;

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
