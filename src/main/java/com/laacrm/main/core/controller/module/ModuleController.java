package com.laacrm.main.core.controller.module;

import com.laacrm.main.core.controller.APIController;
import com.laacrm.main.core.entity.Module;
import com.laacrm.main.core.service.ServiceWrapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/api")
@AllArgsConstructor
public class ModuleController extends APIController {

    private final ServiceWrapper<Module> moduleService;

    @GetMapping("/module")
    public ResponseEntity<APIResponse> getModule(){
        List<Module> modules = moduleService.findAll();
        List<ModuleDTO> moduleDetails = new ArrayList<>();
        for (Module module : modules) {
            moduleDetails.add(getModuleDTOFromModuleEntity(module));
        }
        Map<String, Object> details = new HashMap<>();
        details.put("modules", moduleDetails);
        addResponse(HttpStatus.OK.value(), "Modules fetched successfully", details);
        return response();
    }

    @PostMapping("/module")
    public ResponseEntity<APIResponse> saveModule(@RequestBody ModuleDTO moduleDetails){
        Module moduleEntity = getModuleEntityFromModuleDTO(moduleDetails);
        moduleService.save(moduleEntity);
        return response();
    }

    private Module getModuleEntityFromModuleDTO(ModuleDTO moduleDTO){
        return new Module(
                moduleDTO.getModuleName(),
                moduleDTO.getSingularName(),
                moduleDTO.getPluralName(),
                Integer.valueOf(moduleDTO.getType()),
                Integer.valueOf(moduleDTO.getStatus()));
    }

    private ModuleDTO getModuleDTOFromModuleEntity(Module module){
        return new ModuleDTO(
                String.valueOf(module.getModuleId()),
                module.getModuleName(),
                module.getSingularName(),
                module.getPluralName(),
                String.valueOf(module.getType()),
                String.valueOf(module.getStatus()));
    }
}
