package dev.johnwatts.plugins.certificates;

public class CertificateNotFoundException extends Exception {
    CertificateNotFoundException(String message) {
        super(message);
    }
}
