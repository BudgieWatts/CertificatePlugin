package dev.johnwatts.plugins.certificates.strategies;

import dev.johnwatts.plugins.certificates.shared.Result;

public interface CertificateFindingStrategy<T> {
    Result find(T source);
}
