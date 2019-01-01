package com.phage.services.repository.Service;

import com.phage.services.domain.User;
import com.phage.services.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//Service class with the actual repository operations from the User DB repository, do not use direct Repository Classes in the controller layer.
//@Component Annotation is superset of Repository, Service, and Controller but for some reason @Service doesnt work
@Component
public class UserRepositoryService {

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //here the wrapper seems simple but later we will add to this repositoryService Layer more complex queries,
    //which can be reused across all of the
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByToken( String token) {
        return userRepository.findByToken(token);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Iterable<User> findAll(){
        return userRepository.findAll();
    }
}
