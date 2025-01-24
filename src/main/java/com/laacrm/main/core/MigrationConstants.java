package com.laacrm.main.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class MigrationConstants {

    @Getter
    @AllArgsConstructor
    public enum MigrationStatus {
        IN_QUEUE("inQueue", 1),
        ACTIVE("active", 2),
        FAILED("failed", 3);
        private final String name;
        private final Integer value;
    }

}
