package com.lansoft.dialog;

import com.alibaba.fastjson.JSON;
import com.intellij.core.CoreModuleManager;
import com.intellij.database.psi.DbTable;
import com.intellij.ide.extensionResources.ExtensionsRootType;
import com.intellij.ide.scratch.ScratchFileService;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPackage;
import com.lansoft.constant.MybatisConstant;
import com.lansoft.generator.MybatisGenerator;
import com.lansoft.model.MybatisConfig;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * @Author 郭伟东
 * @Date 2020/12/14  22:17
 */
public class MybatisGeneratorMainUI extends DialogWrapper {
    private JLabel tablePrefix;
    private JLabel projectPathLabel;
    private JButton modelPButton;
    private JButton modelFButton;
    private JButton mapperPButton;
    private JButton mapperFButton;
    private JButton xmlPButton;
    private JButton xmlFPButton;

    private JPanel contentPane;
    private final PsiElement[] psiElements;
    private final Project project;
    /**
     * 选中的表名
     */
    private final ArrayList<String> tableNameList = new ArrayList<>();

    private JTextField tableNamePrefix;
    private JTextField author;
    private JComboBox<String> modelPComboBox;
    private JTextField modelFText;
    private JComboBox<String> mapperPComboBox;
    private JTextField mapperFText;
    private JComboBox<String> xmlPComboBox;
    private JTextField xmlFText;
    private JComboBox<String> moduleName;
    private JCheckBox isLombok;
    private JComboBox<String> lombokComboBox;
    private JCheckBox isRestController;
    private JCheckBox isServiceImpl;
    private JCheckBox isService;
    private JCheckBox isUpperCamelCase;
    private JList<String> selectedtableName;
    private JComboBox<String> pluginType;
    private JFormattedTextField formattedTextField1;
    private JButton saveConfig;
    private JButton resetConfig;


    public MybatisGeneratorMainUI(PsiElement[] psiElements, Project project) {
        super(true);
        this.psiElements = psiElements;

//        List<DbTable> dbTables = Stream.of(psiElements).filter(t -> t instanceof DbTable).map(t -> (DbTable) t).collect(Collectors.toList());
//        Map<String, DbTable> tableMapping = dbTables.stream().collect(Collectors.toMap(DasObject::getName, a -> a, (a, b) -> a));


        this.project = project;
        Module[] modules = CoreModuleManager.getInstance(Objects.requireNonNull(project)).getModules();
        for (Module module : modules) {
            this.moduleName.addItem(module.getName());
        }

        if (modules.length > 0) {
            this.moduleName.setSelectedIndex(0);
        }

        this.modelPButton.addActionListener((e) -> {
            this.modelPComboBox.setSelectedItem(this.packageChooser());
        });
        this.modelFButton.addActionListener((e) -> {
            this.modelFText.setText(this.folderChooser(this.modelFText.getText(), "/src/main/java"));
        });
        this.mapperPButton.addActionListener((e) -> {
            this.mapperPComboBox.setSelectedItem(this.packageChooser());
        });
        this.mapperFButton.addActionListener((e) -> {
            this.mapperFText.setText(this.folderChooser(this.mapperFText.getText(), "/src/main/java"));
        });
        this.xmlPButton.addActionListener((e) -> {
            this.xmlPComboBox.setSelectedItem(this.resourcePackageChooser());
        });
        this.xmlFPButton.addActionListener((e) -> {
            this.xmlFText.setText(this.folderChooser(this.xmlFText.getText(), "/src/main/resources"));
        });
        this.resetConfig.addActionListener(e -> {
            configText();
        });

        readTableName(psiElements);
        //初始化数据（回显数据）
        initializeData();
        setTitle("Mybatis 逆向工程");
        setSize(800, 800);
        init();
    }

