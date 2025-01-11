package com.laacrm.main.framework.service.users;

import com.laacrm.main.framework.entities.Users;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO saveUser(UserDTO userDetails);

    void deleteUser(Long userId);

    Users authenticateUser(LoginUser loginUserDetails);

}
