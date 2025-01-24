package com.laacrm.main.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ScheduledTasksConstants {
    
    @Getter
    @AllArgsConstructor
    public enum TaskStatus {;
        private final String name;
        private final Integer value;
    }
    
}
