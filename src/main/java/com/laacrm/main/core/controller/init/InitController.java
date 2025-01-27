package com.laacrm.main.core.controller.init;

import com.laacrm.main.core.controller.APIController;
import com.laacrm.main.core.service.InitPopulateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
@AllArgsConstructor
public class InitController extends APIController {

    private final InitPopulateService initPopulateService;

    @GetMapping("/initPopulate")
    public ResponseEntity<APIResponse> initPopulate(){
        initPopulateService.populate();
        addResponse(HttpStatus.OK.value(), "Initialized successfully");
        return response();
    }

}
