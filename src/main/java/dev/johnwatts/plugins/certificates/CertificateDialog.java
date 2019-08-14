package dev.johnwatts.plugins.certificates;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CertificateDialog extends DialogWrapper {
    // TODO - this could be improved so much - things like expandable details
    private JPanel panel;
    private TextArea textArea;

    public CertificateDialog(Project project) {
        super(project);
        setTitle("DialogWrapper");
        createCenterPanel();
        init();
    }

    protected void setText(String message) {
        textArea.setText(message);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        panel = new JPanel(new GridLayout());
        textArea = new TextArea();
        panel.add(textArea);
        return panel;
    }
}
