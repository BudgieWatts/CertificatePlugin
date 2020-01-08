package dev.johnwatts.plugins.certificates.dialogs;

import org.bouncycastle.asn1.x500.style.BCStyle;

public class PirateBCStyle extends BCStyle {
    /* Yes, this is positively evil but I wanted to use a thing, but couldn't instantiate BCStyle to use it so I've
       done this terrible thing instead.  The other option was some wholesale copying and pasting of the bits I wanted.
       Pirate because it feels a bit like old fashioned music piracy involving cassette tapes. */
}
