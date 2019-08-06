package dev.johnwatts.plugins.certificates;

import picocli.CommandLine;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
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
        byte[] certBytes = new FileInputStream(cert).readAllBytes();
        byte decodedCert[] = Base64.getMimeDecoder().decode(certBytes);
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        x509 =  (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(decodedCert));

        byte[] keyBytes = new FileInputStream(key).readAllBytes();
        byte decodedKey[] = Base64.getMimeDecoder().decode(keyBytes);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory kf = KeyFactory.getInstance(x509.getPublicKey().getAlgorithm());
        privateKey = kf.generatePrivate(spec);

        String plainTextMessage = "I am a spanner";

        String encryptedMessage = encryptText(plainTextMessage);

        String decryptedMessage = decryptText(encryptedMessage);

        System.out.println(plainTextMessage);
        System.out.println(encryptedMessage);
        System.out.println(decryptedMessage);
        assert(decryptedMessage.equals(plainTextMessage));
        return 0;
    }

    public String encryptText(String msg)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(x509.getPublicKey().getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, x509.getPublicKey());
        return new String(Base64.getEncoder().encode(cipher.doFinal(msg.getBytes("UTF-8"))));
    }

    public String decryptText(String msg)
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
