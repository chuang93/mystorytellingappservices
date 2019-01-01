package com.phage.services.resources;

import com.phage.services.domain.User;
import com.phage.services.repository.UserRepository;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

@RestController
public class LoginResource{

    @Autowired
    private UserRepository userRepository;

    public static final Logger logger = LogManager.getLogger(LoginResource.class);

    private static String CLIENT_ID = "474339377424-mufjbd5juv939iukdqpa09m15uudmd3b.apps.googleusercontent.com";

    @PostMapping(value = "/verify")
    public String verifyToken(String idtoken) throws GeneralSecurityException, IOException {
        //TODO: DOESNT SEEM TO EVALUTATE TO TRUE NO MATTER WHAT
        if(logger.isDebugEnabled()){
            logger.debug("This is Debug Mode");
        }
        //TO DO:RESEARCH WHY IT IS RECOMENDED AND HOW TO MAKE TRANSPORT GLOBAL
        HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        GoogleIdToken idToken = verifier.verify(idtoken);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            if (emailVerified) {
                validateUserRepository(userId, email, givenName, familyName);
                return "Email Verified";
            } else {
                return email + " unable to be verified against retrieved ID Token.";
            }
        }
        return "Error, issue with id token sent: " + idtoken;
    }
    @GetMapping( value = "/users/all")
    @ResponseBody //makes it so that the reponse returns a body with an automatically serialized json object
    public  Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }
    private boolean validateUserRepository(String userId, String email, String firstName, String lastName){
        //TODO: FIX LOGGER TO GET THIS TO SHOW UP
        logger.debug("Checking for existing user: " + firstName + " " + lastName);
        //TODO: ADD VALIDATION LOGIC AND HQL LAYER: IF USER ID EXISTS BUT DOES NOT HAVE CORRECT NAME/EMAIL THEN REJECT LOGIN.
        //TODO: ELSE IF EXISTS OR NEED TO CREATE NEW USER THEN ACCEPT LOGIN.
        User user = new User();
        user.setId(userId);
        user.setEmail_address(email);
        user.setLast_name(lastName);
        user.setFirst_name(firstName);
        user.setCreate_Time(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
        return false;
    }
}
