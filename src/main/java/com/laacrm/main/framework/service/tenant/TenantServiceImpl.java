package com.laacrm.main.framework.service.tenant;

import com.laacrm.main.core.AuthThreadLocal;
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

    @Override
    public Tenant getNextAvailableTenant() {
        return tenantRepo.findFirstByIsActiveFalse().orElse(null);
    }

    @Override
    public Ranges getNextAvailableRange(){
        return rangeRepo.findFirstByTenantIsNull().orElse(null);
    }

    @Override
    public Tenant saveTenant(Tenant tenant) {
        return tenantRepo.save(tenant);
    }

    @Override
    public Ranges saveRange(Ranges range) {
        return rangeRepo.save(range);
    }

    @Override
    public Ranges allocateRangeForTenant(Tenant tenant) {
        Ranges range = getNextAvailableRange();
        range.setTenant(tenant);
        saveRange(range);
        return null;
    }

    @Override
    public Long getCurrentAvailablePK() {
        Tenant currentTenant = AuthThreadLocal.getCurrentTenant();
        Ranges currentRange = rangeRepo.findTopByOrderByTenantDesc();
        Long currentPK = currentTenant.getCurrentUniqueId();
        if(currentPK + 1 > currentRange.getEndRange()){
            LOGGER.log(Level.INFO, "Range exhausted for Tenant {0} :: Allocating New Range", currentTenant.getTenantId());
            Ranges nextRange = getNextAvailableRange();
            nextRange.setTenant(currentTenant);
            saveRange(nextRange);
            LOGGER.log(Level.INFO, "New Range Allocated for Tenant {0}", currentTenant.getTenantId());
            currentTenant.setCurrentUniqueId(nextRange.getStartRange());
        }else{
            currentTenant.setCurrentUniqueId(currentPK + 1);
        }
        saveTenant(currentTenant);
        return currentPK;
    }

}
