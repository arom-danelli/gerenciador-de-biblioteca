package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.entities.User;
import com.elotech.biblioteca_arom.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setRegistrationDate(LocalDate.now());
        user.setPhoneNumber("123456789");
    }

    @Test
    public void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Test User", createdUser.getName());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testCreateUserWithInvalidEmail() {
        user.setEmail("invalid-email");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Email inválido!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testCreateUserWithFutureRegistrationDate() {
        user.setRegistrationDate(LocalDate.now().plusDays(1)); // Data futura

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("Data de cadastro não pode ser maior que o dia atual!", exception.getMessage());
        verify(userRepository, never()).save(any(User.class)); // Certifica que o método save não foi chamado
    }

    @Test
    public void testGetAllUsers() {
        User user2 = new User();
        user2.setName("User 2");

        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Test User", result.get(0).getName());
        assertEquals("User 2", result.get(1).getName());
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(1L, foundUser.getId());
        assertEquals("Test User", foundUser.getName());
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });

        assertEquals("Usuário não foi encontrado!", exception.getMessage());
    }

    @Test
    public void testUpdateUser() {
        User updatedUser = new User();
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPhoneNumber("987654321");
        updatedUser.setRegistrationDate(LocalDate.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("Updated User", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("987654321", result.getPhoneNumber());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}