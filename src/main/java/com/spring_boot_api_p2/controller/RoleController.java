package com.spring_boot_api_p2.controller;

import com.spring_boot_api_p2.base.BaseApi;
import com.spring_boot_api_p2.base.BaseApiPagination;
import com.spring_boot_api_p2.dto.pagination.PageDTO;
import com.spring_boot_api_p2.dto.request.RoleRequest;
import com.spring_boot_api_p2.dto.response.RoleResponse;
import com.spring_boot_api_p2.service.RoleService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/roles")
public class RoleController {
    //Liquibase
    // dependency injection
    private final RoleService roleService;

    RoleController(RoleService roleServiceInject) {
        this.roleService = roleServiceInject;
    }

    // annotation handle http request & response
    //? មានន័យថា Unknown Type Bad Practice
    // មានន័យថា know type ជា RoleResponse
    // បើសិនជាយើងដាក RoleResponse Good Practice
    @PostMapping
    public ResponseEntity<RoleResponse> create(@Valid @RequestBody RoleRequest request) {
        //name require
        // mapper នៅក្នុង Controller  Good Practice
        // តួរនាទីតែ 2 គត់ 1 Handle Request 2 Handle Response
        // មិនប្រើ mapper នៅក្នុង Controller
        RoleResponse role = roleService.create(request);
        return  ResponseEntity.ok(role);
    }

    @GetMapping("{id}")
    public ResponseEntity<RoleResponse> getById(@PathVariable Long id){
        RoleResponse response = roleService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<RoleResponse> update(@PathVariable Long id, @RequestBody RoleRequest request){
        RoleResponse response = roleService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.ok(null);
    }
    // get all data , get pagination , mapstruct
    @GetMapping("all")
    public ResponseEntity<?> getAll(){
        List<RoleResponse> response = roleService.getAll();
        return  ResponseEntity.ok(
                BaseApi.<List<RoleResponse>>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .data(response)
                        .build()
        );
    }
    @GetMapping("all-filter")
    public ResponseEntity<?> getAllFilterByName(@RequestParam(required = false) Map<String, String> params){
        List<RoleResponse> response = roleService.getAllFilter(params);
        return  ResponseEntity.ok(response);
    }
    //    @GetMapping("pagination")
//    public ResponseEntity<?> pagination(@RequestParam(defaultValue = "1") int page,
//                                        @RequestParam(required = false) int size){
//        Page<RoleResponse> allPagination = roleService.getAllPagination(page, size);
//        PageDTO pageDTO = new PageDTO(allPagination);
//        return  ResponseEntity.ok(allPagination);
//    }
    // map key => vale
    //Map Collection Framework  Key-Value Pairs
    @GetMapping("pagination-okay")
    public ResponseEntity<?> pagination1(@RequestParam(required = false) Map<String, String> params){
        Page<RoleResponse> allPagination = roleService.getAllPagination(params);
        PageDTO pageDTO = new PageDTO(allPagination);


        return  ResponseEntity.ok(
                BaseApiPagination.<RoleResponse>builder()
                        .status(true)
                        .code(HttpStatus.OK.value())
                        .message("Success")
                        .timestamp(LocalDateTime.now())
                        .pagination(pageDTO.getPagination())
                        .data((List<RoleResponse>) pageDTO.getItems())
                        .build()
        );
    }
}