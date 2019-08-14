package dev.johnwatts.plugins.certificates;

import java.security.cert.X509Certificate;
import java.util.Optional;

public interface CertificateFindingStrategy<T> {
    Optional<X509Certificate> find(T source) throws CertificateNotFoundException;
}
