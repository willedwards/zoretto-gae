package com.zoretto.security;

import com.google.api.services.cloudkms.v1.CloudKMS;
import com.google.api.services.cloudkms.v1.model.DecryptRequest;
import com.google.api.services.cloudkms.v1.model.DecryptResponse;
import com.google.api.services.cloudkms.v1.model.EncryptRequest;
import com.google.api.services.cloudkms.v1.model.EncryptResponse;

import java.io.IOException;

public class KMSCipher {

    static final String cryptoKeyName =  "projects/zoretto-keystore/locations/global/keyRings/money/cryptoKeys/money-aes-key";

    final CloudKMS cloudKMS;
    public KMSCipher(CloudKMS cloudKMS){
        this.cloudKMS = cloudKMS;
    }
    public  byte[] encrypt(byte[] plaintext) throws IOException {

        // Create the Cloud KMS client.

        EncryptRequest request = new EncryptRequest().encodePlaintext(plaintext);
        EncryptResponse response = cloudKMS.projects().locations().keyRings().cryptoKeys()
                .encrypt(cryptoKeyName, request)
                .execute();

        return response.decodeCiphertext();
    }


    /**
     * Decrypts the provided ciphertext with the specified security key.
     */
    public  byte[] decrypt(byte[] ciphertext) throws IOException {

        // The resource name of the cryptoKey
        DecryptRequest request = new DecryptRequest().encodeCiphertext(ciphertext);
        DecryptResponse response = cloudKMS.projects().locations().keyRings().cryptoKeys()
                .decrypt(cryptoKeyName, request)
                .execute();

        return response.decodePlaintext();
    }
}
