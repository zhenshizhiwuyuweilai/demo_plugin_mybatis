package com.lansoft.ui;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Consumer;
import com.lansoft.HistoryData;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

/**
 * export jar settings dialog (link to SettingDialog.form)
 */
@Deprecated
public class SettingDialog extends JDialog {
    private Project project;
    @Nullable
    private VirtualFile[] selectedFiles;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JCheckBox exportJavaFileCheckBox;
    private JCheckBox exportClassFileCheckBox;
    private JCheckBox exportTestFileCheckBox;
    private JComboBox<String> outPutJarFileComboBox;
    private JButton selectJarFileButton;
    private JPanel settingPanel;
    private HistoryData historyData;

    public SettingDialog(Project project, @Nullable VirtualFile[] selectedFiles) {
        this.project = project;
        this.selectedFiles = selectedFiles;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.buttonOK.addActionListener(e -> onOK());
        this.buttonCancel.addActionListener(e -> onCancel());

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        this.contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), 1);
        this.selectJarFileButton.addActionListener(this::onSelectJarFileButton);

//        readSaveHistory();
//        initComboBox();
    }

    /**
     * get setting panel which contains components except ok and cancel buttons.
     *
     * @return
     */
    public JPanel getSettingPanel() {
        return this.settingPanel;
    }

    private void initComboBox() {
        String[] historyFiles;
        if (historyData != null) {
            historyFiles =
                    Arrays.stream(Optional.ofNullable(historyData.getSavedJarInfos()).orElse(new HistoryData.SavedJarInfo[0]))
                            .map(HistoryData.SavedJarInfo::getPath).toArray(String[]::new);
        } else {
            historyFiles = new String[0];
        }
        ComboBoxModel<String> model = new DefaultComboBoxModel<>(historyFiles);
        outPutJarFileComboBox.setModel(model);
    }

    private void readSaveHistory() {
//        if (historyData != null) { // already initialized
//            return;
//        }
//        Constants.cachePath.toFile().mkdirs();
//        if (Files.exists(Constants.historyFilePath)) {
//            try {
//                String historyJson = new String(Files.readAllBytes(Constants.historyFilePath), StandardCharsets.UTF_8);
//                historyData = CommonUtils.fromJson(historyJson, HistoryData.class);
//            } catch (Exception e) {
//                MessagesUtils.warn(project, e.getMessage());
//            }
//        }
    }

    private void writeSaveHistory(Path exportJarName) {
//        if (historyData == null) {
//            this.historyData = new HistoryData();
//        }
//        HistoryData.SavedJarInfo savedJar = new HistoryData.SavedJarInfo();
//        savedJar.setCreation(System.currentTimeMillis());
//        savedJar.setPath(exportJarName.toString());
//        historyData.addSavedJarInfo(savedJar);
//        Constants.cachePath.toFile().mkdirs();
//        final String json = CommonUtils.toJson(historyData);
//        if (json != null) {
//            try {
//                Files.write(Constants.historyFilePath, json.getBytes(StandardCharsets.UTF_8));
//            } catch (IOException e) {
//                MessagesUtils.warn(project, e.getMessage());
//            }
//        }
    }

    private void onSelectJarFileButton(ActionEvent event) {
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
        Consumer chooserConsumer = new FileChooserConsumerImplForComboBox(this.outPutJarFileComboBox);
        FileChooser.chooseFile(descriptor, project, null, chooserConsumer);
    }


    private void onCancel() {
        this.dispose();
    }

    private void onOK() {
//        doExport(this.selectedFiles);
    }

//    public void doExport(VirtualFile[] exportFiles) {
//        final Module[] modules = CommonUtils.findModule(project, exportFiles);
//        String selectedOutputJarFullPath = (String) this.outPutJarFileComboBox.getModel().getSelectedItem();
//        if (selectedOutputJarFullPath == null || selectedOutputJarFullPath.trim().length() == 0) {
//            WaitForProgressToShow.runOrInvokeAndWaitAboveProgress(() -> showErrorDialog(project, "the selected output path should not empty", Constants.actionName));
//            return;
//        }
//        Path exportJarFullPath = Paths.get(selectedOutputJarFullPath.trim());
//        if (!Files.isDirectory(exportJarFullPath)) {
//            Path exportJarParentPath = exportJarFullPath.getParent();
//            if (exportJarParentPath == null) {// when input file without parent dir, current dir as parent dir.
//                String basePath = project.getBasePath();
//                if (basePath == null) {
//                    exportJarParentPath = Paths.get("./");
//                } else {
//                    exportJarParentPath = Paths.get(basePath);
//                }
//                exportJarFullPath = exportJarParentPath.resolve(exportJarFullPath);
//            }
//            if (!Files.exists(exportJarParentPath)) {
//                WaitForProgressToShow.runOrInvokeAndWaitAboveProgress(() -> showErrorDialog(project, "the selected output path is not exists", Constants.actionName));
//            } else {
//                String exportJarName = exportJarFullPath.getFileName().toString();
//                if (!exportJarName.endsWith(".jar")) {
//                    exportJarFullPath = Paths.get(exportJarFullPath.toString() + ".jar");
//                }
//                if (Files.exists(exportJarFullPath)) {
//                    final int[] result = new int[1];
//                    final Path finalJarPath = exportJarFullPath;
//                    WaitForProgressToShow.runOrInvokeAndWaitAboveProgress(
//                            () -> result[0] = Messages.showYesNoDialog(project, finalJarPath + " already exists, replace it? ",
//                                    Constants.actionName, getWarningIcon()));
//                    if (result[0] == Messages.NO) {
//                        return;
//                    }
//                }
//                this.dispose();
//                writeSaveHistory(exportJarFullPath);
//                final CompileStatusNotification packager = new ExportPacker(project, exportFiles, exportJarFullPath,
//                        exportJavaFileCheckBox.isSelected(), exportClassFileCheckBox.isSelected(),
//                        exportTestFileCheckBox.isSelected());
//                WaitForProgressToShow.runOrInvokeAndWaitAboveProgress(() ->
//                        CompilerManager.getInstance(project).make(project, modules, packager));
//            }
//        } else {
//            WaitForProgressToShow.runOrInvokeAndWaitAboveProgress(() -> showErrorDialog(project, "please specify export jar file name", Constants.actionName));
//        }
//    }

    private static class FileChooserConsumerImplForComboBox implements Consumer<VirtualFile> {
        private JComboBox<String> comboBox;

        public FileChooserConsumerImplForComboBox(JComboBox<String> comboBox) {
            this.comboBox = comboBox;
        }

        @Override
        public void consume(VirtualFile virtualFile) {
            String filePath = virtualFile.getPath();
            if (filePath.trim().length() == 0) {
                return;
            }
            ((DefaultComboBoxModel<String>) comboBox.getModel()).insertElementAt(filePath, 0);
            comboBox.setSelectedIndex(0);
        }
    }

}
