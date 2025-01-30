package com.laacrm.main.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class TaskRecordConstants {

    @AllArgsConstructor
    @Getter
    public enum TaskStatus{
        SUCCESS("success", 1),
        FAILURE("failure", 0);
        private final String name;
        private final Integer value;
    }

}
