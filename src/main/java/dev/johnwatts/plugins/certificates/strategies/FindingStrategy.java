package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.UserDataHolder;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;
import dev.johnwatts.plugins.certificates.shared.Result;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class FindingStrategy {

    private static final String CERT_HEADER = "-----BEGIN CERTIFICATE-----";
    private static final String CERT_FOOTER = "-----END CERTIFICATE-----";

    protected abstract UserDataHolder getSource(AnActionEvent event) throws NoSourceException;

    public abstract Result find(AnActionEvent source);

    protected List<X509Certificate> parsePEM(String pem) {
        int matches = StringUtils.countMatches(pem, CERT_HEADER);
        if (matches == 1) {
            return Collections.singletonList(getX509Certificate(pem));
        }

        int certCount = 0;
        List<X509Certificate> certificates = new ArrayList<>();

        while (certCount < matches) {
            String certPEM = pem.substring(pem.indexOf(CERT_HEADER), pem.indexOf(CERT_FOOTER) + CERT_FOOTER.length());
            certificates.add(getX509Certificate(certPEM));
            pem = pem.substring(pem.indexOf(CERT_FOOTER) + CERT_FOOTER.length());
            certCount++;
        }

        return certificates;
    }

    @NotNull
    private X509Certificate getX509Certificate(String content) {
        String base64Contents = content.replace(CERT_HEADER, "").replace(CERT_FOOTER, "");
        Optional<X509Certificate> cert = Base64X509Decoder.decode(base64Contents);

        if (cert.isEmpty()) {
            throw new IllegalArgumentException("failed to decode PEM file");
        }

        return cert.get();
    }
}
