package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;
import dev.johnwatts.plugins.certificates.shared.Result;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Optional;

public class FindFromDer extends FromFileFindingStrategy  {
    @Override
    public Result find(AnActionEvent source) {
        Result result = new Result();
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            result.setCertificate(Optional.of(
                    (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(this.getSource(source).contentsToByteArray())))
            );
            return result;
        } catch (CertificateException | IOException | NoSourceException e) {
            return Result.exceptionThrown(e);
        }
    }
}
