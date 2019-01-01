package com.phage.services.repository;

import com.phage.services.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    //automatic CRUD WRAPPER over common db operations
}
