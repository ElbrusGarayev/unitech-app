package az.uni.unitechapp.service;

import az.uni.unitechapp.model.request.LoginRequest;
import az.uni.unitechapp.model.request.RegistrationRequest;
import az.uni.unitechapp.model.response.TokenResponse;

public interface AuthService {

    TokenResponse login(LoginRequest loginRequest, String ip);

    void register(RegistrationRequest registrationRequest);

}
