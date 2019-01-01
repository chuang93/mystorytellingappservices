package com.phage.services.repository;

import com.phage.services.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    //automatic CRUD WRAPPER over common db operations. SECOND USER PROPERTY IS THE TYPE OF THE 'ID' PRIMARY KEY'
    User findByToken(String token);
    User findByEmail(String email);
}
