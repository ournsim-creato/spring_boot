package com.spring_boot_api_p2.feature.core.menu.controller;

import com.spring_boot_api_p2.feature.core.menu.dto.request.MenuRequest;
import com.spring_boot_api_p2.feature.core.menu.dto.response.MenuResponse;
import com.spring_boot_api_p2.feature.core.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // CREATE
    @PostMapping
    public ResponseEntity<MenuResponse> create(
            @RequestBody MenuRequest request
    ) {
        return ResponseEntity.ok(
                menuService.create(request)
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<MenuResponse>> getAll() {
        return ResponseEntity.ok(
                menuService.getAll()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                menuService.findById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<MenuResponse> update(
            @PathVariable Long id,
            @RequestBody MenuRequest request
    ) {
        return ResponseEntity.ok(
                menuService.update(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        menuService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
