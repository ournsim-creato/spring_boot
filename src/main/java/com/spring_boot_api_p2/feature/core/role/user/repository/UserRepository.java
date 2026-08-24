package com.spring_boot_api_p2.feature.core.role.user.repository;

import com.spring_boot_api_p2.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {


    Optional<User> findByUsername(String username);

    // បន្ថែម Method ទាំងពីរនេះ សម្រាប់ស្វែងរកដោយមិនខ្វល់ពីអក្សរធំ/តូច
    Optional<User> findByUsernameIgnoreCase(String username);

    /** Cheap existence check for create validation without loading the full entity. */
    boolean existsByUsername(String username);
    // បន្ថែម Method នេះសម្រាប់ Check Username ពេល Create
    boolean existsByUsernameIgnoreCase(String username);
}