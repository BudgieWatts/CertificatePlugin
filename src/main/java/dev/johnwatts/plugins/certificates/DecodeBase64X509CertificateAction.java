package dev.johnwatts.plugins.certificates;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.DocumentUtil;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

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

        selectionModel = editor.getSelectionModel();

        try {
            if (selectionModel.getSelectedText() == null) {
                tryToFindAndSelectEncodedCertificate(editor);
            }

            X509Certificate cert = decodeBase64X509Certificate(selectionModel.getSelectedText());

            title = "Certificate details";
            updateMessageWithCertDetails(message, cert);

        } catch (CertificateNotFoundException e) {
            title = "I see no certs";
            message.append(e.getMessage());

        } catch (IllegalArgumentException | CertificateException e) {
            title = "There was something wrong with the selected text";
            message.append(selectionModel.getSelectedText());
        }
        Messages.showMessageDialog(message.toString(), title, null);
    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

    private void updateMessageWithCertDetails(StringBuffer message, X509Certificate cert) {
        message.append(String.format("Subject X500 Principle: %s\n\n", cert.getSubjectX500Principal()));
        message.append(String.format("Not Before: %s\n\n", cert.getNotBefore()));
        message.append(String.format("Not After: %s", cert.getNotAfter()));
    }

    private void tryToFindAndSelectEncodedCertificate(Editor editor) throws CertificateNotFoundException {
        int startOfEncodedCert;
        int endOfEncodedCert;

        startOfEncodedCert = findStartOfEncodedCert(editor);
        endOfEncodedCert = findEndOfEncodedCert(editor);

        editor.getSelectionModel().setSelection(
                editor.getDocument().getLineStartOffset(startOfEncodedCert),
                editor.getDocument().getLineEndOffset(endOfEncodedCert));

    }

    private int findStartOfEncodedCert(Editor editor) throws CertificateNotFoundException {
        Caret caret = editor.getCaretModel().getCurrentCaret();
        LogicalPosition logicalPosition = caret.getLogicalPosition();
        Document document = editor.getDocument();
        int lineNumber = logicalPosition.line;

        while (lineNumber >= 0) {
            if (document.getText(DocumentUtil.getLineTextRange(document, lineNumber)).equals("-----BEGIN CERTIFICATE-----")) {
                caret.moveToLogicalPosition(new LogicalPosition(lineNumber, 0));
                return lineNumber + 1;
            }
            lineNumber--;
        }

        // Maybe there is a cert in this editor window, it's just it's below the caret
        lineNumber = logicalPosition.line;
        while (lineNumber <= document.getLineCount() - 1) {
            if (document.getText(DocumentUtil.getLineTextRange(document, lineNumber)).equals("-----BEGIN CERTIFICATE-----")) {
                caret.moveToLogicalPosition(new LogicalPosition(lineNumber, 0));
                return lineNumber + 1;
            }
            lineNumber++;
        }

        throw new CertificateNotFoundException("Could not find -----BEGIN CERTIFICATE----- on or above the caret");
    }

    private int findEndOfEncodedCert(Editor editor) throws CertificateNotFoundException {
        Caret caret = editor.getCaretModel().getCurrentCaret();
        LogicalPosition logicalPosition = caret.getLogicalPosition();
        Document document = editor.getDocument();
        int lineNumber = logicalPosition.line;

        while (lineNumber <= document.getLineCount() - 1) {
            if (document.getText(DocumentUtil.getLineTextRange(document, lineNumber)).equals("-----END CERTIFICATE-----")) {
                return lineNumber - 1;
            }
            lineNumber++;
        }
        throw new CertificateNotFoundException("Could not find -----END CERTIFICATE----- on or below the caret");
    }

    private X509Certificate decodeBase64X509Certificate(String base64EncodedCertificate) throws CertificateException, IllegalArgumentException {
        byte decodedCert[] = Base64.getMimeDecoder().decode(base64EncodedCertificate);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        return (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(decodedCert));
    }

    private class CertificateNotFoundException extends Exception {
        CertificateNotFoundException(String message) {
            super(message);
        }
    }
}
