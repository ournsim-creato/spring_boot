package com.spring_boot_api_p2.feature.core.permission.repository;

import com.spring_boot_api_p2.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
