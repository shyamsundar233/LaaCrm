package com.laacrm.main.core.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class Record {

    private Long recordId;

    private Long moduleId;

    private Long layoutId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Map<String, Object> recordDetails;

    public Record() {}

    public Record(Long moduleId, Long layoutId, Map<String, Object> recordDetails) {
        this.moduleId = moduleId;
        this.layoutId = layoutId;
        this.recordDetails = recordDetails;
    }

    public Record(Long recordId, Long moduleId, Long layoutId, Map<String, Object> recordDetails) {
        this.recordId = recordId;
        this.moduleId = moduleId;
        this.layoutId = layoutId;
        this.recordDetails = recordDetails;
    }
}
