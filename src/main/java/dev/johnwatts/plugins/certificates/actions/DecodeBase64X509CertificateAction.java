package dev.johnwatts.plugins.certificates.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import dev.johnwatts.plugins.certificates.strategies.FindByBeginAndEnd;
import dev.johnwatts.plugins.certificates.strategies.FindFromDer;
import dev.johnwatts.plugins.certificates.strategies.FindFromPem;
import dev.johnwatts.plugins.certificates.strategies.FindFromSelection;
import dev.johnwatts.plugins.certificates.shared.Result;
import dev.johnwatts.plugins.certificates.dialogs.CertificateDialog;
import dev.johnwatts.plugins.certificates.strategies.FindingStrategy;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.jetbrains.annotations.NotNull;

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.List;

public class DecodeBase64X509CertificateAction extends AnAction {

    static final List<FindingStrategy> strategies;

    static {
        strategies = List.of(new FindFromPem(), new FindFromDer(), new FindFromSelection(), new FindByBeginAndEnd());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        CertificateDialog dialog = new CertificateDialog(null, findCertificate(event));
        dialog.show();
    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);

        Result result = findCertificate(e);

        if (!result.getCertificates().isEmpty()) {
            StringBuilder menuText = new StringBuilder("Display certificate: ");
            for (X509Certificate certificate : result.getCertificates()) {
                X500Name x500name;
                try {
                    x500name = new JcaX509CertificateHolder(certificate).getSubject();
                } catch (CertificateEncodingException ex) {
                    return;
                }
                RDN[] rdns = x500name.getRDNs(BCStyle.CN);
                menuText.append(IETFUtils.valueToString(rdns[0].getFirst().getValue()));
                menuText.append(", ");
                try {
                    certificate.checkValidity();
                } catch (CertificateExpiredException ex) {
                    menuText.append(" [EXPIRED]");
                } catch (CertificateNotYetValidException ex) {
                    menuText.append(" [NOT YET VALID]");
                }
            }

            // remove trailing comma space
            menuText.setLength(menuText.length() - 2);
            e.getPresentation().setText(menuText.toString());
        } else {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }

    private Result findCertificate(AnActionEvent event) {
        Result result;
        try {
            result = strategies.stream().map(strategy -> strategy.find(event))
                    .filter(Result::isSuccessful)
                    .findFirst()
                    .orElse(Result.noCertificateFound());
        } catch (IllegalArgumentException e) {
            result = Result.exceptionThrown(e);
        }
        return result;
    }
}
