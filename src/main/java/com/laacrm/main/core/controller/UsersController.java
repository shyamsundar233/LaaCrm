package com.laacrm.main.core.controller;

import com.laacrm.main.core.config.JwtService;
import com.laacrm.main.framework.entities.Users;
import com.laacrm.main.framework.exception.FrameworkException;
import com.laacrm.main.framework.service.FrameworkConstants;
import com.laacrm.main.framework.service.users.LoginUser;
import com.laacrm.main.framework.service.users.UserDTO;
import com.laacrm.main.framework.service.users.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v1/api/users/")
@AllArgsConstructor
public class UsersController extends APIController{

    private final Logger LOGGER = Logger.getLogger(UsersController.class.getName());

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/saveUser")
    public ResponseEntity<APIResponse> saveUser(@RequestBody UserDTO userDetails) {
        try{
            userDetails.setRoleName(FrameworkConstants.DefaultRole.USER.getRoleName());
            userService.saveUser(userDetails);
            addResponse(HttpStatus.OK.value(), "User Created Successfully");
            return response();
        }catch (FrameworkException exp){
            LOGGER.log(Level.SEVERE, "==========> User Creation Failed :: {0} <==========", exp.getMessage());
            throw new APIException(HttpStatus.BAD_REQUEST.value(), exp.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<APIResponse> authenticate(@RequestBody LoginUser loginUserDetails) {
        try{
            Users loginUser = userService.authenticateUser(loginUserDetails);
            String jwtToken = jwtService.generateToken(loginUser);
            Map<String, Object> details = new HashMap<>();
            details.put("token", jwtToken);
            details.put("expiresIn", String.valueOf(jwtService.getExpirationTime()));
            addResponse(HttpStatus.OK.value(), "User Authenticated Successfully", details);
            return response();
        }catch (FrameworkException exp){
            LOGGER.log(Level.SEVERE, "==========> User Login Failed :: {0} <==========", exp.getMessage());
            throw new APIException(HttpStatus.UNAUTHORIZED.value(), exp.getMessage());
        }
    }

}
