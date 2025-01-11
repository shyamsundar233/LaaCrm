package com.laacrm.main.framework.service.tenant;

import com.laacrm.main.framework.entities.Ranges;
import com.laacrm.main.framework.entities.Tenant;
import com.laacrm.main.framework.repo.RangeRepo;
import com.laacrm.main.framework.repo.TenantRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
@AllArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final Logger LOGGER = Logger.getLogger(TenantServiceImpl.class.getName());

    private final TenantRepo tenantRepo;
    private final RangeRepo rangeRepo;

    @Override
    public void populateTenants() {
        if(tenantRepo.count() == 0 && rangeRepo.count() == 0) {
            LOGGER.log(Level.INFO, "Tenant Initial Data is getting populated");
            Long startRange = 100000000000000L;
            Long endRange = 999999999999999999L;
            Long offset = 10000000000000L;
            while (startRange < endRange) {
                Ranges range = new Ranges();
                range.setStartRange(startRange);
                range.setEndRange((startRange + offset) - 1);
                rangeRepo.save(range);
                Tenant tenant = new Tenant();
                tenant.setCurrentUniqueId(startRange);
                tenant.setIsActive(Boolean.FALSE);
                tenantRepo.save(tenant);
                startRange+=offset;
            }
            LOGGER.log(Level.INFO, "Tenant Initial Data populated successfully");
        }
    }

}
