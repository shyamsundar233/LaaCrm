package com.laacrm.main.framework.repo;

import com.laacrm.main.framework.entities.Ranges;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RangeRepo extends JpaRepository<Ranges, Long> {

    Optional<Ranges> findFirstByTenantIsNull();

}
