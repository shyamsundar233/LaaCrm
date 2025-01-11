package com.laacrm.main.framework.repo;

import com.laacrm.main.framework.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<Users, Long> {
}
