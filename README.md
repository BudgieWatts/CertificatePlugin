# CertificatePlugin

This IntelliJ plugin provides some help when dealing with pki certificates.

## Installation

To install the plugin manually:
 
 * Download a release from GitHub, or
 
 * Run `gradlew buildPlugin` which puts the built plugin in `build/distributions`
 
 * navigate to `IntelliJ IDEA/Preferences/Plugins`, click the gear-icon next to the `Market Place` and `Installed` tabs, select `Install Plugin from Disk...' and navigate to the plugin zip file you downloaded or built.
 
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

