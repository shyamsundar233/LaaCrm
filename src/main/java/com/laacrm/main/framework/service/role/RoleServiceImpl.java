package com.laacrm.main.framework.service.role;

import com.laacrm.main.framework.entities.Role;
import com.laacrm.main.framework.exception.FrameworkException;
import com.laacrm.main.framework.repo.RoleRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final Logger LOGGER = Logger.getLogger(RoleServiceImpl.class.getName());

    private final RoleRepo roleRepo;

    @Override
    public List<Role> getAllRoles() {
        return List.of();
    }

    @Override
    public Role getRoleById(Long roleId) {
        return null;
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleRepo.findByRoleName(roleName).orElse(null);
    }

    @Override
    public Role saveRole(Role role) {
        createRoleValidation(role);
        role = roleRepo.save(role);
        LOGGER.log(Level.INFO, "Role saved: {0}", role);
        return role;
    }

    @Override
    public void deleteRole(Long roleId) {

    }

    private void createRoleValidation(Role role) {
        int exceptionCode = -1;
        exceptionCode = role == null ? 1 : exceptionCode;
        exceptionCode = exceptionCode == -1 && role.getRoleName() == null ? 2 : exceptionCode;
        exceptionCode = exceptionCode == -1 && role.getRoleName().isEmpty() ? 3 : exceptionCode;
        switch (exceptionCode) {
            case 1:
                LOGGER.log(Level.SEVERE, "Role Pojo is null");
                throw new FrameworkException(HttpStatus.BAD_REQUEST.value(), "Role pojo is null");
            case 2:
                LOGGER.log(Level.SEVERE, "Role Name is null");
                throw new FrameworkException(HttpStatus.BAD_REQUEST.value(), "Role name cannot be null");
            case 3:
                LOGGER.log(Level.SEVERE, "Role Name is empty");
                throw new FrameworkException(HttpStatus.BAD_REQUEST.value(), "Role name cannot be empty");
        }
    }
}
