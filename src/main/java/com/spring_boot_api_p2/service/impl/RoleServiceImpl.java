package com.spring_boot_api_p2.service.impl;
import com.spring_boot_api_p2.domain.entity.Role;
import com.spring_boot_api_p2.dto.request.RoleRequest;
import com.spring_boot_api_p2.dto.response.RoleResponse;
import com.spring_boot_api_p2.exception.ResourceNotFoundException;
import com.spring_boot_api_p2.mapper.RoleMapper;
import com.spring_boot_api_p2.repository.RoleRepository;
import com.spring_boot_api_p2.service.RoleService;

import com.spring_boot_api_p2.specification.RoleSpecification;
import com.spring_boot_api_p2.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.IllformedLocaleException;
import java.util.List;
import java.util.Map;
// Role , Permission, Menu (parentId, User
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse create(RoleRequest request) {
        request.setName(request.getName().trim());

        boolean existsByName = roleRepository.existsByName(request.getName());
        if (existsByName){
        new IllegalArgumentException("Role Name" + request.getName() +"Already exists");
        }
        Role entity = roleMapper.toEntity(request);

        Role save = roleRepository.save(entity);

        return roleMapper.toResponse(save);
    }

    // Bussinuse Login
    @Override
    public RoleResponse findById(Long id) {
        return roleRepository.findById(id)
                .map(roleMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));

    }

    @Override
    public RoleResponse update(Long id, RoleRequest request) {
        // 1 data មានអត់
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));

        role.setName(request.getName().trim());
        boolean idNot = roleRepository.existsByDescriptionAndIdNot(role.getName(), id);
        if (idNot){
            throw  new IllegalArgumentException("Role name" + request.getName() + "Already exists with id" + id);
        }


        roleMapper.updateEntity(role,request);
        Role save = roleRepository.save(role);
        return roleMapper.toResponse(save);
    }

    @Override
    public void deleteById(Long id) {
        // data មានឬអត់
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", id));
        roleRepository.delete(role);

    }

    @Override
    public List<RoleResponse> getAll() {
        List<Role> all = roleRepository.findAll();

        return all.stream().map(roleMapper::toResponse)
                .toList();

    }

    @Override
    public List<RoleResponse> getAllFilter(Map<String, String> params) {
        Specification<Role> spec = RoleSpecification.builderSpecification(params);

        return roleRepository.findAll(spec).stream()
                .map(roleMapper::toResponse)
                .toList();

    }

    @Override
    public Page<RoleResponse> getAllPagination(Map<String, String> params) {
//        size = Math.min(size,100);
        // Service Role 50
        // Service Permission 50
        // Service User 50
        // util / helper

        int pageLimit = PageUtil.safeParse(params.get(PageUtil.PAGE_LIMIT), PageUtil.DEFAULT_PAGE_LIMIT);
        int pageNumber = PageUtil.safeParse(params.get(PageUtil.PAGE_NUMBER), PageUtil.DEFAULT_PAGE_NUMBER);

        Specification<Role> spec = RoleSpecification.builderSpecification(params);

        Pageable pageable = PageUtil.getPageable(pageNumber, pageLimit);

        Page<Role> all = roleRepository.findAll(spec,pageable);

        return all.map(roleMapper::toResponse);
    }
}