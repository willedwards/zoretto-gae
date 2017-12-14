package com.zoretto;

import com.google.api.services.cloudkms.v1.CloudKMS;
import com.zoretto.security.KMSAuth;
import com.zoretto.security.KMSCipher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class ApplicationServlet extends HttpServlet {

    private static final Logger log = java.util.logging.Logger.getLogger(ApplicationServlet.class.getName());

    KMSCipher kmsCipher;

    private void getKey(){
        if(kmsCipher !=null ){
            return;
        }

        try {
            log.info("kms started");
            CloudKMS kms = KMSAuth.getKms();
            log.info("kms done");
            kmsCipher = new KMSCipher(kms);
        } catch (Exception e) {
            log.warning(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        getKey();

        String operation = (String)req.getAttribute("op");
        String text = (String)req.getAttribute("val");

        if(operation != null){

            String result ="";
            if(operation.equals("enc")){
                result = performEncryption(text);
            }
            else if(operation.equals("dec")){
                result = performDencryption(text);
            }

            log.info("result = " + result);
            resp.setContentType("text/plain");
            resp.getWriter().println("result = " + result);
        }

    }

    private String performDencryption(String cipher) {
        try {
            byte[] plainBytes = kmsCipher.decrypt(cipher.getBytes());
            return plainBytes.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String performEncryption(String plainText){

        log.info("encrypting started");
        byte[] cipher = new byte[0];
        try {
            cipher = kmsCipher.encrypt(plainText.getBytes());
            return cipher.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
