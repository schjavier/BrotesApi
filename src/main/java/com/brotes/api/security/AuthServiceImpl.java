package com.brotes.api.security;

import com.brotes.api.modelo.user.User;
import com.brotes.api.modelo.user.UserRegistrationData;
import com.brotes.api.modelo.user.UserRegistrationResponse;
import com.brotes.api.modelo.user.UserRepository;
import com.brotes.api.validations.UserValidations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserValidations userValidations;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           UserDetailServiceImpl userDetailsService,
                           UserValidations userValidations,
                           PasswordEncoder passwordEncoder,
                           UserRepository userRepository){

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userValidations = userValidations;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.username(),
                loginRequestDto.password()
        ));

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.username());
        return jwtUtil.generateToken(userDetails);
    }


    @Override
    public UserRegistrationResponse signUp(UserRegistrationData userRegistrationData) {

        userValidations.userExistsValidation(userRegistrationData.username());

        User user = User.builder().username(userRegistrationData.username())
                .password(passwordEncoder.encode(userRegistrationData.password()))
                .email(userRegistrationData.email())
                .build();

        User savedUser = userRepository.save(user);

        return new UserRegistrationResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }
}
