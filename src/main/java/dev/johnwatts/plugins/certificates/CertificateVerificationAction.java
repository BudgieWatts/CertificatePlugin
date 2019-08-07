package dev.johnwatts.plugins.certificates;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

public class CertificateVerificationAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        File cert = chooseFile("Select Certificate to Verify");
        if( cert == null ) {
            return;
        }

        File key = chooseFile("Select Key to Verify");

        if( key == null ) {
            return;
        }

        CertificateVerifier verifier = new CertificateVerifier();

        boolean result;

        try {
            verifier.setCert(cert);
            verifier.setKey(key);
            result = verifier.verifyKeyPair();
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | CertificateException | IOException | InvalidKeySpecException exception) {
            Messages.showMessageDialog(exception.getMessage(), "An exception was thrown.", null);
            return;
        }

        if ( result ) {
            Messages.showMessageDialog("Certificate public key and private key are a pair.", "Success", null);
        } else {
            Messages.showMessageDialog("Certificate public key and private key are NOT a pair.", ":(", null);
        }
    }

    private File chooseFile(String title) {
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle(title);
        jfc.showOpenDialog(jfc);
        return jfc.getSelectedFile();
    }

    public static void main(String[] args) {
        CertificateVerificationAction action = new CertificateVerificationAction();
        action.actionPerformed(null);
    }
}
