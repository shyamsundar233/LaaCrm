package com.laacrm.main.core.controller.module;

import lombok.Data;

import java.util.List;

@Data
public class FieldDTO {

    private String fieldId;

    private String moduleId;

    private String fieldName;

    private String fieldType;

    private String isVisible;

    private List<FieldPropertyDTO> fieldProperties;

    public FieldDTO() {}

    public FieldDTO(String fieldId, String moduleId, String fieldName, String fieldType, String isVisible, List<FieldPropertyDTO> fieldProperties) {
        this.fieldId = fieldId;
        this.moduleId = moduleId;
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldProperties = fieldProperties;
        this.isVisible = isVisible;
    }

}
