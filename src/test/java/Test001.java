import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiPackage;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.SystemIndependent;
import org.picocontainer.PicoContainer;

/**
 * @author guowd
 * @date 2021/7/10  16:31
 */
public class Test001 {
    public static void main(String[] args) {
        PackageChooserDialog chooserDialog = new PackageChooserDialog("package chooser", new Project() {
            @Override
            public @NotNull String getName() {
                return null;
            }

            @Override
            public VirtualFile getBaseDir() {
                return null;
            }

            @Override
            public @Nullable @SystemIndependent String getBasePath() {
                return null;
            }

            @Override
            public @Nullable VirtualFile getProjectFile() {
                return null;
            }

            @Override
            public @Nullable @SystemIndependent String getProjectFilePath() {
                return null;
            }

            @Override
            public @Nullable VirtualFile getWorkspaceFile() {
                return null;
            }

            @Override
            public @NotNull String getLocationHash() {
                return null;
            }

            @Override
            public void save() {

            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public boolean isInitialized() {
                return false;
            }

            @Override
            public <T> T getComponent(@NotNull Class<T> interfaceClass) {
                return null;
            }

            @Override
            public @NotNull PicoContainer getPicoContainer() {
                return null;
            }

            @Override
            public @NotNull MessageBus getMessageBus() {
                return null;
            }

            @Override
            public boolean isDisposed() {
                return false;
            }

            @Override
            public @NotNull Condition<?> getDisposed() {
                return null;
            }

            @Override
            public void dispose() {

            }

            @Override
            public <T> @Nullable T getUserData(@NotNull Key<T> key) {
                return null;
            }

            @Override
            public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {

            }
        });
        chooserDialog.show();
        PsiPackage selectedPackage = chooserDialog.getSelectedPackage();
        System.out.println(selectedPackage.getQualifiedName());
    }
}
