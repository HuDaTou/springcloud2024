package com.overthinker.cloud.auth.controller;

import com.overthinker.cloud.auth.dto.PermissionDTO;
import com.overthinker.cloud.auth.service.PermissionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Internal controller for permission registration.
 */
@RestController
@RequestMapping("/internal/api/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/register")
    public void registerPermissions(@RequestBody List<PermissionDTO> permissions) {
        permissionService.registerPermissions(permissions);
    }
}
