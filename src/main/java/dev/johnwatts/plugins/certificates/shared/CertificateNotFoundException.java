package dev.johnwatts.plugins.certificates.shared;

public class CertificateNotFoundException extends Exception {
    public CertificateNotFoundException(String message) {
        super(message);
    }
}
