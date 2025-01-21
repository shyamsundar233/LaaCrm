package com.laacrm.main.core.controller.module;

import lombok.Data;

@Data
public class FieldPropertyDTO {

    private String propertyId;

    private String fieldId;

    private String propertyName;

    private String propertyValue;

    public FieldPropertyDTO(String propertyId, String fieldId, String propertyName, String propertyValue) {
        this.propertyId = propertyId;
        this.fieldId = fieldId;
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }
}
