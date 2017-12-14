The intent of this app is to be able to decrypt some encrypted data.

The decryption key is held in projectId = zoretto-keystore

Encryption issue is this.

```
curl -s -X POST "https://cloudkms.googleapis.com/v1/projects/zoretto-keystore/locations/global/keyRings/money/cryptoKeys/money_aes_key:encrypt" -d "{\"plaintext\":\"U29tZSB0ZXh0IHRvIGJlIGVuY3J5cHRlZA==\"}" -H "Authorization:Bearer $(gcloud auth application-default print-access-token)"   -H "Content-Type:application/json"
{
  "error": {
    "code": 403,
    "message": "Permission 'cloudkms.cryptoKeyVersions.useToEncrypt' denied for resource 'projects/zoretto-keystore/locations/global/keyRings/money/cryptoKeys/money_aes_key'.",
    "status": "PERMISSION_DENIED"
  }
}


```