    /**
     * 初始化数据
     */
    public void initializeData() {
        //回显选中的表名到左侧的列表中
        listShow(psiElements);
        //初始化默认
        String configJson = PropertiesComponent.getInstance().getValue("CustomMybatisConfigData");
        if (StringUtil.isNotEmpty(configJson)) {
            try {
                MybatisConfig config = JSON.parseObject(configJson, MybatisConfig.class);
                this.moduleName.setSelectedItem(config.getModuleName());
                this.modelPComboBox.setSelectedItem(config.getModelPackage());
                this.modelFText.setText(config.getModelFolder());
                this.mapperPComboBox.setSelectedItem(config.getMapperPackage());
                this.mapperFText.setText(config.getMapperFolder());
                this.xmlPComboBox.setSelectedItem(config.getXmlPackage());
                this.xmlFText.setText(config.getXmlFolder());
                this.isLombok.setSelected(config.isLombok());
                this.isUpperCamelCase.setSelected(config.isUpperCamelCase());
                MybatisConstant.PLUGIN_TYPE.forEach((k, v) -> {
                    this.pluginType.addItem(k);
                });

                this.pluginType.setSelectedItem(config.getPluginType());
                this.author.setText(config.getAuthor());
                this.tableNamePrefix.setText(config.getTableNamePrefix());
            } catch (Exception var5) {
                var5.printStackTrace();
                Messages.showErrorDialog("读取历史数据解析失败！！！", "Mybatis逆向工程插件异常信息");
                this.configText();
            }
        } else {
            this.configText();
        }
    }

    private void saveConfig(ActionEvent actionEvent) {
        String[] dataLists = PropertiesComponent.getInstance().getValues("CustomMybatisConfigDataList");
        if (dataLists == null || dataLists.length == 0) {
            dataLists = new String[]{};
        }
        MybatisConfig mybatisConfig = new MybatisConfig(this);

//        String[]
//        PropertiesComponent.getInstance().setValues("CustomMybatisConfigDataList", );
    }

//    private void savePassWord() {
//        DatabaseCredentials dc = DatabaseCredentials.getInstance();
//        if (psiElements.length > 0) {
//            DbTableImpl table = (DbTableImpl) psiElements[0];
//            LocalDataSource dataSource = (LocalDataSource) (table.getDataSource().getDelegate());
//
//            DatabaseAuthPanel auth = new DatabaseAuthPanel(project, dataSource, dc);
//            auth.reset(dataSource, false);
//            DatabaseAuthProvider.AuthWidget widget = auth.getAuthWidget();
//            if (widget != null) {
//                widget.forceSave();
//            }
//            dc.setPassword(dataSource,"");
//            boolean ok;
//            if (ourCredentialsInterceptor == null) {
//                auth.getComponent().setBorder(Borders.emptyTop(12));
//                DialogBuilder builder = DatabaseCredentials.prepareDialog(project, (error ? MessageType.ERROR : MessageType.WARNING).getDefaultIcon(), auth.getComponent(), "Connecting to '" + dataSource.getName() + "'...", StringUtil.notNullize(message));
//                builder.setPreferredFocusComponent(auth.getPreferredFocusedComponent());
//                ok = builder.show() == 0;
//            } else {
//                ok = (Boolean) ourCredentialsInterceptor.fun(auth);
//            }
//
//            if (!ok) {
//                throw new ProcessCanceledException();
//            }
//
//            auth.save(dataSource, true);
//        }
//    }

    /**
     * 文件夹选择器
     *
     * @param resentableUrl 选择前的文件夹路径
     * @param src           默认的包路径
     * @return 选择的包路径
     */
    private String folderChooser(String resentableUrl, String src) {
        VirtualFile fileByPath;
        if (StringUtil.isNotEmpty(resentableUrl)) {
            fileByPath = LocalFileSystem.getInstance().findFileByPath(resentableUrl);
        } else {
            fileByPath = LocalFileSystem.getInstance().findFileByPath(Objects.requireNonNull(this.project.getPresentableUrl()));
        }
        if (fileByPath == null) {
            fileByPath = LocalFileSystem.getInstance().findFileByPath(Objects.requireNonNull(this.project.getPresentableUrl()));
        }
        VirtualFile virtualFile = FileChooser.chooseFile(FileChooserDescriptorFactory.createSingleFolderDescriptor(), this.project, fileByPath);
        if (virtualFile != null) {
            return virtualFile.getPath();
        } else {
            return this.project.getProjectFile() != null ? this.project.getBasePath() + src : "";
        }
    }

