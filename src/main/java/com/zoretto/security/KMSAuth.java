package com.zoretto.security;


// Imports the Google Cloud client library

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.cloudkms.v1.CloudKMS;
import com.google.api.services.cloudkms.v1.CloudKMSScopes;
import com.google.api.services.cloudkms.v1.model.KeyRing;
import com.google.api.services.cloudkms.v1.model.ListKeyRingsResponse;

import java.io.IOException;

public class KMSAuth {

    private static final CloudKMS kms = createAuthorizedClient();

    public static CloudKMS getKms(){
        return kms;
    }

    private static CloudKMS createAuthorizedClient()  {
        // Create the credential
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        // Authorize the client using Application Default Credentials
        // @see https://g.co/dv/identity/protocols/application-default-credentials
        GoogleCredential credential = null;
        try {
            credential = GoogleCredential.getApplicationDefault(transport, jsonFactory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Depending on the environment that provides the default credentials (e.g. Compute Engine, App
        // Engine), the credentials may require us to specify the scopes we need explicitly.
        // Check for this case, and inject the scope if required.
        if (credential.createScopedRequired()) {
            credential = credential.createScoped(CloudKMSScopes.all());
        }

        return new CloudKMS.Builder(transport, jsonFactory, credential)
                .setApplicationName("CloudKMS snippets")
                .build();
    }

    public static void printAllKeys() throws Exception {

        // The resource name of the cryptoKey
        String keyRingPath = "projects/zoretto-keystore/locations/global";

        // Make the RPC call
        ListKeyRingsResponse response = kms.projects()
                                            .locations()
                                            .keyRings()
                                            .list(keyRingPath)
                                            .execute();

        // Print the returned key rings
        if (null != response.getKeyRings()) {
            System.out.println("Key Rings: ");
            for (KeyRing keyRing : response.getKeyRings()) {
                System.out.println(keyRing.getName());
            }
        }
        else {
            System.out.println("No key rings defined.");
        }
    }
}
