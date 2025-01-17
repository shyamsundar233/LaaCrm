package com.laacrm.main.core.service;

import com.laacrm.main.core.Constants;
import com.laacrm.main.core.controller.APIException;
import com.laacrm.main.core.dao.DaoHelper;
import com.laacrm.main.core.entity.Module;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Service
@Transactional
@AllArgsConstructor
public class ModuleService implements ServiceWrapper<Module> {

    private static final Logger LOGGER = Logger.getLogger(ModuleService.class.getName());

    private final DaoHelper<Module, Long> moduleDaoHelper;
    private final Validator validator = new Validator();

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
        validator.validateSave(module);
        module.setModuleName(getNextAvailableModuleName());
        return moduleDaoHelper.save(module);
    }

    @Override
    public Module update(Module module) {
        validator.validateUpdate(module);
        Module existingModule = findById(module.getModuleId());
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
        return moduleDaoHelper.update(existingModule);
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

    class Validator {

        public void validateSave(Module module) {
            defaultValidation(module);
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
            Pattern pattern = Pattern.compile(Constants.ModuleConstants.MODULE_NAME_REGEX);
            if(!pattern.matcher(module.getSingularName()).matches()) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Special characters not allowed in Singular Name");
            }
            if(!pattern.matcher(module.getPluralName()).matches()) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Special characters not allowed in Plural Name");
            }
            if(!Constants.ModuleConstants.ModuleType.isValidType(module.getType())){
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module Type is not valid");
            }
            if(!Constants.ModuleConstants.ModuleStatus.isValidStatus(module.getStatus())){
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Module Status is not valid");
            }
            List<Module> allModules = findAll();
            boolean isDuplicate = allModules.stream().anyMatch(mod -> mod.getSingularName().equals(module.getSingularName()));
            if(isDuplicate) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Singular Name already exists");
            }
            isDuplicate = allModules.stream().anyMatch(mod -> mod.getPluralName().equals(module.getPluralName()));
            if(isDuplicate) {
                throw new APIException(HttpStatus.BAD_REQUEST.value(), "Plural Name already exists");
            }
        }
    }
}
