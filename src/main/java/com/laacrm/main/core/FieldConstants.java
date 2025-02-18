package com.laacrm.main.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class FieldConstants {

    public static final String FIELD_NAME_REGEX = "^[a-zA-Z0-9$/.\\s]{2,50}$";

    @Getter
    @AllArgsConstructor
    public enum FieldType{
        SINGLE_LINE("single_line", 1),
        MULTI_LINE("multi_line", 2),
        NUMBER("number", 3),
        DROPDOWN("dropdown", 4),
        CHECKBOX("checkbox", 5),
        DEFAULT_NAME("default_name", 6),
        DEFAULT_OWNER("default_owner", 7),
        EMAIL("email", 8),
        PHONE("phone", 9),
        URL("url", 10),
        DATE("date", 11),
        CURRENCY("currency", 12);

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
