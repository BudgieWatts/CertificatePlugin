package dev.johnwatts.plugins.certificates.dialogs;

import dev.johnwatts.plugins.certificates.shared.Result;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;

import javax.swing.*;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.awt.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.function.Supplier;

class CertificateDisplayPanel implements Supplier<JComponent> {

    private Result result;

    public CertificateDisplayPanel(Result result) {
        this.result = result;
    }

    @Override
    public JComponent get() {

        JLabel notBefore;
        JLabel notAfter;

        PanelBuilder panelBuilder = new PanelBuilder();

        X509Certificate certificate = this.result.getCertificate().get();

        X500Name subjectX500name;

        try {
            subjectX500name = new JcaX509CertificateHolder(certificate).getSubject();
        } catch (CertificateEncodingException e) {
            panelBuilder.addLabel("Something went wrong parsing the subject distinguished name.");
            return panelBuilder.getPanel();
        }

        X500Name issuerX500name;

        try {
            issuerX500name = new JcaX509CertificateHolder(certificate).getIssuer();
        } catch (CertificateEncodingException e) {
            panelBuilder.addLabel("Something went wrong parsing the issuer distinguished name.");
            return panelBuilder.getPanel();
        }

        panelBuilder.setAnchor(GridBagConstraints.WEST);
        panelBuilder.setFill(GridBagConstraints.NONE);
        panelBuilder.addLabelledComponent("Subject:", X500NamePanelBuilder.getPanel(subjectX500name));
        panelBuilder.setFill(GridBagConstraints.HORIZONTAL);

        if ( issuerX500name.equals(subjectX500name) ) {
            panelBuilder.addLabelledText("", "Self-signed");
        } else {
            panelBuilder.setAnchor(GridBagConstraints.WEST);
            panelBuilder.setFill(GridBagConstraints.NONE);
            panelBuilder.addLabelledComponent("Issuer:", X500NamePanelBuilder.getPanel(issuerX500name));
            panelBuilder.setFill(GridBagConstraints.HORIZONTAL);
        }

        notBefore = new JLabel(certificate.getNotBefore().toString());
        if ( Date.from(Instant.now()).before(certificate.getNotBefore()) ) {
            notBefore.setForeground(Color.RED);
        }
        panelBuilder.addLabelledComponent("Not before:", notBefore);

        notAfter = new JLabel(certificate.getNotAfter().toString());
        if ( Date.from(Instant.now()).after(certificate.getNotAfter()) ) {
            notAfter.setForeground(Color.RED);
        }
        panelBuilder.addLabelledComponent("Not after:", notAfter);

        panelBuilder.addLabelledText("Serial Number:", certificate.getSerialNumber().toString());
        panelBuilder.addLabelledText("Version:", Integer.toString(certificate.getVersion()));

        return panelBuilder.getPanel();
    }

    private static class X500NamePanelBuilder {
        public static JPanel getPanel(X500Name x500Name) {
            PanelBuilder panelBuilder = new PanelBuilder();
            panelBuilder.setAnchor(GridBagConstraints.WEST);
            panelBuilder.setFill(GridBagConstraints.NONE);
            panelBuilder.setInsets(new Insets(0, 0, 5, 5));
            List<ASN1ObjectIdentifier> attributeTypes = Arrays.asList(x500Name.getAttributeTypes());
            Collections.reverse(attributeTypes);
            Set<ASN1ObjectIdentifier> attributeSet = new LinkedHashSet<>(attributeTypes);

            PirateBCStyle pirateBCStyle = new PirateBCStyle();

            for ( ASN1ObjectIdentifier attributeType: attributeSet ) {
                List<RDN> rdns = Arrays.asList(x500Name.getRDNs(attributeType));
                Collections.reverse(rdns);
                for ( RDN rdn : rdns ) {
                    panelBuilder.addLabelledText(pirateBCStyle.oidToDisplayName(attributeType) + " = ",
                            IETFUtils.valueToString(rdn.getFirst().getValue()));
                }
            }
            JPanel panel = panelBuilder.getPanel();
            return panel;
        }
    }
}
