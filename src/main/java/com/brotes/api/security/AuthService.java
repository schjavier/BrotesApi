package com.brotes.api.security;

import com.brotes.api.modelo.user.UserRegistrationData;
import com.brotes.api.modelo.user.UserRegistrationResponse;

public interface AuthService {

    String login(LoginRequestDto loginRequestDto);
    UserRegistrationResponse signUp(UserRegistrationData userRegistrationData);
}