    /**
     * 打开包选择器
     * 选择源文件（java）
     *
     * @return 选择的路径
     */
    private String packageChooser() {
        PackageChooserDialog chooserDialog = new PackageChooserDialog("package chooser", this.project);
        chooserDialog.show();
        PsiPackage selectedPackage = chooserDialog.getSelectedPackage();
        return selectedPackage.getQualifiedName();
    }

    /**
     * 选择资源文件(resources)
     *
     * @return 选择的路径
     */
    private String resourcePackageChooser() {
        ResourcePackageChooserDialog chooserDialog = new ResourcePackageChooserDialog("choose mapper xml package", this.project);
        chooserDialog.show();
        PsiPackage selectedPackage = chooserDialog.getSelectedPackage();
        return selectedPackage.getQualifiedName();
    }

    /**
     * 临时测试使用
     *
     * @param pathParam
     * @return
     * @throws IOException
     */
    private static File getPath(@NotNull String pathParam) throws IOException {
        @NotNull PluginId id = Objects.requireNonNull(PluginId.findId("com.baomidou.plugin.idea.mybatisx"));
        final ScratchFileService scratchFileService = ScratchFileService.getInstance();
        final ExtensionsRootType extensionsRootType = ExtensionsRootType.getInstance();
        final String path = scratchFileService.getRootPath(extensionsRootType) + "/" + id.getIdString() +
                (StringUtil.isEmpty(pathParam) ? "" : "/"
                        + pathParam);
        @NotNull PluginId id2 = Objects.requireNonNull(PluginId.findId("com.lansoft.custom_mybatis_plugin"));
        System.out.println(id.getIdString());
        final File file = new File(path);
        if (!file.exists()) {
            extensionsRootType.extractBundledResources(id, "");
        }
        return file;
    }

    public void configText() {
        this.modelPComboBox.setSelectedItem("com.lansoft.entity");
        this.modelFText.setText(this.project.getBasePath() + "/src/main/java");
        this.mapperPComboBox.setSelectedItem("com.lansoft.mapper");
        this.mapperFText.setText(this.project.getBasePath() + "/src/main/java");
        this.xmlPComboBox.setSelectedItem("mybatis.mapper");
        this.xmlFText.setText(this.project.getBasePath() + "/src/main/resources");
        this.isLombok.setSelected(true);
        this.isUpperCamelCase.setSelected(true);
        for (String key : MybatisConstant.PLUGIN_TYPE.keySet()) {
            this.pluginType.addItem(key);
        }
    }

    /**
     * 获取到选中的表名存起来备用
     *
     * @param psiElements 触发事件收到的参数
     */
    private void readTableName(PsiElement[] psiElements) {
        for (PsiElement psiElement : psiElements) {
            final DbTable dbTable = (DbTable) psiElement;
            tableNameList.add(dbTable.getName());
        }
    }

    /**
     * 回显选中的表名到左侧的列表中
     *
     * @param psiElements 触发事件收到的参数
     */
    public void listShow(PsiElement[] psiElements) {
        selectedtableName.setModel(new AbstractListModel<String>() {
            @Override
            public int getSize() {
                return psiElements.length;
            }

            @Override
            public String getElementAt(int i) {
                return ((DbTable) psiElements[i]).getName();
            }
        });
    }

    /**
     * 将容器返回给idea
     * 在idea中显示当前窗口
     * 必须
     *
     * @return
     */
    @Override
    protected JComponent createCenterPanel() {
        //将容器返回给idea
        return contentPane;
    }


