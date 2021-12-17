package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository = mock(CartRepository.class);

    private final BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPath(){
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("test", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void createUserPasswordNotEqualToConfirmPassword() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("notThisPassword");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void createUserPasswordNotSevenCharacters() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("short");
        createUserRequest.setConfirmPassword("short");

        final ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void findByUserNameHappyPath() {
        final long id = 1;
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(id);
        when(userRepository.findByUsername("test")).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName("test");

        assertEquals(200, response.getStatusCodeValue());

        User userActual = response.getBody();
        assertNotNull(userActual);
        assertEquals("test", userActual.getUsername());
        assertEquals(id, userActual.getId());
    }

    @Test
    public void findByUserNameNotFound() {
        final long id = 1;
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(id);

        final ResponseEntity<User> response = userController.findByUserName("not_test");

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void findByIdHappyPath() {
        final long id = 1;
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findById(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User userActual = response.getBody();
        assertNotNull(userActual);
        assertEquals("test", userActual.getUsername());
        assertEquals(id, userActual.getId());
    }

    @Test
    public void findByIdNotFound() {
        final long id = 1;
        User user = new User();
        user.setUsername("test");
        user.setPassword("testPassword");
        user.setId(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findById(2L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
