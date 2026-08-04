package com.spring_boot_api_p2.feature.core.role.repository;

import com.spring_boot_api_p2.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    boolean existsByName(String name);
    boolean existsByDescriptionAndIdNot(String name, Long id);
    boolean existsByNameAndIdNot(String name, Long id);
}