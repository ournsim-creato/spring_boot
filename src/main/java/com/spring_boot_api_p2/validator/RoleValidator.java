package com.spring_boot_api_p2.validator;

import com.spring_boot_api_p2.dto.request.RoleRequest;
import com.spring_boot_api_p2.repository.RoleRepository;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {

    private final RoleRepository roleRepository;

    RoleValidator(RoleRepository roleRepositoryNew) {
        this.roleRepository = roleRepositoryNew;
    }

    //Call only this method
    public void validate(Long id, RoleRequest request){
        validateDuplicateName(request.getName(), id);
    }

    // Check Duplicate Name
    private void validateDuplicateName(String name, Long id) {
        boolean exists = (id == null)
                ? roleRepository.existsByName(name)
                : roleRepository.existsByNameAndIdNot(name, id);

        if (exists) {
            throw new IllegalArgumentException(
                    "Role name '" + name + "' already exists in validator");
        }
    }

}