package com.laacrm.main.framework.repo;

import com.laacrm.main.framework.entities.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepo extends JpaRepository<Tenant, Long> {
}
