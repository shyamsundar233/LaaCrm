package com.laacrm.main.core.entity;

import com.laacrm.main.framework.service.tenant.TenantService;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class PKGenerator implements IdentifierGenerator {

    private final TenantService tenantService;

    @Autowired
    public PKGenerator(@Lazy TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) {
        return tenantService.getCurrentAvailablePK();
    }
}
