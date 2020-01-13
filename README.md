# CertificatePlugin

This IntelliJ plugin provides some help when dealing with pki certificates.

## Installation

To install the plugin manually:
 
 * Download a release from GitHub, or
 
 * Run `gradlew clean buildPlugin` which puts the built plugin in `build/distributions`
 
 * navigate to `IntelliJ IDEA/Preferences/Plugins`, click the gear-icon next to the `Market Place` and `Installed` tabs, select `Install Plugin from Disk...' and navigate to the plugin zip file you downloaded or built.
 
### Installing with an incompatible version of IntelliJ

IntelliJ checks whether the plugin is compatible with the running version by reading two values form `resources/META-INF/plugin.xml`.  Some versions of IntelliJ run under Java 8 by default and won't be able to use this plugin.
However, if your version of IntelliJ runs under Java 11 it may be able to use the plugin anyway.  If you get a message saying the plugin is incompatible when try to install it, you might be able to get it to work if you take the following steps before running the `./gradlew clean buildPlugin` step above:

* Note the build number of your version of IntelliJ found through the 'About IntelliJ IDEA' menu (you only need the first part; probably 191 or 192)

* Open `build.gradle` and alter the value of `sinceBuild '192.7142'` to `'[your build number].*'`.

* Follow the build and install instructions above.

Your results may vary but it definitely won't work if your version of IntelliJ is running under Java 8.  See the [JetBrains docs](https://intellij-support.jetbrains.com/hc/en-us/articles/206544879-Selecting-the-JDK-version-the-IDE-will-run-under) for instructions on how to change the JDK to Java 11 or later.
 
## Usage

### Certificate Decoding / Display

#### Project Explorer Panel

Right click on a file.  If the file is a certificate you'll be given the option to display it.  If it's not a valid certificate the menu won't show anything.

#### File Editor Window

* Right click within an editor window and the plugin will try the following strategies to find a certificate.
  
  - Decoding the entire file being edited.
  
  - Decoding the selected text (which would need to be the base64 encoded content that you'd find between -----BEGIN CERTIFICATE----- and -----END CERTIFICATE----- PEM delimiters)
  
  - Searching the file for PEM delimiters and decoding the lines in between. (The plugin searches for the first BEGIN tag it can find, first by scanning up from the caret positon and then down if none is found.  If it does find one, it will search down for the first END tag it can find.)
  
  If any of those results in a valid certificate, you'll be given the option to display the certificate.  If the certificate has expired or is not yet valid, this will be noted in square brackets in the menu.
  
### Certificate / Private Key Verification

The tools menu will present an option to `Verify Certificate/Private Key`.  Selecting this option will prompt you to open a certificate followed by a private key.  The plugin will then use the certificate to encrypt a string and then attempt to decrypt the encrypted string using the private key.  You'll be shown a dialog telling you the outcome of the decryption attempt.

