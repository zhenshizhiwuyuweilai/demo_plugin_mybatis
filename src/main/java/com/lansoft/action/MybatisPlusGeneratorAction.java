package com.lansoft.action;

import com.alibaba.fastjson.JSON;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiElement;
import com.lansoft.model.TableInfo;
import com.lansoft.dialog.MybatisGeneratorMainUI;

import java.util.ArrayList;
import java.util.List;

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
        List<String> list = new ArrayList<>();
        for (PsiElement psiElement : psiElements) {
            TableInfo tableInfo = new TableInfo((DbTable) psiElement);
            String tableName = tableInfo.getTableName();
            System.out.println(tableName);
            list.add(tableInfo.toString());
        }
        String jsonString = JSON.toJSONString(list);
        System.out.println(jsonString);
        //显示通知窗口
//        Messages.showMessageDialog(jsonString, "测试通知效果", Messages.getInformationIcon());
//        JBPopupFactory instance = JBPopupFactory.getInstance();
//        instance.createActionGroupPopup()

//        DialogWrapper dialog = new MybatisPlusGeneratorDialog();
//        dialog.show();

//        SettingDialog setting = UIFactory.createSettingDialog(e.getDataContext());
//        setting.setVisible(true);
        DialogWrapper dialog = new MybatisGeneratorMainUI();
        dialog.show();

    }
}
