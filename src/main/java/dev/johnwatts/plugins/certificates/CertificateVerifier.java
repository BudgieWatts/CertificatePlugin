package dev.johnwatts.plugins.certificates;

import com.intellij.openapi.ui.Messages;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import picocli.CommandLine;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.Callable;

public class CertificateVerifier implements Callable<Integer> {
    @CommandLine.Option(names = {"-c", "--cert"}, description = "X509 certificate to verify")
    File cert;

    @CommandLine.Option(names = {"-k", "--key"}, description = "private key to verify")
    File key;

    private X509Certificate x509;
    private PrivateKey privateKey;

    @Override
    public Integer call() throws Exception {

        exitIfFilesNonExistent();

        makeX509();
        makePrivateKey();

        boolean result = verifyKeyPair();
        if ( result ) {
            System.out.println("Certificate public key and private key are a pair.");
            return 0;
        } else {
            System.out.println("Certificate public key and private key are not a pair");
            return 1;
        }
    }

    public void setCert(File cert) throws CertificateException, IOException {
        this.cert = cert;
        if(!doesFileExist(this.cert)) Messages.showMessageDialog("Certificate file doesn't exist", "WTF", null);;
        makeX509();
    }

    public void setKey(File key) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        this.key = key;
        if(!doesFileExist(this.key)) Messages.showMessageDialog("Key file doesn't exist", "WTF", null);;
        makePrivateKey();
    }

    public boolean verifyKeyPair() throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        final String plainTextMessage = "I am a spanner";

        String encryptedMessage = encryptText(plainTextMessage);
        String decryptedMessage = decryptText(encryptedMessage);

        return decryptedMessage.equals(plainTextMessage);
    }

    private void makePrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] decodedKey = decodeBase64File(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance(x509.getPublicKey().getAlgorithm());
        this.privateKey = kf.generatePrivate(spec);
    }

    private void makeX509() throws CertificateException, IOException {
        Security.addProvider(new BouncyCastleProvider());
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        byte[] decodedCert = decodeBase64File(cert);
        this.x509 =  (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(decodedCert));
    }

    private void exitIfFilesNonExistent() {
        if ( !(doesFileExist(cert) & doesFileExist(key)) ){
            System.exit(1);
        }
    }

    private boolean doesFileExist(File file) {
        if ( !file.exists() ) {
            System.err.println(String.format("File does not exist: %s", file.toString()));
            return false;
        }
        return true;
    }

    private byte[] decodeBase64File(File file) throws FileNotFoundException, IOException {

        String fileContent = new String(new FileInputStream(file).readAllBytes());
        return Base64.getMimeDecoder().decode(
                fileContent.trim().replaceAll("-----(BEGIN|END).*-----\n{0,1}", ""));
    }

    private String encryptText(String msg)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(x509.getPublicKey().getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, x509.getPublicKey());
        return new String(Base64.getEncoder().encode(cipher.doFinal(msg.getBytes("UTF-8"))));
    }

    private String decryptText(String msg)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(x509.getPublicKey().getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(msg)), "UTF-8");
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new CertificateVerifier()).execute(args));
    }
}
