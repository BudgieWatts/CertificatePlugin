package dev.johnwatts.plugins.certificates.strategies;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.UserDataHolder;
import dev.johnwatts.plugins.certificates.shared.NoSourceException;
import dev.johnwatts.plugins.certificates.shared.Result;
import org.junit.Before;
import org.junit.Test;

import java.security.cert.X509Certificate;
import java.util.List;

import static org.junit.Assert.*;

public class FindingStrategyTest {

    private FindingStrategy strategy;

    @Before
    public void init() {
        strategy = new FindingStrategyMock();
    }

    @Test
    public void emptyContent() {
        List<X509Certificate> certificates = strategy.parsePEM("");
        assertNotNull(certificates);
        assertEquals(0, certificates.size());
    }

    @Test
    public void singleCertificate() {
        String pem = "-----BEGIN CERTIFICATE-----\n" +
                "MIIHJzCCBg+gAwIBAgISAxKoW0FC0kSFeD+0eoZdRqg6MA0GCSqGSIb3DQEBCwUA\n" +
                "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD\n" +
                "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0yMDAzMDMxNDUyMDFaFw0y\n" +
                "MDA2MDExNDUyMDFaMB4xHDAaBgNVBAMMEyouc3RhY2tleGNoYW5nZS5jb20wggEi\n" +
                "MA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCaMvgFv+EUfHw59M43xqsn4n9t\n" +
                "c2iKh6LGHvG9OaNShpmoLUWR4/bu6u0LzmqpMJSXg1542YzbGuK84O6yufm2gFrj\n" +
                "RRay+0K3yulXbYf6SkRrC1y0EmMXqRMu/YUMCd1Dx3hgxtHCt1Zh1J5yt+pkW2gP\n" +
                "0bRecwhtpe5JT+Hm14O9Thka5EyGETA6pWDp/jJA4b6NBIAooHp/N4WEKUbTk4wh\n" +
                "ofbPAL3clt8MlMijsEFtHkqGwFHDmnqMVePehn0fPfsNH4PvI/bzKqL/R4epzY7V\n" +
                "8jyEG4g0hmMVpl3DW+gEZSCI2XBN0jFFBDj6uT0EaXAZke9leRimY1An34ebAgMB\n" +
                "AAGjggQxMIIELTAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwEG\n" +
                "CCsGAQUFBwMCMAwGA1UdEwEB/wQCMAAwHQYDVR0OBBYEFPBhiLKPHese/2i8vXrQ\n" +
                "r5wMNAkYMB8GA1UdIwQYMBaAFKhKamMEfd265tE5t6ZFZe/zqOyhMG8GCCsGAQUF\n" +
                "BwEBBGMwYTAuBggrBgEFBQcwAYYiaHR0cDovL29jc3AuaW50LXgzLmxldHNlbmNy\n" +
                "eXB0Lm9yZzAvBggrBgEFBQcwAoYjaHR0cDovL2NlcnQuaW50LXgzLmxldHNlbmNy\n" +
                "eXB0Lm9yZy8wggHkBgNVHREEggHbMIIB14IPKi5hc2t1YnVudHUuY29tghIqLmJs\n" +
                "b2dvdmVyZmxvdy5jb22CEioubWF0aG92ZXJmbG93Lm5ldIIYKi5tZXRhLnN0YWNr\n" +
                "ZXhjaGFuZ2UuY29tghgqLm1ldGEuc3RhY2tvdmVyZmxvdy5jb22CESouc2VydmVy\n" +
                "ZmF1bHQuY29tgg0qLnNzdGF0aWMubmV0ghMqLnN0YWNrZXhjaGFuZ2UuY29tghMq\n" +
                "LnN0YWNrb3ZlcmZsb3cuY29tghUqLnN0YWNrb3ZlcmZsb3cuZW1haWyCDyouc3Vw\n" +
                "ZXJ1c2VyLmNvbYINYXNrdWJ1bnR1LmNvbYIQYmxvZ292ZXJmbG93LmNvbYIQbWF0\n" +
                "aG92ZXJmbG93Lm5ldIIUb3BlbmlkLnN0YWNrYXV0aC5jb22CD3NlcnZlcmZhdWx0\n" +
                "LmNvbYILc3N0YXRpYy5uZXSCDXN0YWNrYXBwcy5jb22CDXN0YWNrYXV0aC5jb22C\n" +
                "EXN0YWNrZXhjaGFuZ2UuY29tghJzdGFja292ZXJmbG93LmJsb2eCEXN0YWNrb3Zl\n" +
                "cmZsb3cuY29tghNzdGFja292ZXJmbG93LmVtYWlsghFzdGFja3NuaXBwZXRzLm5l\n" +
                "dIINc3VwZXJ1c2VyLmNvbTBMBgNVHSAERTBDMAgGBmeBDAECATA3BgsrBgEEAYLf\n" +
                "EwEBATAoMCYGCCsGAQUFBwIBFhpodHRwOi8vY3BzLmxldHNlbmNyeXB0Lm9yZzCC\n" +
                "AQUGCisGAQQB1nkCBAIEgfYEgfMA8QB2AOcS8rA3fhpi+47JDGGE8ep7N8tWHREm\n" +
                "W/Pg80vyQVRuAAABcKEYy1YAAAQDAEcwRQIhAM5JQxkkMowxAbatd3Dw6X+6yHC1\n" +
                "69nxqx+jD6TIKocNAiBY6NbYWe9ypgCB2tx95NwBLWGwYujGZiFqGgNGuaLx9gB3\n" +
                "ALIeBcyLos2KIE6HZvkruYolIGdr2vpw57JJUy3vi5BeAAABcKEYy1YAAAQDAEgw\n" +
                "RgIhALb+qYk+UyX01YSw+AOpW9/2H9NhoNKxmdMJsWFDZiPhAiEA5/Gj1O2D0xNE\n" +
                "gSiBdk94ouWm2HhyNU0S4V1683gJXb0wDQYJKoZIhvcNAQELBQADggEBAEPcdg3Z\n" +
                "ZXjPqNFgr0Vnw75X4Yi6uNms7Zsm3K8OZOo+Lyd7tdCMPUNc2jJcgauOHt1ihQlw\n" +
                "Hq2YpU8XqDLRWWlY9j0VShdjLZQHSqZumbu01yjCuwRUdyYrhdpmnA/sbE8ZU2ao\n" +
                "WZFEJi+QhNAgla/lw0A/Nli4lZfexQyRXHQiM1J39oKnIGK2IoG3WMjyABZqALar\n" +
                "zp5JTFmWZgeJfNscFStLxJxjlclmLqrB/7klVvV56zlUXEQV3QIiI0LTi0FQjJJX\n" +
                "lxAO+Ctl+mjuRothAdVlcLgzWfPJZ/qmJ/SEwo5zsVWTeW1SEG/UupQiZbXILzO4\n" +
                "YsqEjQRYCSSaWow=\n" +
                "-----END CERTIFICATE-----\n";

        List<X509Certificate> certificates = strategy.parsePEM(pem);
        assertNotNull(certificates);
        assertEquals(1, certificates.size());

        X509Certificate cert = certificates.get(0);
        assertNotNull(cert);
        assertEquals("CN=Let's Encrypt Authority X3, O=Let's Encrypt, C=US", cert.getIssuerDN().getName());
    }

