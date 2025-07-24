package com.overthinker.cloud.system.auth.service;

import com.overthinker.cloud.api.client.AuthClient;
import com.overthinker.cloud.api.dto.PermissionDTO;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Client to register permissions with the central authentication service using Feign.
 */
public class PermissionRegistryClient {

    private static final Logger LOGGER = Logger.getLogger(PermissionRegistryClient.class.getName());

    private final AuthClient authClient;

    public PermissionRegistryClient(AuthClient authClient) {
        this.authClient = authClient;
    }

    public void registerPermissions(List<PermissionDTO> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            LOGGER.info("No permissions found to register.");
            return;
        }

        try {
            LOGGER.info("Registering " + permissions.size() + " permissions via Feign client.");
            authClient.registerPermissions(permissions);
            LOGGER.info("Permissions registered successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not register permissions with auth service via Feign", e);
        }
    }
}
