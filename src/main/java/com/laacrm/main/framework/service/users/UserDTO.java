package com.laacrm.main.framework.service.users;

import lombok.Data;

@Data
public class UserDTO {

    private Long userId;

    private String userName;

    private String password;

    private String email;

    private String phone;

    private String firstName;

    private String lastName;

    private String roleName;

}
