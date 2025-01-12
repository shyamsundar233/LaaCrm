package com.laacrm.main.core.repo;

import com.laacrm.main.core.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleRepo extends JpaRepository<Module, Long> {
}
