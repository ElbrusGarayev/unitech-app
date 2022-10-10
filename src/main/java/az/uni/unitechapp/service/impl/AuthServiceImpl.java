package az.uni.unitechapp.service.impl;

import az.uni.unitechapp.dao.entity.Session;
import az.uni.unitechapp.dao.entity.User;
import az.uni.unitechapp.dao.repository.SessionRepository;
import az.uni.unitechapp.dao.repository.UserRepository;
import az.uni.unitechapp.encryptor.DataEncryptor;
import az.uni.unitechapp.exception.InactiveUserException;
import az.uni.unitechapp.exception.InvalidUsernameOrPasswordException;
import az.uni.unitechapp.exception.PinAlreadyExistsException;
import az.uni.unitechapp.jwt.JwtService;
import az.uni.unitechapp.model.request.LoginRequest;
import az.uni.unitechapp.model.request.RegistrationRequest;
import az.uni.unitechapp.model.response.TokenResponse;
import az.uni.unitechapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static az.uni.unitechapp.enums.UserStatus.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SessionRepository sessionRepository;

    @Override
    public TokenResponse login(LoginRequest loginRequest, String ip) {
        log.info("new login from ip: {}", ip);
        return userRepository.findFirstByPin(loginRequest.getUsername())
                .map(user -> getTokenIfPasswordIsValidOrElseThrow(user, loginRequest, ip))
                .orElseThrow(InvalidUsernameOrPasswordException::new);
    }

    @Override
    public void register(RegistrationRequest registrationRequest) {
        userRepository.findFirstByPin(registrationRequest.getPin())
                .ifPresent(user -> {
                    throw new PinAlreadyExistsException();
                });
        createUser(registrationRequest);
    }

    private TokenResponse getTokenIfPasswordIsValidOrElseThrow(User user, LoginRequest loginRequest, String ip) {
        if (!checkUserPassword(user.getPassword(), loginRequest.getPassword()))
            throw new InvalidUsernameOrPasswordException();
        if (!checkUserStatus(user)) {
            throw new InactiveUserException();
        }
        String token = TOKEN_PREFIX.concat(jwtService.issueToken(user.getId(), user.getPin()));
        addLoginHistory(user, token, ip);
        return TokenResponse.builder()
                .externalId(user.getId())
                .token(token)
                .build();
    }

    private void addLoginHistory(User user, String token, String ip) {
        sessionRepository.save(Session.builder()
                .userId(user.getId())
                .token(token)
                .ip(ip)
                .build());
    }

    private boolean checkUserStatus(User user) {
        return user.getStatus() == ACTIVE;
    }

    private boolean checkUserPassword(String password, String requestedPassword) {
        return password.equals(DataEncryptor.sha256Hex(DataEncryptor.md5Hex(requestedPassword)));
    }

    private void createUser(RegistrationRequest registrationRequest) {
        User user = userRepository.save(User.builder()
                .pin(registrationRequest.getPin())
                .password(encryptPassword(registrationRequest.getPassword()))
                .status(ACTIVE)
                .build());
        userRepository.save(user);
    }

    private String encryptPassword(String password) {
        return DataEncryptor.sha256Hex(DataEncryptor.md5Hex(password));
    }

}
