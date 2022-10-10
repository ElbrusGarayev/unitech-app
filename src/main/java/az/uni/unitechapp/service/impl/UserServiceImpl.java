package az.uni.unitechapp.service.impl;

import az.uni.unitechapp.dao.entity.User;
import az.uni.unitechapp.dao.repository.UserRepository;
import az.uni.unitechapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow();
    }

}
