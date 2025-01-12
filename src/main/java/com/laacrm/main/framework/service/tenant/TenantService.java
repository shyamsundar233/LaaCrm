package com.laacrm.main.framework.service.tenant;

import com.laacrm.main.framework.entities.Ranges;
import com.laacrm.main.framework.entities.Tenant;

public interface TenantService {

    void populateTenants();

    Tenant getNextAvailableTenant();

    Ranges getNextAvailableRange();

    Tenant saveTenant(Tenant tenant);

    Ranges saveRange(Ranges range);

    Ranges allocateRangeForTenant(Tenant tenant);

    Long getCurrentAvailablePK();
}
