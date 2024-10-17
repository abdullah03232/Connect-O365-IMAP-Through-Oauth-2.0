package com.o365.O365.IMAP.Application.connect;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;

public class Office365IMAPOAuth {
    // Replace these with your Azure AD App credentials
    private static final String CLIENT_ID = "********************";
    private static final String CLIENT_SECRET = "************************";
    private static final String TENANT_ID = "************************";
    private static final String EMAIL = "****";
    
    // Scope for Office 365 IMAP
    private static final String SCOPE = "https://outlook.office365.com/.default";

    public static void main(String[] args) {
        try {
            // Step 1: Get the OAuth2 access token
            String accessToken = getAccessToken();
            System.out.println("Token-"+accessToken);

            // Step 2: Connect to IMAP using the access token
         //   connectToIMAPWithOAuth2(EMAIL, accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Step 1: Get the OAuth2 access token
    public static String getAccessToken() throws Exception {
        ConfidentialClientApplication app = ConfidentialClientApplication.builder(
                CLIENT_ID, ClientCredentialFactory.createFromSecret(CLIENT_SECRET))
                .authority("https://login.microsoftonline.com/" + TENANT_ID)
                .build();

        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                Set.of(SCOPE))
                .build();

        CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
        IAuthenticationResult result = future.get();
        return result.accessToken();
    }

    // Step 2: Connect to IMAP using the access token
    public static void connectToIMAPWithOAuth2(String email, String accessToken) throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.put("mail.imap.ssl.enable", "true");
        properties.put("mail.imap.auth.mechanisms", "XOAUTH2");
        properties.put("mail.imap.starttls.enable", "true");

        Session session = Session.getInstance(properties);
        Store store = session.getStore("imap");
        store.connect("outlook.office365.com", 993, email, accessToken);

        // Access inbox
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        
        // Print message count
        System.out.println("Total Messages: " + inbox.getMessageCount());
        System.out.println("Unread Messages: " + inbox.getUnreadMessageCount());

        inbox.close(false);
        store.close();
    }

}