    @Override
    protected void doOKAction() {
        //保存用户设置的mybatis 配置
//        final MybatisConfig mybatisConfig = new MybatisConfig(this);
        //实行生成操作
//        MybatisPlusGenerator.generatorCode(mybatisConfig);
        //显示通知窗口
//        Messages.showMessageDialog("Mybatis生成完成", "Mybatis通知效果", Messages.getInformationIcon());
//        if (this.getOKAction().isEnabled()) {
//            System.out.println("===================start=================");
//            System.out.println(toString());
//            System.out.println("===================end================");
//        }
        //关闭窗口
//        this.close(0);

        if (this.tableNameList.size() > 0) {
            //保存用户设置的mybatis 配置
            MybatisConfig mybatisConfig = new MybatisConfig(this);
            PropertiesComponent.getInstance().setValue("CustomMybatisConfigData", JSON.toJSONString(mybatisConfig));
            String selectedItem = (String) this.pluginType.getSelectedItem();
            Map<String, MybatisGenerator> pluginType = MybatisConstant.PLUGIN_TYPE;
            MybatisGenerator mybatisGenerator = pluginType.get(selectedItem);
            if (mybatisGenerator != null) {
                //执行生成操作
                mybatisGenerator.generatorCode(mybatisConfig);
                Messages.showMessageDialog("Mybatis生成完成", "Mybatis插件生成结果", Messages.getInformationIcon());
                this.close(0);
                VirtualFileManager.getInstance().syncRefresh();
            } else {
                Messages.showMessageDialog("请选择插件类型", "Mybatis插件生成结果", Messages.getErrorIcon());
            }
        } else {
            Messages.showErrorDialog("请选择表名", "Mybatis逆向工程插件异常信息");
        }
    }

    @Override
    public String toString() {
        String str = "MybatisGeneratorMainUI:{" +
                "’tablePrefix‘:’" + tablePrefix.getText() +
                "‘,’tableNamePrefix’:‘" + tableNamePrefix.getText() +
                "’,‘moduleName’:‘" + moduleName.getSelectedItem() +
                "‘,’modelPComboBox’:‘" + modelPComboBox.getSelectedItem() +
                "‘,’modelFText‘:’" + modelFText.getText() +
                "’,‘mapperPComboBox‘:’" + mapperPComboBox.getSelectedItem() +
                "‘,mapperFText‘:’" + mapperFText.getText() +
                "’,‘xmlPComboBox’:‘" + xmlPComboBox.getSelectedItem() +
                "’,‘xmlFText’:‘" + xmlFText.getText() +
                "‘,’isLombok‘:’" + isLombok.isSelected() +
                "’,‘lombokComboBox’:‘" + lombokComboBox.getSelectedItem() + "}";

        return str;
    }

    //    @Nullable
//    @Override
//    protected JComponent createCenterPanel() {
//        JPanel panel = new JPanel(new BorderLayout());
//        tfTitle = new EditorTextField("笔记的标题");
//        tfMark = new EditorTextField("笔记的内容");
//        //设置宽高
//        tfMark.setPreferredSize(new Dimension(200, 200));
//        panel.add(tfTitle, BorderLayout.NORTH);
//        panel.add(tfMark, BorderLayout.CENTER);
//        return panel;
////        return this;
//    }


    public PsiElement[] getPsiElements() {
        return psiElements;
    }

    public JTextField getTableNamePrefix() {
        return tableNamePrefix;
    }

    public JLabel getTablePrefix() {
        return tablePrefix;
    }

    public JTextField getAuthor() {
        return author;
    }

    public JComboBox<String> getModelPComboBox() {
        return modelPComboBox;
    }

    public JTextField getModelFText() {
        return modelFText;
    }

    public JComboBox<String> getMapperPComboBox() {
        return mapperPComboBox;
    }

    public JTextField getMapperFText() {
        return mapperFText;
    }

    public JComboBox<String> getXmlPComboBox() {
        return xmlPComboBox;
    }

    public JTextField getXmlFText() {
        return xmlFText;
    }

    public JComboBox<String> getProjectPath() {
        return moduleName;
    }

    public JCheckBox getIsLombok() {
        return isLombok;
    }

    public JCheckBox getIsRestController() {
        return isRestController;
    }

    public JCheckBox getIsServiceImpl() {
        return isServiceImpl;
    }

    public JCheckBox getIsService() {
        return isService;
    }

    public JCheckBox getIsUpperCamelCase() {
        return isUpperCamelCase;
    }

    public ArrayList<String> getTableNameList() {
        return tableNameList;
    }

    public JComboBox<String> getModuleName() {
        return this.moduleName;
    }

    public JComboBox<String> getPluginType() {
        return this.pluginType;
    }

    public Project getProject() {
        return this.project;
    }
}
