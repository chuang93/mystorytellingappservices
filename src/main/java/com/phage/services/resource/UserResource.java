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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

//Controller or RestController annotation required for this @Component stereotype that uses @RequestMapping or @GetMapping for example.
@RestController
public class UserResource {

    private UserRepositoryService userRepositoryService;

    public static final Logger logger = LogManager.getLogger(UserResource.class);
    //TODO: MOVE CLIENT_ID OUT OF THE CONTROLLER LAYER
    private static final String CLIENT_ID = "474339377424-mufjbd5juv939iukdqpa09m15uudmd3b.apps.googleusercontent.com";

    //AUTOWIRED MEANS TO INJECT AN INSTANCE OF A CLASS WITHOUT EXPLICITLY INSTANTIATING IT.
    @Autowired
    public UserResource(UserRepositoryService userRepositoryService){
            this.userRepositoryService = userRepositoryService;
    }
    @PostMapping(value = "/verify")
    public ResponseEntity<User> login(String idtoken) throws Exception{
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
                try{
                    User user = validateUserRepository(userId, email, givenName, familyName);
                    return getResponseOkJson(user);
                }catch(Exception e){
                    logger.debug("internal server error in validate user repository layer");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                logger.debug(email + " unable to be verified against retrieved ID Token.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }
        logger.debug(String.format("Error, issue with id token sent: %s", idtoken));
        return ResponseEntity.badRequest().body(null);
    }

    @GetMapping( value = "/users/all")
    @ResponseBody //makes it so that the response returns a body with an automatically serialized json object
    public  Iterable<User> getAllUsers(){
        return userRepositoryService.findAll();
    }

    private User validateUserRepository(String token, String email, String firstName, String lastName) throws Exception{
        //TODO: FIX LOGGER TO GET THIS TO SHOW UP
        logger.debug("Checking for existing user: " + firstName + " " + lastName);
        User existingUser = userRepositoryService.findByToken(token);
        //TODO::REFINE THIS VALIDATION CONDTION
        boolean invalidUser = false;
        if(invalidUser)
        {
            logger.debug(String.format("Invalid User from ID Token: %s", token));
            throw new Exception("Invalid User");
        }
        else {
            if (existingUser != null) {
                logger.debug(String.format("User %s %s, email %s with ID Token: %s already exists in Database.", firstName, lastName, email, token));
                return existingUser;
            } else {
                User user = new User();
                user.setToken(token);
                user.setEmail(email);
                user.setLastName(lastName);
                user.setFirstName(firstName);
                user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
                userRepositoryService.saveUser(user);
                return user;
            }
        }
    }
    private ResponseEntity getResponseOkJson(Object o){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");
        return ResponseEntity.ok().headers(headers).body(o);
    }

}
