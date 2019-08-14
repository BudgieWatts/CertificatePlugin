package dev.johnwatts.plugins.certificates;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Optional;

public class Base64X509Decoder {
    public static Optional<X509Certificate> decode(String encodedCert) {
        try {
            return decode(encodedCert.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return Optional.empty();
        }
    }

    public static Optional<X509Certificate> decode(byte[] encodedCert) {
        byte decodedCert[] = Base64.getMimeDecoder().decode(encodedCert);
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            return Optional.of(
                    (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(decodedCert)));
        } catch (CertificateException e) {
            return Optional.empty();
        }

    }
}
