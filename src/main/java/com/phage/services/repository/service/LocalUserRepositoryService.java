package com.phage.services.repository.service;

import com.phage.services.domain.LocalUser;
import com.phage.services.repository.LocalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class LocalUserRepositoryService {

    private LocalUserRepository localUserRepository;

    @Autowired
    public LocalUserRepositoryService(LocalUserRepository localUserRepository){
        this.localUserRepository = localUserRepository;
    }

    public LocalUser findByEmail(String email){ return localUserRepository.findByEmail(email);}

    public LocalUser findByUsername(String username){ return localUserRepository.findByUsername(username);}

    public LocalUser findByName(String lastName, String firstName){ return localUserRepository.findByLastNameAndFirstName(lastName, firstName);}

    public void saveUser(LocalUser user){ localUserRepository.save(user);}

    public LocalUser getNewLocalUser(String lastName, String firstName, String email, String username, String password){
        LocalUser user = new LocalUser();
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setCreateTime(Timestamp.valueOf(LocalDateTime.now()));
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    public Iterable<LocalUser> findAll(){return localUserRepository.findAll();}

}
