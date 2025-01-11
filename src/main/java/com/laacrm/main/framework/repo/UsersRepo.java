package com.laacrm.main.framework.repo;

import com.laacrm.main.framework.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

}
