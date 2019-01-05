package com.phage.services.resource;

import com.phage.services.domain.LocalUser;
import com.phage.services.repository.service.LocalUserRepositoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;

import static com.phage.services.resource.ResourceUtility.getResponseOkJson;

@RequestMapping(value="user/")
@RestController
public class LocalUserResource {
    private LocalUserRepositoryService localUserRepositoryService;

    public static final Logger logger = LogManager.getLogger(LocalUserResource.class);
    //AUTOWIRED MEANS TO INJECT AN INSTANCE OF A CLASS WITHOUT EXPLICITLY INSTANTIATING IT.
    @Autowired
    public LocalUserResource(LocalUserRepositoryService localUserRepositoryService){
        this.localUserRepositoryService = localUserRepositoryService;
    }
    @PostMapping(value = "/signup")
    public ResponseEntity<LocalUser> signup(@Nonnull String lastName, @Nonnull String firstName, @Nonnull String email, @Nonnull String username, @Nonnull String password) throws Exception{
        //TODO: DOESNT SEEM TO EVALUTATE TO TRUE NO MATTER WHAT
        if(logger.isDebugEnabled()){
            logger.debug("This is Debug Mode");
        }
        LocalUser existingUserEmail = localUserRepositoryService.findByEmail(email);
        LocalUser existingUserUsername = localUserRepositoryService.findByUsername(username);
        //TODO::CHANGE INVALID USER CONDITION TO IMPLEMENT CORRECT LOGIC
        boolean invalidUser = false;
        if(invalidUser)
        {
            logger.debug(String.format("Invalid User from email:  " + email));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else {
            if (existingUserEmail != null || existingUserUsername != null) {
                String existError = String.format("Username %s or Email %s already exists in the database", username, email);
                logger.debug(existError);
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                LocalUser newLocalUser = localUserRepositoryService.getNewLocalUser(lastName, firstName, email, username, password);
                try{
                    localUserRepositoryService.saveUser(newLocalUser);
                    return getResponseOkJson(newLocalUser);
                }catch(Exception e){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
    }

    @GetMapping( value = "/{email}")
    public ResponseEntity<LocalUser> getUser(@PathVariable @Nonnull String email){
        LocalUser localUser = localUserRepositoryService.findByEmail(email);
        return getResponseOkJson(localUser);
    }

    @GetMapping( value = "/all")
    public  ResponseEntity<Iterable<LocalUser>> getAllUsers(){
        return getResponseOkJson(localUserRepositoryService.findAll());
    }

}
