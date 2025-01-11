package com.laacrm.main.framework.service;

import lombok.Getter;

public class FrameworkConstants {

    @Getter
    public enum DefaultRole{
        ADMIN("ROLE_ADMIN"),
        USER("ROLE_USER");

        private final String roleName;

        DefaultRole(String roleName){
            this.roleName = roleName;
        }
    }

}
