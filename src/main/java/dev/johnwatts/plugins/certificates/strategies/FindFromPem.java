package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;
import dev.johnwatts.plugins.certificates.shared.Result;

import java.io.IOException;

public class FindFromPem extends FromFileFindingStrategy {

    @Override
    public Result find(AnActionEvent source) {
        try {
            FileDocumentManager.getInstance().saveAllDocuments();
            String content = new String(this.getSource(source).contentsToByteArray());
            Result result = new Result();
            result.setCertificates(parsePEM(content));
            return result;
        } catch (IOException | IllegalArgumentException | NoSourceException e) {
            return Result.exceptionThrown(e);
        }
    }
}
