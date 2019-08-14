package dev.johnwatts.plugins.certificates;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.util.DocumentUtil;

import java.security.cert.X509Certificate;
import java.util.Optional;

public class FindByBeginAndEnd implements CertificateFindingStrategy<Editor> {
    // TODO - refactor this so that FindByBeginAndEnd<Editor> FindByBeginAndEnd<File> are things.
    public Optional<X509Certificate> find(Editor editor) {
        int startOfEncodedCert;
        int endOfEncodedCert;

        try {
            startOfEncodedCert = findStartOfEncodedCert(editor);
            endOfEncodedCert = findEndOfEncodedCert(editor);
        } catch (CertificateNotFoundException e) {
            return Optional.empty();
        }

        editor.getSelectionModel().setSelection(
                editor.getDocument().getLineStartOffset(startOfEncodedCert),
                editor.getDocument().getLineEndOffset(endOfEncodedCert));
            return Base64X509Decoder.decode(editor.getSelectionModel().getSelectedText());
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
}
