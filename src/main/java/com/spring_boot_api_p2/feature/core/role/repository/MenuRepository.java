package com.spring_boot_api_p2.feature.core.role.repository;

import com.spring_boot_api_p2.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}