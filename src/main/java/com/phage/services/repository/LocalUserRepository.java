package com.phage.services.repository;

import com.phage.services.domain.LocalUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalUserRepository extends CrudRepository<LocalUser, Integer> {
    LocalUser findByEmail(String email);
    LocalUser findByUsername(String username);
    LocalUser findByLastNameAndFirstName(String lastName, String firstName);
}