    @Test
    public void certificateChain() {
        String pem = "-----BEGIN CERTIFICATE-----\n" +
                "MIIHJzCCBg+gAwIBAgISAxKoW0FC0kSFeD+0eoZdRqg6MA0GCSqGSIb3DQEBCwUA\n" +
                "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD\n" +
                "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0yMDAzMDMxNDUyMDFaFw0y\n" +
                "MDA2MDExNDUyMDFaMB4xHDAaBgNVBAMMEyouc3RhY2tleGNoYW5nZS5jb20wggEi\n" +
                "MA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCaMvgFv+EUfHw59M43xqsn4n9t\n" +
                "c2iKh6LGHvG9OaNShpmoLUWR4/bu6u0LzmqpMJSXg1542YzbGuK84O6yufm2gFrj\n" +
                "RRay+0K3yulXbYf6SkRrC1y0EmMXqRMu/YUMCd1Dx3hgxtHCt1Zh1J5yt+pkW2gP\n" +
                "0bRecwhtpe5JT+Hm14O9Thka5EyGETA6pWDp/jJA4b6NBIAooHp/N4WEKUbTk4wh\n" +
                "ofbPAL3clt8MlMijsEFtHkqGwFHDmnqMVePehn0fPfsNH4PvI/bzKqL/R4epzY7V\n" +
                "8jyEG4g0hmMVpl3DW+gEZSCI2XBN0jFFBDj6uT0EaXAZke9leRimY1An34ebAgMB\n" +
                "AAGjggQxMIIELTAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYBBQUHAwEG\n" +
                "CCsGAQUFBwMCMAwGA1UdEwEB/wQCMAAwHQYDVR0OBBYEFPBhiLKPHese/2i8vXrQ\n" +
                "r5wMNAkYMB8GA1UdIwQYMBaAFKhKamMEfd265tE5t6ZFZe/zqOyhMG8GCCsGAQUF\n" +
                "BwEBBGMwYTAuBggrBgEFBQcwAYYiaHR0cDovL29jc3AuaW50LXgzLmxldHNlbmNy\n" +
                "eXB0Lm9yZzAvBggrBgEFBQcwAoYjaHR0cDovL2NlcnQuaW50LXgzLmxldHNlbmNy\n" +
                "eXB0Lm9yZy8wggHkBgNVHREEggHbMIIB14IPKi5hc2t1YnVudHUuY29tghIqLmJs\n" +
                "b2dvdmVyZmxvdy5jb22CEioubWF0aG92ZXJmbG93Lm5ldIIYKi5tZXRhLnN0YWNr\n" +
                "ZXhjaGFuZ2UuY29tghgqLm1ldGEuc3RhY2tvdmVyZmxvdy5jb22CESouc2VydmVy\n" +
                "ZmF1bHQuY29tgg0qLnNzdGF0aWMubmV0ghMqLnN0YWNrZXhjaGFuZ2UuY29tghMq\n" +
                "LnN0YWNrb3ZlcmZsb3cuY29tghUqLnN0YWNrb3ZlcmZsb3cuZW1haWyCDyouc3Vw\n" +
                "ZXJ1c2VyLmNvbYINYXNrdWJ1bnR1LmNvbYIQYmxvZ292ZXJmbG93LmNvbYIQbWF0\n" +
                "aG92ZXJmbG93Lm5ldIIUb3BlbmlkLnN0YWNrYXV0aC5jb22CD3NlcnZlcmZhdWx0\n" +
                "LmNvbYILc3N0YXRpYy5uZXSCDXN0YWNrYXBwcy5jb22CDXN0YWNrYXV0aC5jb22C\n" +
                "EXN0YWNrZXhjaGFuZ2UuY29tghJzdGFja292ZXJmbG93LmJsb2eCEXN0YWNrb3Zl\n" +
                "cmZsb3cuY29tghNzdGFja292ZXJmbG93LmVtYWlsghFzdGFja3NuaXBwZXRzLm5l\n" +
                "dIINc3VwZXJ1c2VyLmNvbTBMBgNVHSAERTBDMAgGBmeBDAECATA3BgsrBgEEAYLf\n" +
                "EwEBATAoMCYGCCsGAQUFBwIBFhpodHRwOi8vY3BzLmxldHNlbmNyeXB0Lm9yZzCC\n" +
                "AQUGCisGAQQB1nkCBAIEgfYEgfMA8QB2AOcS8rA3fhpi+47JDGGE8ep7N8tWHREm\n" +
                "W/Pg80vyQVRuAAABcKEYy1YAAAQDAEcwRQIhAM5JQxkkMowxAbatd3Dw6X+6yHC1\n" +
                "69nxqx+jD6TIKocNAiBY6NbYWe9ypgCB2tx95NwBLWGwYujGZiFqGgNGuaLx9gB3\n" +
                "ALIeBcyLos2KIE6HZvkruYolIGdr2vpw57JJUy3vi5BeAAABcKEYy1YAAAQDAEgw\n" +
                "RgIhALb+qYk+UyX01YSw+AOpW9/2H9NhoNKxmdMJsWFDZiPhAiEA5/Gj1O2D0xNE\n" +
                "gSiBdk94ouWm2HhyNU0S4V1683gJXb0wDQYJKoZIhvcNAQELBQADggEBAEPcdg3Z\n" +
                "ZXjPqNFgr0Vnw75X4Yi6uNms7Zsm3K8OZOo+Lyd7tdCMPUNc2jJcgauOHt1ihQlw\n" +
                "Hq2YpU8XqDLRWWlY9j0VShdjLZQHSqZumbu01yjCuwRUdyYrhdpmnA/sbE8ZU2ao\n" +
                "WZFEJi+QhNAgla/lw0A/Nli4lZfexQyRXHQiM1J39oKnIGK2IoG3WMjyABZqALar\n" +
                "zp5JTFmWZgeJfNscFStLxJxjlclmLqrB/7klVvV56zlUXEQV3QIiI0LTi0FQjJJX\n" +
                "lxAO+Ctl+mjuRothAdVlcLgzWfPJZ/qmJ/SEwo5zsVWTeW1SEG/UupQiZbXILzO4\n" +
                "YsqEjQRYCSSaWow=\n" +
                "-----END CERTIFICATE-----\n" +
                " 1 s:C = US, O = Let's Encrypt, CN = Let's Encrypt Authority X3\n" +
                "   i:O = Digital Signature Trust Co., CN = DST Root CA X3\n" +
                "-----BEGIN CERTIFICATE-----\n" +
                "MIIEkjCCA3qgAwIBAgIQCgFBQgAAAVOFc2oLheynCDANBgkqhkiG9w0BAQsFADA/\n" +
                "MSQwIgYDVQQKExtEaWdpdGFsIFNpZ25hdHVyZSBUcnVzdCBDby4xFzAVBgNVBAMT\n" +
                "DkRTVCBSb290IENBIFgzMB4XDTE2MDMxNzE2NDA0NloXDTIxMDMxNzE2NDA0Nlow\n" +
                "SjELMAkGA1UEBhMCVVMxFjAUBgNVBAoTDUxldCdzIEVuY3J5cHQxIzAhBgNVBAMT\n" +
                "GkxldCdzIEVuY3J5cHQgQXV0aG9yaXR5IFgzMIIBIjANBgkqhkiG9w0BAQEFAAOC\n" +
                "AQ8AMIIBCgKCAQEAnNMM8FrlLke3cl03g7NoYzDq1zUmGSXhvb418XCSL7e4S0EF\n" +
                "q6meNQhY7LEqxGiHC6PjdeTm86dicbp5gWAf15Gan/PQeGdxyGkOlZHP/uaZ6WA8\n" +
                "SMx+yk13EiSdRxta67nsHjcAHJyse6cF6s5K671B5TaYucv9bTyWaN8jKkKQDIZ0\n" +
                "Z8h/pZq4UmEUEz9l6YKHy9v6Dlb2honzhT+Xhq+w3Brvaw2VFn3EK6BlspkENnWA\n" +
                "a6xK8xuQSXgvopZPKiAlKQTGdMDQMc2PMTiVFrqoM7hD8bEfwzB/onkxEz0tNvjj\n" +
                "/PIzark5McWvxI0NHWQWM6r6hCm21AvA2H3DkwIDAQABo4IBfTCCAXkwEgYDVR0T\n" +
                "AQH/BAgwBgEB/wIBADAOBgNVHQ8BAf8EBAMCAYYwfwYIKwYBBQUHAQEEczBxMDIG\n" +
                "CCsGAQUFBzABhiZodHRwOi8vaXNyZy50cnVzdGlkLm9jc3AuaWRlbnRydXN0LmNv\n" +
                "bTA7BggrBgEFBQcwAoYvaHR0cDovL2FwcHMuaWRlbnRydXN0LmNvbS9yb290cy9k\n" +
                "c3Ryb290Y2F4My5wN2MwHwYDVR0jBBgwFoAUxKexpHsscfrb4UuQdf/EFWCFiRAw\n" +
                "VAYDVR0gBE0wSzAIBgZngQwBAgEwPwYLKwYBBAGC3xMBAQEwMDAuBggrBgEFBQcC\n" +
                "ARYiaHR0cDovL2Nwcy5yb290LXgxLmxldHNlbmNyeXB0Lm9yZzA8BgNVHR8ENTAz\n" +
                "MDGgL6AthitodHRwOi8vY3JsLmlkZW50cnVzdC5jb20vRFNUUk9PVENBWDNDUkwu\n" +
                "Y3JsMB0GA1UdDgQWBBSoSmpjBH3duubRObemRWXv86jsoTANBgkqhkiG9w0BAQsF\n" +
                "AAOCAQEA3TPXEfNjWDjdGBX7CVW+dla5cEilaUcne8IkCJLxWh9KEik3JHRRHGJo\n" +
                "uM2VcGfl96S8TihRzZvoroed6ti6WqEBmtzw3Wodatg+VyOeph4EYpr/1wXKtx8/\n" +
                "wApIvJSwtmVi4MFU5aMqrSDE6ea73Mj2tcMyo5jMd6jmeWUHK8so/joWUoHOUgwu\n" +
                "X4Po1QYz+3dszkDqMp4fklxBwXRsW10KXzPMTZ+sOPAveyxindmjkW8lGy+QsRlG\n" +
                "PfZ+G6Z6h7mjem0Y+iWlkYcV4PIWL1iwBi8saCbGS5jN2p8M+X+Q7UNKEkROb3N6\n" +
                "KOqkqm57TH2H3eDJAkSnh6/DNFu0Qg==\n" +
                "-----END CERTIFICATE-----\n";
        List<X509Certificate> certificates = strategy.parsePEM(pem);
        assertNotNull(certificates);
        assertEquals(2, certificates.size());

        X509Certificate cert = certificates.get(0);
        assertNotNull(cert);
        assertEquals("CN=Let's Encrypt Authority X3, O=Let's Encrypt, C=US", cert.getIssuerDN().getName());

        cert = certificates.get(1);
        assertNotNull(cert);
        assertEquals("CN=DST Root CA X3, O=Digital Signature Trust Co.", cert.getIssuerDN().getName());
    }
}

class FindingStrategyMock extends FindingStrategy {

    @Override
    protected UserDataHolder getSource(AnActionEvent event) throws NoSourceException {
        return null;
    }

    @Override
    public Result find(AnActionEvent source) {
        return null;
    }
}
