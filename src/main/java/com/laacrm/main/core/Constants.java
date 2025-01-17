package com.laacrm.main.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Constants {

    public static class ModuleConstants {

        public static final String MODULE_NAME_REGEX = "^[a-zA-Z0-9$/.\\s]{2,50}$";

        @Getter
        @AllArgsConstructor
        public enum ModuleType{
            DEFAULT("default", 1);
            private final String name;
            private final Integer value;

            public static boolean isValidType(Integer value){
                for(ModuleType moduleType : ModuleType.values()){
                    if(moduleType.value.equals(value)){
                        return true;
                    }
                }
                return false;
            }
        }

        @Getter
        @AllArgsConstructor
        public enum ModuleStatus{
            ACTIVE("active", 1);
            private final String name;
            private final Integer value;

            public static boolean isValidStatus(Integer value){
                for(ModuleStatus moduleStatus : ModuleStatus.values()){
                    if(moduleStatus.value.equals(value)){
                        return true;
                    }
                }
                return false;
            }
        }
    }

}
