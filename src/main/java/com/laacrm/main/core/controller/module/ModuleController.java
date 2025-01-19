package com.laacrm.main.core.controller.module;

import com.laacrm.main.core.controller.APIController;
import com.laacrm.main.core.entity.Field;
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

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<APIResponse> getModuleById(@PathVariable("moduleId") Long moduleId){
        Module module = moduleService.findById(moduleId);
        ModuleDTO moduleDetails = module != null ? getModuleDTOFromModuleEntity(module) : null;
        Map<String, Object> details = new HashMap<>();
        details.put("module", moduleDetails);
        addResponse(HttpStatus.OK.value(), "Module fetched successfully", details);
        return response();
    }

    @PostMapping("/module")
    public ResponseEntity<APIResponse> saveModule(@RequestBody ModuleDTO moduleDetails){
        Module moduleEntity = getModuleEntityFromModuleDTO(moduleDetails);
        moduleService.save(moduleEntity);
        addResponse(HttpStatus.OK.value(), "Module saved successfully");
        return response();
    }

    @PutMapping("/module")
    public ResponseEntity<APIResponse> updateModule(@RequestBody ModuleDTO moduleDetails){
        Module moduleEntity = getModuleEntityFromModuleDTO(moduleDetails);
        if(moduleDetails.getModuleId() != null && !moduleDetails.getModuleId().isEmpty()){
            moduleEntity.setModuleId(Long.valueOf(moduleDetails.getModuleId()));
        }
        moduleService.update(moduleEntity);
        addResponse(HttpStatus.OK.value(), "Module updated successfully");
        return response();
    }

    @DeleteMapping("/module/{moduleId}")
    public ResponseEntity<APIResponse> deleteModule(@PathVariable("moduleId") Long moduleId){
        moduleService.delete(moduleId);
        addResponse(HttpStatus.OK.value(), "Module deleted successfully");
        return response();
    }

    private Module getModuleEntityFromModuleDTO(ModuleDTO moduleDTO){
        List<Field> fieldsList = new ArrayList<>();
        for (FieldDTO fieldDTO : moduleDTO.getFields()) {
            fieldsList.add(getFieldEntityFromFieldDTO(fieldDTO));
        }
        return new Module(
                moduleDTO.getModuleName(),
                moduleDTO.getSingularName(),
                moduleDTO.getPluralName(),
                moduleDTO.getType() != null && !moduleDTO.getType().isEmpty() ? Integer.valueOf(moduleDTO.getType()) : null,
                moduleDTO.getStatus() != null && !moduleDTO.getStatus().isEmpty() ? Integer.valueOf(moduleDTO.getStatus()) : null,
                fieldsList);
    }

    private ModuleDTO getModuleDTOFromModuleEntity(Module module){
        List<FieldDTO> fieldsList = new ArrayList<>();
        for (Field field : module.getFields()) {
            fieldsList.add(getFieldDTOFromFieldEntity(field));
        }
        return new ModuleDTO(
                String.valueOf(module.getModuleId()),
                module.getModuleName(),
                module.getSingularName(),
                module.getPluralName(),
                String.valueOf(module.getType()),
                String.valueOf(module.getStatus()),
                fieldsList);
    }

    private Field getFieldEntityFromFieldDTO(FieldDTO fieldDTO){
        return new Field(
                fieldDTO.getFieldName(),
                Integer.valueOf(fieldDTO.getFieldType())
        );
    }

    private FieldDTO getFieldDTOFromFieldEntity(Field field){
        return new FieldDTO(
                String.valueOf(field.getFieldId()),
                String.valueOf(field.getModule().getModuleId()),
                field.getFieldName(),
                String.valueOf(field.getFieldType()),
                field.getFieldValue()
        );
    }
}
