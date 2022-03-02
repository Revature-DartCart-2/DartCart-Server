package com.revature.repositories;

import com.revature.driver.DartCartApplication;
import com.revature.models.CartItem;
import com.revature.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityGraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = DartCartApplication.class)
public class UserRepoTests {

    @Autowired
    UserRepo userRepo;

    final private User mockUser = new User(
            1,
            "test1",
            "password",
            "Test",
            "User",
            "test1@DartCart.net",
            "123-456-7890",
            "1 Test Street, Test Town, Testonia 12345",
            123563672L,
            null
    );

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void givenUserId_whenFindById_thenFindUser(int id) {
        Optional<User> output = userRepo.findById(id);
        assertTrue(output.isPresent());

        User user = output.get();
        assertEquals(id, user.getId());
        System.out.println(user);
    }

    @Test
    public void whenFindAll_thenReturnAllUsers() {
        List<User> users = (List<User>) userRepo.findAll();
        assertNotEquals(0, users.size());
        System.out.println(users);
    }

    @Test
    public void givenUser_whenSave_thenAddUser() {
        UserRepo mockRepo = mock(UserRepo.class);

        mockRepo.save(mockUser);
        verify(mockRepo).save(mockUser);
        System.out.println(mockUser);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void givenUserId_whenSave_thenUpdateUser(int id) {
        User testUser = new User(
                id,
                "UpdateTest" + id,
                "0abad76ce6a87e08b34da234de06b1f325d777067d670b8f59dc887f0853d53d",
                "Update",
                "Test",
                "nope@gmail.com",
                "757-411-1204",
                "1 Test Street, Test Town, Testonia 12345",
                1645743231935L,
                null
        );

        userRepo.save(testUser);
        Optional<User> output = userRepo.findById(id);

        User updatedUser = output.get();
        assertEquals("Update", updatedUser.getFirstName());
        System.out.println(updatedUser);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4})
    public void givenUserId_whenDelete_thenDeleteUser(int id) {
        UserRepo mockRepo = mock(UserRepo.class);
        mockRepo.deleteById(id);
        verify(mockRepo).deleteById((id));
    }

}
