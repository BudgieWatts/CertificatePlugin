package dev.johnwatts.plugins.certificates;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.function.Function;

public class CertificateStringFactory {
    public enum Field {
        ISSUER(cert -> {return cert.getIssuerX500Principal().toString() + "\n\n";}),
        SUBJECT(cert -> {return cert.getSubjectX500Principal().toString() + "\n\n";}),
        NOT_BEFORE(cert -> {return cert.getNotBefore().toString() + "\n\n";}),
        NOT_AFTER(cert -> {return cert.getNotAfter().toString() + "\n\n";}),
        PUBLIC_KEY(cert -> {return cert.getPublicKey().toString() + "\n\n";});

        private Function<X509Certificate, String> outputFunction;

        private Field(Function<X509Certificate, String> outputFunction) {
            this.outputFunction = outputFunction;
        }
    }
    public static String output(X509Certificate cert, Field ... fields) {
        StringBuilder stringBuilder = new StringBuilder();

        Arrays.asList(fields).forEach(f -> stringBuilder.append(f.outputFunction.apply(cert)));

        return stringBuilder.toString();
    }
}
