package com.laacrm.main.framework.service.users;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long userId);

    UserDTO saveUser(UserDTO userDetails);

    void deleteUser(Long userId);

}
