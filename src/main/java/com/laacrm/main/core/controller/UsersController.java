package com.laacrm.main.core.controller;

import com.laacrm.main.framework.exception.FrameworkException;
import com.laacrm.main.framework.service.FrameworkConstants;
import com.laacrm.main.framework.service.users.UserDTO;
import com.laacrm.main.framework.service.users.UserService;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v1/api/users/")
@AllArgsConstructor
public class UsersController extends APIController{

    private final Logger LOGGER = Logger.getLogger(UsersController.class.getName());

    private final UserService userService;

    @PostMapping("/saveUser")
    public ResponseEntity<JSONObject> saveUser(@RequestBody UserDTO userDetails) {
        try{
            userDetails.setRoleName(FrameworkConstants.DefaultRole.USER.getRoleName());
            userService.saveUser(userDetails);
            return response();
        }catch (FrameworkException exp){
            LOGGER.log(Level.SEVERE, "==========> User Creation Failed :: {0} <==========", exp.getMessage());
            throw new APIException(HttpStatus.BAD_REQUEST.value(), exp.getMessage());
        }
    }

}
