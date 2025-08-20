package com.brotes.api.validations;

import com.brotes.api.exceptions.UserAlreadyExistsException;
import com.brotes.api.modelo.user.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidations {

    private final UserRepository userRepository;

    public UserValidations(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void userExistsValidation(String username){

        if(userRepository.existsByUsername(username)){
            throw new UserAlreadyExistsException("El Usuario ya se encuentra regitrado");
        }

    }


}
