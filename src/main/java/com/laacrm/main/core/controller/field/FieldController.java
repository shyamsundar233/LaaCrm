package com.laacrm.main.core.controller.field;

import com.laacrm.main.core.controller.APIController;
import com.laacrm.main.core.controller.module.FieldDTO;
import com.laacrm.main.core.controller.module.ModuleController;
import com.laacrm.main.core.entity.Field;
import com.laacrm.main.core.service.FieldService;
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
public class FieldController extends APIController {

    private final FieldService fieldService;

    @GetMapping("/fields")
    public ResponseEntity<APIResponse> getFields(@RequestParam("moduleId") Long moduleId) {
        Map<String, Object> details = new HashMap<>();
        details.put("fields", getFieldDTOList(fieldService.findAll(moduleId)));
        addResponse(HttpStatus.OK.value(), "Fields fetched Successfully", details);
        return response();
    }

    @GetMapping("/fields/{fieldId}")
    public ResponseEntity<APIResponse> getFieldById(@PathVariable Long fieldId) {
        Map<String, Object> details = new HashMap<>();
        details.put("fieldId", getFieldDTOList(List.of(fieldService.findById(fieldId))));
        addResponse(HttpStatus.OK.value(), "Field fetched Successfully", details);
        return response();
    }

    private List<Field> getFieldList(List<FieldDTO> fieldDTOList) {
        List<Field> fieldList = new ArrayList<>();
        for (FieldDTO fieldDTO : fieldDTOList) {
            fieldList.add(ModuleController.getFieldEntityFromFieldDTO(fieldDTO));
        }
        return fieldList;
    }

    private List<FieldDTO> getFieldDTOList(List<Field> fields) {
        List<FieldDTO> fieldDTOList = new ArrayList<>();
        for (Field field : fields) {
            fieldDTOList.add(ModuleController.getFieldDTOFromFieldEntity(field));
        }
        return fieldDTOList;
    }

}
