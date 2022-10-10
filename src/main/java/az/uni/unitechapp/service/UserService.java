package az.uni.unitechapp.service;

import az.uni.unitechapp.dao.entity.User;

public interface UserService {

    User getCurrentUser(Long userId);

}
