package dev.johnwatts.plugins.certificates.dialogs;

import dev.johnwatts.plugins.certificates.shared.Result;

import javax.swing.*;
import java.awt.*;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Date;
import java.util.function.Supplier;

class CertificateDisplayPanel implements Supplier<JComponent> {

    private Result result;

    public CertificateDisplayPanel(Result result) {
        this.result = result;
    }

    @Override
    public JComponent get() {

        TextArea keyUsage;
        JLabel notBefore;
        JLabel notAfter;

        DialogBuilder dialogBuilder = new DialogBuilder();

        X509Certificate certificate = this.result.getCertificate().get();

        notBefore = new JLabel(certificate.getNotBefore().toString());
        if ( Date.from(Instant.now()).before(certificate.getNotBefore()) ) {
            notBefore.setForeground(Color.RED);
        }
        dialogBuilder.addLabelledComponent("Not before:", notBefore);

        notAfter = new JLabel(certificate.getNotAfter().toString());
        if ( Date.from(Instant.now()).after(certificate.getNotAfter()) ) {
            notAfter.setForeground(Color.RED);
        }
        dialogBuilder.addLabelledComponent("Not after:", notAfter);

        dialogBuilder.addLabelledText("Serial Number:", certificate.getSerialNumber().toString());
        dialogBuilder.addLabelledText("Version:", Integer.toString(certificate.getVersion()));

        try {
            if ( certificate.getExtendedKeyUsage() != null ) {
                keyUsage = new TextArea();
                certificate.getExtendedKeyUsage().forEach(usage -> keyUsage.append(usage + "\n"));
                dialogBuilder.addLabel("Key Usage");
                dialogBuilder.addSpanningComponent(keyUsage);
            }
        } catch (CertificateParsingException e) {
            e.printStackTrace();
        }

        return dialogBuilder.getPanel();
    }
}
