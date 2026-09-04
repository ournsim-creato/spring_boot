package com.spring_boot_api_p2.feature.core.menu.service;

import com.spring_boot_api_p2.feature.core.menu.dto.request.MenuRequest;
import com.spring_boot_api_p2.feature.core.menu.dto.response.MenuResponse;

import java.util.List;

public interface MenuService {

    MenuResponse create(MenuRequest request);

    MenuResponse findById(Long id);

    MenuResponse update(Long id, MenuRequest request);

    void deleteById(Long id);

    List<MenuResponse> getAll();
}
