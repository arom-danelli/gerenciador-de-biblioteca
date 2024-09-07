package com.elotech.biblioteca_arom.controllers;

import com.elotech.biblioteca_arom.entities.User;
import com.elotech.biblioteca_arom.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar as operações relacionadas aos usuários.
 * Fornece endpoints para criação, atualização, obtenção e exclusão de usuários.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Construtor que injeta o serviço de usuários.
     *
     * @param userService o serviço de gerenciamento de usuários
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Cria um novo usuário.
     *
     * @param user o objeto User contendo as informações do usuário a ser criado
     * @return uma resposta HTTP com o usuário criado e status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    /**
     * Retorna uma lista de todos os usuários cadastrados.
     *
     * @return uma lista de usuários
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Retorna um usuário específico com base no ID fornecido.
     *
     * @param id o ID do usuário
     * @return o usuário encontrado e status 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * Atualiza um usuário existente com base no ID fornecido.
     *
     * @param id o ID do usuário a ser atualizado
     * @param updateUser o objeto User com os novos dados
     * @return o usuário atualizado e status 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        User updatedUser = userService.updateUser(id, updateUser);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Exclui um usuário com base no ID fornecido.
     *
     * @param id o ID do usuário a ser excluído
     * @return uma resposta HTTP com status 204 (No Content) se a exclusão for bem-sucedida
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
