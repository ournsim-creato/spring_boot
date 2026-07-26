package com.spring_boot_api_p2.domain.repository;

import com.spring_boot_api_p2.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}