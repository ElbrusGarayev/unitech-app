package az.uni.unitechapp.controller;

import az.uni.unitechapp.model.request.LoginRequest;
import az.uni.unitechapp.model.request.RegistrationRequest;
import az.uni.unitechapp.model.response.TokenResponse;
import az.uni.unitechapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest loginRequest, String ip) {
        return authService.login(loginRequest, ip);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registration")
    public void register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        authService.register(registrationRequest);
    }

}
