package dev.johnwatts.plugins.certificates.shared;

import java.security.cert.X509Certificate;
import java.util.Optional;

public class Result {
    Optional<X509Certificate> certificate;
    String message;

    public Result() {
        this.certificate = Optional.empty();
    }

    public static Result exceptionThrown(Exception e) {
        Result result = new Result();
        result.setMessage(e.getMessage());
        return result;
    }

    public static Result noCertificateFound() {
        Result result = new Result();
        result.setMessage("No valid certificate found.");
        return result;
    }

    public Optional<X509Certificate> getCertificate() {
        return certificate;
    }

    public void setCertificate(Optional<X509Certificate> certificate) {
        this.certificate = certificate;
    }

    public boolean isSuccessful() {
        return certificate.isPresent();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
