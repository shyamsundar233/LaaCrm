package com.laacrm.main.core.controller.module;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModuleDTO {

    private String moduleId;

    private String moduleName;

    private String singularName;

    private String pluralName;

    private String type;

    private String status;

}
