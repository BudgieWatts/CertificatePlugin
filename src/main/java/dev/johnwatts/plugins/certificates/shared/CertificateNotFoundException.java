package dev.johnwatts.plugins.certificates.shared;

public class CertificateNotFoundException extends Exception {
    CertificateNotFoundException(String message) {
        super(message);
    }
}
