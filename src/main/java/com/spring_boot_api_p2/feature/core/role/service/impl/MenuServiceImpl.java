package com.spring_boot_api_p2.feature.core.role.service.impl;

import com.spring_boot_api_p2.domain.entity.Menu;
import com.spring_boot_api_p2.dto.request.MenuRequest;
import com.spring_boot_api_p2.dto.response.MenuResponse;
import com.spring_boot_api_p2.exception.ResourceNotFoundException;
import com.spring_boot_api_p2.feature.core.role.repository.MenuRepository;
import com.spring_boot_api_p2.feature.core.role.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public MenuResponse create(MenuRequest request) {

        Menu menu = new Menu();

        menu.setName(request.getName());
        menu.setPath(request.getPath());
        menu.setRedirect(request.getRedirect());
        menu.setAlwaysShow(request.getAlwaysShow());
        menu.setHidden(request.getHidden());
        menu.setTitle(request.getTitle());
        menu.setIcon(request.getIcon());
        menu.setNoCache(request.getNoCache());
        menu.setTitleKey(request.getTitleKey());
        menu.setLink(request.getLink());
        menu.setComponent(request.getComponent());
        menu.setSortOrder(request.getSortOrder());

        if (request.getParentId() != null) {

            Menu parent = menuRepository.findById(request.getParentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Parent Menu",
                                    request.getParentId()
                            )
                    );

            menu.setParent(parent);
        }

        Menu saved = menuRepository.save(menu);

        return toResponse(saved);
    }

    @Override
    public MenuResponse findById(Long id) {

        Menu menu = menuRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Menu", id)
                );

        return toResponse(menu);
    }

    @Override
    public MenuResponse update(Long id, MenuRequest request) {

        Menu menu = menuRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Menu", id)
                );

        menu.setName(request.getName());
        menu.setPath(request.getPath());
        menu.setRedirect(request.getRedirect());
        menu.setAlwaysShow(request.getAlwaysShow());
        menu.setHidden(request.getHidden());
        menu.setTitle(request.getTitle());
        menu.setIcon(request.getIcon());
        menu.setNoCache(request.getNoCache());
        menu.setTitleKey(request.getTitleKey());
        menu.setLink(request.getLink());
        menu.setComponent(request.getComponent());
        menu.setSortOrder(request.getSortOrder());

        if (request.getParentId() != null) {

            Menu parent = menuRepository.findById(request.getParentId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Parent Menu",
                                    request.getParentId()
                            )
                    );

            menu.setParent(parent);

        } else {
            menu.setParent(null);
        }

        Menu updated = menuRepository.save(menu);

        return toResponse(updated);
    }

    @Override
    public void deleteById(Long id) {

        Menu menu = menuRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Menu", id)
                );

        menuRepository.delete(menu);
    }

    @Override
    public List<MenuResponse> getAll() {

        return menuRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MenuResponse toResponse(Menu menu) {

        MenuResponse response = new MenuResponse();

        response.setId(menu.getId());
        response.setName(menu.getName());
        response.setPath(menu.getPath());
        response.setRedirect(menu.getRedirect());
        response.setAlwaysShow(menu.getAlwaysShow());
        response.setHidden(menu.getHidden());
        response.setTitle(menu.getTitle());
        response.setIcon(menu.getIcon());
        response.setNoCache(menu.getNoCache());
        response.setTitleKey(menu.getTitleKey());
        response.setLink(menu.getLink());
        response.setComponent(menu.getComponent());
        response.setSortOrder(menu.getSortOrder());

        if (menu.getParent() != null) {
            response.setParentId(menu.getParent().getId());
        }

        return response;
    }
}