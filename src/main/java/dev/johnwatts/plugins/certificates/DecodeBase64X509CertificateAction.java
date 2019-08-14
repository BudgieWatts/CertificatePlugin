package dev.johnwatts.plugins.certificates;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import java.security.cert.X509Certificate;
import java.util.Optional;

import static dev.johnwatts.plugins.certificates.CertificateStringFactory.Field.ISSUER;
import static dev.johnwatts.plugins.certificates.CertificateStringFactory.Field.NOT_AFTER;
import static dev.johnwatts.plugins.certificates.CertificateStringFactory.Field.NOT_BEFORE;
import static dev.johnwatts.plugins.certificates.CertificateStringFactory.Field.PUBLIC_KEY;
import static dev.johnwatts.plugins.certificates.CertificateStringFactory.Field.SUBJECT;

public class DecodeBase64X509CertificateAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        StringBuffer message = new StringBuffer();
        String title;
        SelectionModel selectionModel;

        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (editor == null) {
            return;
        }

        Optional<X509Certificate> cert = Optional.empty();

        selectionModel = editor.getSelectionModel();

        try {
            if(selectionModel.hasSelection()) {
                cert = Base64X509Decoder.decode(selectionModel.getSelectedText());
            }
            // Possibly interrogate pastebuffer, maybe before selection model
            if (cert.isEmpty()) {
                cert = new FindByBeginAndEnd().find(editor);
            }

            if( cert.isPresent() ) {
                title = "Certificate details";
                updateMessageWithCertDetails(message, cert.get());
            } else {
                title = "Couldn't find a certificate";
                message.append("A certificate could not be found.");
            }
            // eventually, use a chain of strategies which will mean the 'failures' will need reconsidering.

        } catch (IllegalArgumentException e) {
            title = "There was something wrong with the selected text";
            message.append(selectionModel.getSelectedText());
        }

        CertificateDialog dialog = new CertificateDialog(editor.getProject());
        dialog.setTitle(title);
        dialog.setText(message.toString());
        dialog.show();
    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    private void updateMessageWithCertDetails(StringBuffer message, X509Certificate cert) {
        message.append(CertificateStringFactory.output(cert, ISSUER, SUBJECT, NOT_BEFORE, NOT_AFTER, PUBLIC_KEY));
    }
}
