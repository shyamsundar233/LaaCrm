package com.laacrm.main.core.controller.module;

import com.laacrm.main.core.controller.APIController;
import com.laacrm.main.core.entity.*;
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
        Module module = new Module(
                moduleDTO.getModuleName(),
                moduleDTO.getSingularName(),
                moduleDTO.getPluralName(),
                moduleDTO.getType() != null && !moduleDTO.getType().isEmpty() ? Integer.valueOf(moduleDTO.getType()) : null,
                moduleDTO.getStatus() != null && !moduleDTO.getStatus().isEmpty() ? Integer.valueOf(moduleDTO.getStatus()) : null,
                null);
        List<Layout> layoutsList = new ArrayList<>();
        for(LayoutDTO layoutDTO : moduleDTO.getLayouts()){
            Layout layout = new Layout(
                    layoutDTO.getLayoutId() != null && !layoutDTO.getLayoutId().isEmpty() ? Long.valueOf(layoutDTO.getLayoutId()) : null,
                    layoutDTO.getLayoutName(),
                    null,
                    Boolean.parseBoolean(layoutDTO.getIsDefault())
            );
            List<Field> fieldsList = new ArrayList<>();
            for (FieldDTO fieldDTO : layoutDTO.getFields()) {
                fieldsList.add(getFieldEntityFromFieldDTO(fieldDTO));
            }
            layout.setFields(fieldsList);
            layoutsList.add(layout);
        }
        module.setLayouts(layoutsList);
        return module;
    }

    private ModuleDTO getModuleDTOFromModuleEntity(Module module){
        ModuleDTO moduleDTO = new ModuleDTO(
                String.valueOf(module.getModuleId()),
                module.getModuleName(),
                module.getSingularName(),
                module.getPluralName(),
                String.valueOf(module.getType()),
                String.valueOf(module.getStatus()),
                null
        );
        List<LayoutDTO> layoutDTOList = new ArrayList<>();
        for (Layout layout : module.getLayouts()) {
            LayoutDTO layoutDTO = new LayoutDTO(
                    String.valueOf(layout.getLayoutId()),
                    layout.getLayoutName(),
                    String.valueOf(layout.getModule().getModuleId()),
                    String.valueOf(layout.isDefault()),
                    null
            );
            List<FieldDTO> fieldsList = new ArrayList<>();
            for(Field field : layout.getFields()) {
                fieldsList.add(getFieldDTOFromFieldEntity(field));
            }
            layoutDTO.setFields(fieldsList);
            layoutDTOList.add(layoutDTO);
        }
        moduleDTO.setLayouts(layoutDTOList);
        return moduleDTO;
    }

    public static Field getFieldEntityFromFieldDTO(FieldDTO fieldDTO){
        List<FieldProperties> fieldProperties = new ArrayList<>();
        Field field = new Field(
                fieldDTO.getFieldName(),
                Integer.valueOf(fieldDTO.getFieldType()),
                fieldProperties);
        for(FieldPropertyDTO fieldPropertyDTO : fieldDTO.getFieldProperties()){
            FieldProperties properties = new FieldProperties();
            FieldPropertiesRef propRef = new FieldPropertiesRef();
            propRef.setPropertyName(fieldPropertyDTO.getPropertyName());
            properties.setPropertyValue(fieldPropertyDTO.getPropertyValue());
            properties.setProperty(propRef);
            properties.setField(field);
            fieldProperties.add(properties);
        }
        return field;
    }

    public static FieldDTO getFieldDTOFromFieldEntity(Field field){
        List<FieldPropertyDTO> fieldPropertiesList = new ArrayList<>();
        for(FieldProperties fieldProperties : field.getFieldProperties()){
            FieldPropertyDTO fieldPropertyDTO = new FieldPropertyDTO(
                    String.valueOf(fieldProperties.getPropertyId()),
                    String.valueOf(fieldProperties.getField().getFieldId()),
                    fieldProperties.getProperty().getPropertyName(),
                    fieldProperties.getPropertyValue()
            );
            fieldPropertiesList.add(fieldPropertyDTO);
        }
        return new FieldDTO(
                String.valueOf(field.getFieldId()),
                String.valueOf(field.getModule().getModuleId()),
                field.getFieldName(),
                String.valueOf(field.getFieldType()),
                fieldPropertiesList
        );
    }
}
