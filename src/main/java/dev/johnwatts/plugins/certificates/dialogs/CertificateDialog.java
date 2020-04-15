package dev.johnwatts.plugins.certificates.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import dev.johnwatts.plugins.certificates.shared.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.function.Supplier;

public class CertificateDialog extends DialogWrapper {

    private Result result;

    private Supplier<JComponent> displayPanelSupplier;

    public CertificateDialog(Project project) {
        super(project);
    }

    public CertificateDialog(Project project, Result result) {
        this(project);
        this.result = result;
        if ( result.isSuccessful() ) {
            this.displayPanelSupplier = new CertificateDisplayPanel(result);
        } else {
            this.displayPanelSupplier = new SimpleMessagePanel(result);
        }
        init();
    }

    @NotNull
    protected Action[] createActions() {
        return new Action[]{new DialogWrapperExitAction("OK", 0)};
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        return displayPanelSupplier.get();

    }
}
