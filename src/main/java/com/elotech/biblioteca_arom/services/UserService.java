package com.elotech.biblioteca_arom.services;

import com.elotech.biblioteca_arom.entities.User;
import com.elotech.biblioteca_arom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Serviço responsável pela gestão dos usuários no sistema de biblioteca.
 * Fornece operações para criar, atualizar, listar e excluir usuários,
 * além de validações de email e data de registro.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    /**
     * Construtor que injeta o repositório de usuários.
     *
     * @param userRepository o repositório de usuários
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Cria um novo usuário após validação de email e data de registro.
     *
     * @param user o objeto User contendo os detalhes do usuário
     * @return o usuário criado
     * @throws RuntimeException se o email for inválido ou se a data de registro for maior que a data atual
     */
    public User createUser(User user) {
        validateEmail(user.getEmail());
        validateRegistrationDate(user.getRegistrationDate());
        return userRepository.save(user);
    }

    /**
     * Retorna todos os usuários cadastrados no sistema.
     *
     * @return uma lista contendo todos os usuários
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Busca um usuário pelo seu ID.
     *
     * @param id o ID do usuário a ser buscado
     * @return o usuário encontrado
     * @throws RuntimeException se o usuário não for encontrado
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não foi encontrado!"));
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id o ID do usuário a ser atualizado
     * @param updateUser o objeto User contendo as novas informações
     * @return o usuário atualizado
     */
    public User updateUser(Long id, User updateUser) {
        User existingUser = getUserById(id);
        existingUser.setName(updateUser.getName());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setRegistrationDate(updateUser.getRegistrationDate());
        existingUser.setPhoneNumber(updateUser.getPhoneNumber());

        return userRepository.save(existingUser);
    }

    /**
     * Exclui um usuário pelo seu ID.
     *
     * @param id o ID do usuário a ser excluído
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Valida o formato do email.
     *
     * @param email o email a ser validado
     * @throws RuntimeException se o email for inválido
     */
    private void validateEmail(String email) {
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new RuntimeException("Email inválido!");
        }
    }

    /**
     * Valida a data de registro para garantir que não seja uma data futura.
     *
     * @param registrationDate a data de registro a ser validada
     * @throws RuntimeException se a data de registro for maior que o dia atual
     */
    private void validateRegistrationDate(LocalDate registrationDate) {
        if (registrationDate.isAfter(LocalDate.now())) {
            throw new RuntimeException("Data de cadastro não pode ser maior que o dia atual!");
        }
    }
}
