package com.laacrm.main.core.controller.module;

import com.laacrm.main.core.controller.APIController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class ModuleController extends APIController {

    @GetMapping("/module")
    public ResponseEntity<APIResponse> getModule(){
        return response();
    }

}
