package com.phage.services.resource;

import com.phage.services.domain.User;
import com.phage.services.repository.service.UserRepositoryService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

//Controller or RestController annotation required for this @Component stereotype that uses @RequestMapping or @GetMapping for example.
@RestController
public class LoginResource{

    private UserRepositoryService userRepositoryService;

    public static final Logger logger = LogManager.getLogger(LoginResource.class);
    //TODO: MOVE CLIENT_ID OUT OF THE CONTROLLER LAYER
    private static final String CLIENT_ID = "474339377424-mufjbd5juv939iukdqpa09m15uudmd3b.apps.googleusercontent.com";

    //AUTOWIRED MEANS TO INJECT AN INSTANCE OF A CLASS WITHOUT EXPLICITLY INSTANTIATING IT.
    @Autowired
    public LoginResource(UserRepositoryService userRepositoryService){
            this.userRepositoryService = userRepositoryService;
    }

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
    @ResponseBody //makes it so that the response returns a body with an automatically serialized json object
    public  Iterable<User> getAllUsers(){
        return userRepositoryService.findAll();
    }

    private boolean validateUserRepository(String token, String email, String firstName, String lastName){
        //TODO: FIX LOGGER TO GET THIS TO SHOW UP
        logger.debug("Checking for existing user: " + firstName + " " + lastName);
        User existingUser = userRepositoryService.findByToken(token);
        if (existingUser != null){
          logger.debug(String.format("User %s %s, email %s with ID Token: %s already exists in Database.", firstName, lastName, email, token));
        }
        else{
            User user = new User();
            user.setToken(token);
            user.setEmail(email);
            user.setLastName(lastName);
            user.setFirstName(firstName);
            user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
            userRepositoryService.saveUser(user);
        }
        return true;
    }
}
