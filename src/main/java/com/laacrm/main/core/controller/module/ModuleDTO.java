package com.laacrm.main.core.controller.module;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ModuleDTO {

    private String moduleId;

    private String moduleName;

    private String singularName;

    private String pluralName;

    private String type;

    private String status;

    private List<LayoutDTO> layouts = new ArrayList<>();

    public ModuleDTO() {}

    public ModuleDTO(String moduleId, String moduleName, String singularName, String pluralName, String type, String status, List<LayoutDTO> layouts) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.singularName = singularName;
        this.pluralName = pluralName;
        this.type = type;
        this.status = status;
        this.layouts = layouts;
    }

}
