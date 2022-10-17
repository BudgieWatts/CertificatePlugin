package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;
import dev.johnwatts.plugins.certificates.shared.Result;

import java.io.IOException;

public class FindFromDer extends FromFileFindingStrategy {
    @Override
    public Result find(AnActionEvent source) {
        try {
            Result result = new Result();
            result.setCertificates(parsePEM(new String(this.getSource(source).contentsToByteArray())));
            return result;
        } catch (IOException | NoSourceException e) {
            return Result.exceptionThrown(e);
        }
    }
}
