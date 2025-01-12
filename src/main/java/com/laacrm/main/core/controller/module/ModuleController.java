package com.laacrm.main.core.controller.module;

import com.laacrm.main.core.controller.APIController;
import com.laacrm.main.core.entity.Module;
import com.laacrm.main.core.service.ServiceWrapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
@AllArgsConstructor
public class ModuleController extends APIController {

    private final ServiceWrapper<Module> moduleService;

    @GetMapping("/module")
    public ResponseEntity<APIResponse> getModule(){
        List<Module> modules = moduleService.findAll();
        return response();
    }

}
