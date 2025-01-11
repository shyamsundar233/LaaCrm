package com.laacrm.main.framework.service.role;

import com.laacrm.main.framework.entities.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    Role getRoleById(Long roleId);

    Role getRoleByName(String roleName);

    Role saveRole(Role role);

    void deleteRole(Long roleId);

    void populateDefaultRoles();
}
