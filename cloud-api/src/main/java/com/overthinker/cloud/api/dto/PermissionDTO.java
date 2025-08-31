package com.overthinker.cloud.api.dto;

/**
 * Data Transfer Object for transporting permission information.
 *
 * @param category      The category of the permission (e.g., "User Management"), from @Tag.
 * @param name          The name of the permission (e.g., "Get user list"), from @Operation.
 * @param httpMethod    The HTTP method (e.g., "GET", "POST").
 * @param path          The full request path (e.g., "/user-service/api/v1/users").
 */
public record PermissionDTO(
        String category,
        String name,
        String httpMethod,
        String path
) {
}
