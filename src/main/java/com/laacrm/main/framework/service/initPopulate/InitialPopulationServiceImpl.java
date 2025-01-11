package com.laacrm.main.framework.service.initPopulate;

import com.laacrm.main.framework.service.role.RoleService;
import com.laacrm.main.framework.service.tenant.TenantService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
@AllArgsConstructor
public class InitialPopulationServiceImpl implements InitialPopulationService {

    private final Logger LOGGER = Logger.getLogger(InitialPopulationServiceImpl.class.getName());

    private final RoleService roleService;
    private final TenantService tenantService;

    @Override
    public void initPopulate() {
        LOGGER.log(Level.INFO, "Initial Data Population started");
        roleService.populateDefaultRoles();
        tenantService.populateTenants();
        LOGGER.log(Level.INFO, "Initial Data Population completed");
    }
}
