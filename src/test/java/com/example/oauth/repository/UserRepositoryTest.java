package com.example.oauth.repository;

import com.example.oauth.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository ur;

    @Test
    void testFindByEmail() {
        User user = new User(null, "test@example.com", "Test User", "USER", "google", "testId");
        ur.save(user);

        Optional<User> found = ur.findByEmail("test@example.com");
        assertTrue(found.isPresent());
        assertEquals("Test User", found.get().getName());
    }

    @Test
    void testFindByProviderAndProviderId() {
        User user = new User(null, "test@example.com", "Test User", "USER", "google", "testId");
        ur.save(user);

        Optional<User> found = ur.findByProviderAndProviderId("google", "testId");
        assertTrue(found.isPresent());
        assertEquals("test@example.com", found.get().getEmail());
    }
}
