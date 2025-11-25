package com.brotes.api.validations;

import com.brotes.api.exceptions.UserAlreadyExistsException;
import com.brotes.api.modelo.user.User;
import com.brotes.api.modelo.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserValidations {

    private final UserRepository userRepository;

    public UserValidations(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void userExistsValidation(String username){

        if(userRepository.existsByUsername(username)){
            throw new UserAlreadyExistsException("El Usuario ya se encuentra registrado");
        }

    }


}
