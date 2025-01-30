package com.laacrm.main.core.controller.module;

import lombok.Data;

import java.util.List;

@Data
public class LayoutDTO {

    private String layoutId;

    private String layoutName;

    private String moduleId;

    private String isDefault;

    private List<FieldDTO> fields;

    public LayoutDTO() {}

    public LayoutDTO(String layoutId, String layoutName, String moduleId, String isDefault, List<FieldDTO> fields) {
        this.layoutId = layoutId;
        this.layoutName = layoutName;
        this.moduleId = moduleId;
        this.isDefault = isDefault;
        this.fields = fields;
    }

}
