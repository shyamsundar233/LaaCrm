package com.laacrm.main.core;

import com.laacrm.main.framework.entities.Tenant;
import com.laacrm.main.framework.entities.Users;

public class AuthThreadLocal {

    private static final ThreadLocal<Users> currentUser = new ThreadLocal<>();

    private static final ThreadLocal<Tenant> currentTenant = new ThreadLocal<>();

    public static void setCurrentUser(Users users) {
        currentUser.set(users);
    }

    public static Users getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentTenant(Tenant tenant) {
        currentTenant.set(tenant);
    }

    public static Tenant getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clearAll() {
        currentUser.remove();
    }

}
