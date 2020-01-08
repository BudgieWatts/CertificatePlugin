package dev.johnwatts.plugins.certificates.dialogs;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import dev.johnwatts.plugins.certificates.shared.Result;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CertificateDialog extends DialogWrapper {

    private Result result;

    private Supplier<JComponent> displayPanelSupplier;

    private List<String> flippantButtonText;

    public CertificateDialog(Project project) {
        super(project);
        flippantButtonText = new ArrayList<>();
        flippantButtonText.add("Same cert, different day");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("OK");
        flippantButtonText.add("Alice says hi!");
        flippantButtonText.add("Bob says hi!");
        flippantButtonText.add("Alice says hi!");
        flippantButtonText.add("Bob says hi!");
        flippantButtonText.add("I love PKI");
        flippantButtonText.add("I love PKI");
        flippantButtonText.add("I love PKI");
        flippantButtonText.add("I love PKI");
        flippantButtonText.add("I love PKI");
        flippantButtonText.add("I love PKI");
        flippantButtonText.add("Can you really trust a root CA?");
        flippantButtonText.add("Can you really trust a root CA?");
        flippantButtonText.add("Can you really trust a root CA?");
        flippantButtonText.add("Can you really trust a root CA?");
        flippantButtonText.add("Can you really trust a root CA?");
        flippantButtonText.add("Click here to delete everything");
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
        return new Action[]{new DialogWrapperExitAction(getRandomButtonText(), 0)};
    }

    private String getRandomButtonText() {
        return flippantButtonText.get((int)(Math.random() * (flippantButtonText.size())));
    }
    @Nullable
    @Override
    protected JComponent createCenterPanel() {

        return displayPanelSupplier.get();

    }

}
