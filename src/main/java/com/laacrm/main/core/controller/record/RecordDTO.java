package com.laacrm.main.core.controller.record;

import lombok.Data;

import java.util.Map;

@Data
public class RecordDTO {

    private String recordId;

    private String moduleId;

    private String layoutId;

    private String createdAt;

    private String updatedAt;

    private Map<String, Object> recordDetails;

    public RecordDTO(String recordId, String moduleId, String layoutId, String createdAt, String updatedAt, Map<String, Object> recordDetails) {
        this.recordId = recordId;
        this.moduleId = moduleId;
        this.layoutId = layoutId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.recordDetails = recordDetails;
    }
}
