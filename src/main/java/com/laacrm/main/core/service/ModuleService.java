package com.laacrm.main.core.service;

import com.laacrm.main.core.FieldConstants;
import com.laacrm.main.core.ModuleConstants;
import com.laacrm.main.core.controller.APIException;
import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.Field;
import com.laacrm.main.core.entity.FieldProperties;
import com.laacrm.main.core.entity.FieldPropertiesRef;
import com.laacrm.main.core.entity.Module;
import com.laacrm.main.core.repo.FieldPropertiesRefRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Service
@Transactional
@AllArgsConstructor
public class ModuleService implements ServiceWrapper<Module> {

    private static final Logger LOGGER = Logger.getLogger(ModuleService.class.getName());

    private final DaoHelper<Module, Long> moduleDaoHelper;
    private final FieldPropertiesRefRepo propRefRepo;
    private final ModuleValidator moduleValidator = new ModuleValidator();
    private final FieldValidator fieldValidator = new FieldValidator();

    @Override
    public List<Module> findAll() {
        return moduleDaoHelper.findAll(Module.class);
    }

    @Override
    public Module findById(Long moduleId) {
        return moduleDaoHelper.findById(Module.class, moduleId).orElse(null);
    }

    @Override
    public Module save(Module module) {
        moduleValidator.validateSave(module);
        module.setModuleName(getNextAvailableModuleName());
        for(Field field : module.getFields()) {
            field.setModule(module);
        }
        fieldValidator.validateSave(module.getFields());
        updateFieldRefData(module.getFields());
        return moduleDaoHelper.save(module);
    }

    @Override
    public Module update(Module module) {
        Module existingModule = findById(module.getModuleId());
        if(existingModule == null) {
            throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module not found");
        }
        if(module.getSingularName() != null && !module.getSingularName().isEmpty()){
            existingModule.setSingularName(module.getSingularName());
        }
        if(module.getPluralName() != null && !module.getPluralName().isEmpty()){
            existingModule.setPluralName(module.getPluralName());
        }
        if(module.getType() != null){
            existingModule.setType(module.getType());
        }
        if(module.getStatus() != null){
            existingModule.setStatus(module.getStatus());
        }
        moduleValidator.validateUpdate(existingModule);
        updateFieldsInExistingModule(existingModule, module);
        fieldValidator.validateSave(existingModule.getFields());
        return moduleDaoHelper.update(existingModule);
    }

    private void updateFieldRefData(List<Field> fields){
        for(Field field : fields){
            for(FieldProperties property : field.getFieldProperties()){
                FieldPropertiesRef fieldPropertiesRef = propRefRepo.findByPropertyName(property.getProperty().getPropertyName()).orElse(null);
                if(fieldPropertiesRef == null){
                    throw new APIException(HttpStatus.BAD_REQUEST.value(), "Field properties not found");
                }
                property.setProperty(fieldPropertiesRef);
            }
        }
    }

    private void updateFieldsInExistingModule(Module existingModule, Module module) {
        for(Field field : module.getFields()) {
            if(field.getFieldId() == null){
                field.setModule(existingModule);
                existingModule.getFields().add(field);
            }else{
                Long fieldId = field.getFieldId();
                Field existingField = existingModule.getFields().stream().filter(fld -> fld.getFieldId().equals(fieldId)).findFirst().orElse(null);
                if(existingField != null){
                    if(field.getFieldName() != null && !field.getFieldName().isEmpty()){
                        existingField.setFieldName(field.getFieldName());
                    }
                    if(field.getFieldType() != null){
                        existingField.setFieldType(field.getFieldType());
                    }
                }
            }
        }
    }

    @Override
    public void delete(Long moduleId) {
        Module module = findById(moduleId);
        if(module == null){
            throw new APIException(HttpStatus.NOT_FOUND.value(), "Module not found");
        }
        moduleDaoHelper.deleteById(Module.class, moduleId);
    }

    private String getNextAvailableModuleName(){
        List<String> existingModuleNames = findAll().stream().map(Module::getModuleName).toList();
        if(existingModuleNames.size() >= 200){
            LOGGER.log(Level.SEVERE, "==========> Module Limit Exceeded <==========");
            throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module Limit Exceeded");
        }
        int moduleCount = 1;
        while (moduleCount <= 200){
            if(!existingModuleNames.contains("CustomModule" + moduleCount)){
                return "CustomModule" + moduleCount;
            }
            moduleCount++;
        }
        return null;
    }

    class ModuleValidator {

        public void validateSave(Module module) {
            defaultValidation(module);
            duplicateModuleNameValidation(module);
        }

        public void validateUpdate(Module module) {
            if(module.getModuleId() == null){
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module Id cannot be null or empty");
            }
            Module existingModule = findById(module.getModuleId());
            if(existingModule == null){
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module Not Found");
            }
            defaultValidation(module);
        }

        private void defaultValidation(Module module) {
            if(module.getSingularName() == null || module.getSingularName().isEmpty()) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Singular Name cannot be null or empty");
            }
            if(module.getPluralName() == null || module.getPluralName().isEmpty()) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Plural Name cannot be null or empty");
            }
            if(module.getSingularName().equals(module.getPluralName())) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Singular Name and Plural Name cannot be the same");
            }
            Pattern pattern = Pattern.compile(ModuleConstants.MODULE_NAME_REGEX);
            if(!pattern.matcher(module.getSingularName()).matches()) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Special characters not allowed in Singular Name");
            }
            if(!pattern.matcher(module.getPluralName()).matches()) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Special characters not allowed in Plural Name");
            }
            if(!ModuleConstants.ModuleType.isValidType(module.getType())){
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module Type is not valid");
            }
            if(!ModuleConstants.ModuleStatus.isValidStatus(module.getStatus())){
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module Status is not valid");
            }
        }
        private void duplicateModuleNameValidation(Module module) {
            List<Module> allModules = findAll();
            boolean isDuplicate = allModules.stream().anyMatch(mod -> !mod.getModuleId().equals(module.getModuleId()) && mod.getSingularName().equals(module.getSingularName()));
            if(isDuplicate) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Singular Name already exists");
            }
            isDuplicate = allModules.stream().anyMatch(mod -> !mod.getModuleId().equals(module.getModuleId()) && mod.getPluralName().equals(module.getPluralName()));
            if(isDuplicate) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Plural Name already exists");
            }
        }
    }

    static class FieldValidator {

        public void validateSave(List<Field> fields) {
            defaultValidation(fields);
        }

        private void defaultValidation(List<Field> fields) {
            for(Field field : fields) {
                if(field.getFieldName() == null || field.getFieldName().isEmpty()) {
                    throw new APIException(HttpStatus.BAD_REQUEST.value(), "Field Name cannot be null or empty");
                }
                Pattern pattern = Pattern.compile(FieldConstants.FIELD_NAME_REGEX);
                if(!pattern.matcher(field.getFieldName()).matches()) {
                    throw new APIException(HttpStatus.BAD_REQUEST.value(), "Special characters not allowed in Field Name");
                }
                if(field.getFieldType() == null || !FieldConstants.FieldType.isValidType(field.getFieldType())) {
                    throw new APIException(HttpStatus.BAD_REQUEST.value(), "Field Type is not valid");
                }
                if(field.getModule() == null) {
                    throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module cannot be null");
                }
            }
            Set<String> fieldNames = new HashSet<>();
            for(Field field : fields) {
                if(!fieldNames.add(field.getFieldName())) {
                    throw new APIException(HttpStatus.BAD_REQUEST.value(), "Duplicate Field Name Found");
                }
            }
        }
    }
}
