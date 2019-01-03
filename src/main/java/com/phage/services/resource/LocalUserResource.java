package com.phage.services.resource;

import com.phage.services.domain.LocalUser;
import com.phage.services.repository.service.LocalUserRepositoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.phage.services.resource.ResourceUtility.getResponseOkJson;

@RequestMapping(value="user/local")
@RestController
public class LocalUserResource {
    private LocalUserRepositoryService localUserRepositoryService;

    public static final Logger logger = LogManager.getLogger(LocalUserResource.class);
    //AUTOWIRED MEANS TO INJECT AN INSTANCE OF A CLASS WITHOUT EXPLICITLY INSTANTIATING IT.
    @Autowired
    public LocalUserResource(LocalUserRepositoryService localUserRepositoryService){
        this.localUserRepositoryService = localUserRepositoryService;
    }
    @PostMapping(value = "/persist")
    public ResponseEntity<LocalUser> persist(String lastName, String firstName, String email, String userName, String password) throws Exception{
        //TODO: DOESNT SEEM TO EVALUTATE TO TRUE NO MATTER WHAT
        if(logger.isDebugEnabled()){
            logger.debug("This is Debug Mode");
        }
        LocalUser localUser = validateUserRepository(lastName, firstName, email, userName, password);
        return getResponseOkJson(localUser);
    }

    @GetMapping( value = "/all")
    @ResponseBody //makes it so that the response returns a body with an automatically serialized json object
    public  Iterable<LocalUser> getAllUsers(){
        return localUserRepositoryService.findAll();
    }

    private LocalUser validateUserRepository(String lastName, String firstName, String email, String userName, String password) throws Exception{
        LocalUser existingUser = localUserRepositoryService.findByEmail(email);
        boolean invalidUser = false;
        //TODO::CHANGE INVALID USER CONDITION TO IMPLEMENT CORRECT LOGIC
        if(invalidUser)
        {
            logger.debug(String.format("Invalid User from email:  " + email));
            throw new Exception("Invalid User");
        }
        else {
            if (existingUser != null) {
                logger.debug(String.format("User %s %s, email %s already exists in Database.", firstName, lastName, email));
                return existingUser;
            } else {
                LocalUser newLocalUser = localUserRepositoryService.getNewLocalUser(lastName, firstName, email, userName, password);
                return newLocalUser;
            }
        }
    }
}
