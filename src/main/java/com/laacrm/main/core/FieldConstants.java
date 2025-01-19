package com.laacrm.main.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class FieldConstants {

    public static final String FIELD_NAME_REGEX = "^[a-zA-Z0-9$/.\\s]{2,50}$";

    @Getter
    @AllArgsConstructor
    public enum FieldType{
        SYSTEM_FIELD("systemField", 1),
        USER_FIELD("userField", 2);

        private final String name;
        private final Integer value;

        public static boolean isValidType(Integer value){
            for(FieldType fieldType : FieldType.values()){
                if(fieldType.value.equals(value)){
                    return true;
                }
            }
            return false;
        }
    }

}
