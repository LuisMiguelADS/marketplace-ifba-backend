package com.marketplace.ifba.service;

import com.marketplace.ifba.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // @Override
    // public UserDetails loadUserByUsername(String username) throws
    // UsernameNotFoundException {
    // return userRepository.findAll().stream().filter(userDetails ->
    // userDetails.getEmail().equals(username)).findFirst().orElse(null);
    // }

    // fiz essa modificação para lançar uma exceção caso o usuário não seja
    // encontrado,
    // no metodo anterior retornava null, enquanto que o correto é lançar uma
    // exceção; Reajustei para ficar ainda mais alinhado com os testes e os padrões
    // do Spring Security

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findAll().stream()
                .filter(userDetails -> userDetails.getEmail().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}
