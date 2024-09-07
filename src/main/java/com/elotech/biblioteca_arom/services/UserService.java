package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.entities.User;
import com.elotech.biblioteca_arom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE
    public User createUser(User user) {
        validateEmail(user.getEmail());
        validateRegistrationDate(user.getRegistrationDate());
        return userRepository.save(user);
    }

    // READ (find All)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    //READ (find by ID)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não foi encontrado!"));
    }

    // UPDATE
    public User updateUser(Long id, User updateUser) {
        User existingUser = getUserById(id);
        existingUser.setName(updateUser.getName());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setRegistrationDate(updateUser.getRegistrationDate());
        existingUser.setPhoneNumber(updateUser.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    // DELETE
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private void validateEmail(String email) {
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new RuntimeException("Email inválido!");
        }
    }

    private void validateRegistrationDate(LocalDate registrationDate) {
        if (registrationDate.isAfter(LocalDate.now())) {
            throw new RuntimeException("Data de cadastro não pode ser maior que o dia atual!");
        }
    }
}
