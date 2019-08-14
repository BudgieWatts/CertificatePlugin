package dev.johnwatts.plugins.certificates;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.function.Function;

public class CertificateStringFactory {
    public enum Field {
        ISSUER(cert -> {return splitPrincipal(cert.getIssuerX500Principal().toString());}),
        SUBJECT(cert -> {return splitPrincipal(cert.getSubjectX500Principal().toString());}),
        NOT_BEFORE(cert -> {return cert.getNotBefore().toString();}),
        NOT_AFTER(cert -> {return cert.getNotAfter().toString();}),
        PUBLIC_KEY(cert -> {return cert.getPublicKey().toString();}),
        ISSUER_DN(cert -> {return cert.getIssuerDN().toString();}),
        SIGNING_ALGO_NAME(cert -> {return cert.getSigAlgName();}),
        TYPE(cert -> {return cert.getType();}),
        SUBJECT_DN(cert -> {return cert.getSubjectDN().toString();});
        // TODO Other properties here

        private Function<X509Certificate, String> outputFunction;

        Field(Function<X509Certificate, String> outputFunction) {
            this.outputFunction = outputFunction;
        }
    }

    public static String output(X509Certificate cert, Field ... fields) {
        StringBuilder stringBuilder = new StringBuilder();

        Arrays.asList(fields).forEach(f -> stringBuilder.append(f.outputFunction.apply(cert)).append("\n\n"));

        return stringBuilder.toString();
    }

    // TODO get this to work
    private static String splitPrincipal(String principal) {
        return principal.replaceAll("([A-Z]{1,2}=.*),{0,1}", "$1\n");
    }
}
