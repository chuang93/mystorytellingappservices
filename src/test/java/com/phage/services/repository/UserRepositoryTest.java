package com.phage.services.repository;

import com.phage.services.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
//Configure class with @DataJPATest annotation to not be autoconfigured, to stil run against the database.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest  //recreate and use all beans with @Repository stereotype or other persistence beans
public class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    //integration test to actual DB
    @Test
    public void testFindAll() {
        Iterable<User> users = userRepository.findAll();
        Assert.assertNotNull(users);
    }
}
