package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;
import dev.johnwatts.plugins.certificates.shared.Result;

import java.io.IOException;

public class FindFromPem extends FromFileFindingStrategy  {
    @Override
    public Result find(AnActionEvent source) {
        Result result = new Result();

        try {
            String base64Contents =
                    new String(this.getSource(source).contentsToByteArray())
                    .replace("-----BEGIN CERTIFICATE-----", "")
                    .replace("-----END CERTIFICATE-----", "");
            result.setCertificate(Base64X509Decoder.decode(base64Contents));
        } catch (IOException | IllegalArgumentException | NoSourceException e) {
            return Result.exceptionThrown(e);
        }
        return result;
    }
}
