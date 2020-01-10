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

Right click on a file and select `Decode Certificate`.  If the file is a valid certificate you will see a dialog displaying some of the contents of the certificate.  If it's not a valid certificate, you'll see a messages saying 'No valid certificate found.'

#### File Editor Window

* Right click within an editor window and select 'Decode Certificate'. The plugin will try the following strategies to find a certificate.
  
  - Decoding the entire file being edited.
  
  - Decoding the selected text (which would need to be the base64 encoded content that you'd find between -----BEGIN CERTIFICATE----- and -----END CERTIFICATE----- PEM delimiters)
  
  - Searching the file for PEM delimiters and decoding the lines in between. (The plugin searches for the first BEGIN tag it can find, first by scanning up from the caret positon and then down if none is found.  If it does find one, it will search down for the first END tag it can find.)
  
  If any of those results in a valid certificate, that certificate will be displayed and no further searching will be done.  If none of those strategies results in a certificate, you'll see a messages saying 'No valid certificate found.
  
### Certificate / Private Key Verification

Right clicking in the project explorer or editor window will present an option to `Verify Certificate/Private Key`.  Selecting this option will prompt you to open a certificate followed by a private key.  The plugin will then use the certificate to encrypt a string and then attempt to decrypt the encrypted string using the private key.  You'll be shown a dialog telling you the outcome of the decryption attempt.

