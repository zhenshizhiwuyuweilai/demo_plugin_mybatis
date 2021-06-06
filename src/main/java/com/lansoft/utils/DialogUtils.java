package com.lansoft.utils;

import com.intellij.credentialStore.OneTimeString;
import com.intellij.database.access.DatabaseCredentials;
import com.intellij.database.dataSource.LocalDataSource;
import com.intellij.openapi.ui.Messages;

/**
 * @author guowd
 * @date 2021/6/6  18:09
 */
public class DialogUtils {
    /**
     * @param dataSource 数据源
     * @return 不为空数据库密码
     */
    public static OneTimeString retypePassword(LocalDataSource dataSource) {
        DatabaseCredentials dc = DatabaseCredentials.getInstance();
        OneTimeString password = dc.getPassword(dataSource);
        if (password == null) {
            int i = 0;
            String inputDialog;
            do {
                i++;
                inputDialog = Messages.showInputDialog("获取数据库密码失败,请输入数据库密码！", "Custom-Mybatis-Generator", Messages.getErrorIcon());
            } while ((inputDialog == null || inputDialog.length() == 0) && i <= 3);
            if (inputDialog != null) {
                //弹出密码输入框，输入密码
                password = new OneTimeString(inputDialog);
                dc.setPassword(dataSource, password);
            } else {
                password = new OneTimeString("");
            }
        }
        return password;
    }
}
