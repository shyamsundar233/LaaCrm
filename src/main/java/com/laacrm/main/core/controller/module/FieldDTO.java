package com.laacrm.main.core.controller.module;

import lombok.Data;

@Data
public class FieldDTO {

    private String fieldId;

    private String moduleId;

    private String fieldName;

    private String fieldType;

    public FieldDTO() {}

    public FieldDTO(String fieldId, String moduleId, String fieldName, String fieldType) {
        this.fieldId = fieldId;
        this.moduleId = moduleId;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

}
