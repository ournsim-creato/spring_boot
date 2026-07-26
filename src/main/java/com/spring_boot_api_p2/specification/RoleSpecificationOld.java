package com.spring_boot_api_p2.specification;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import com.spring_boot_api_p2.domain.entity.Role;
public class RoleSpecificationOld {

    public static Specification<Role> hasNameContaining(String name) {
        return (root, query, criteriaBuilder) -> {

            if (name == null || name.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Role> builderSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {

            Predicate predicate = criteriaBuilder.conjunction();

            if (params.containsKey("name") && params.get("name") != null && !params.get("name").isEmpty()){

                 predicate =criteriaBuilder.
                         and(
                                 predicate, hasNameContaining(params.get("name")).toPredicate(root,query,criteriaBuilder)
                         );

            }
            return predicate;
        };
    }
}