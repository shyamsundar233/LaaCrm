package com.laacrm.main.framework.repo;

import com.laacrm.main.framework.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

    @Query("SELECT SUBSTRING(u.userCode, 5) FROM Users u WHERE u.userCode LIKE 'USR-%' ORDER BY SUBSTRING(u.userCode, 5) DESC")
    Long findMaxUserCodeSuffix();

}
