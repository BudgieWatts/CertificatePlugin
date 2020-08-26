package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.util.DocumentUtil;
import dev.johnwatts.plugins.certificates.shared.CertificateNotFoundException;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;
import dev.johnwatts.plugins.certificates.shared.Result;

public class FindByBeginAndEnd extends FromEditorFindingStrategy {
    @Override
    public Result find(AnActionEvent source) {
        int startOfEncodedCert;
        int endOfEncodedCert;

        Result result = new Result();
        Editor editor;

        try {
            editor = this.getSource(source);
            try {
                startOfEncodedCert = findStartOfEncodedCert(editor);
                endOfEncodedCert = findEndOfEncodedCert(editor);
            } catch (CertificateNotFoundException e) {
                result.setMessage(e.getMessage());
                return result;
            }

            editor.getSelectionModel().setSelection(
                    editor.getDocument().getLineStartOffset(startOfEncodedCert),
                    editor.getDocument().getLineEndOffset(endOfEncodedCert));
            result.setCertificates(parsePEM(editor.getSelectionModel().getSelectedText()));
            return result;
        } catch (NoSourceException e) {
            return Result.noCertificateFound();
        }
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
