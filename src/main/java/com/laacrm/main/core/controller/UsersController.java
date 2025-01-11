package com.laacrm.main.core.controller;

import com.laacrm.main.framework.service.users.UserDTO;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/users/")
@AllArgsConstructor
public class UsersController extends APIController{

    @PostMapping("/saveUser")
    public ResponseEntity<JSONObject> saveUser(UserDTO userDetails) {
        return response();
    }

}
