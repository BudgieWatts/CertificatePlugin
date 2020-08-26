package dev.johnwatts.plugins.certificates.shared;

import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;

public class Result {
    List<X509Certificate> certificates;
    String message;

    public Result() {
        this.certificates = Collections.emptyList();
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

    public List<X509Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<X509Certificate> certificates) {
        this.certificates = certificates;
    }

    public boolean isSuccessful() {
        return !certificates.isEmpty();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
