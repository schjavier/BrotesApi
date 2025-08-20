package com.brotes.api.controller;

import com.brotes.api.modelo.user.UserRegistrationData;
import com.brotes.api.modelo.user.UserRegistrationResponse;
import com.brotes.api.security.AuthService;
import com.brotes.api.security.LoginRequestDto;
import com.brotes.api.security.LoginResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> userLogin(@RequestBody LoginRequestDto request){
        String token = authService.login(request);
        return ResponseEntity.ok(new LoginResponseDto(token));
    }


    @PostMapping("/signup")
    public ResponseEntity<UserRegistrationResponse> signup(@RequestBody UserRegistrationData userRegistrationData){
        UserRegistrationResponse userRegistrationResponse = authService.signUp(userRegistrationData);

        return ResponseEntity.status(HttpStatus.CREATED).body(userRegistrationResponse);
    }


}

