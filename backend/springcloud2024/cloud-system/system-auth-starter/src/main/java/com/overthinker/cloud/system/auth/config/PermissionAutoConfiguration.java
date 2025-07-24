package com.overthinker.cloud.system.auth.config;

import com.overthinker.cloud.api.client.AuthClient;
import com.overthinker.cloud.system.auth.service.PermissionRegistryClient;
import com.overthinker.cloud.system.auth.service.PermissionScanner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for the permission scanning and registration starter.
 */
@Configuration
@EnableFeignClients(basePackages = "com.overthinker.cloud.api.client")
@ConditionalOnProperty(name = "permission.scanner.enabled", havingValue = "true", matchIfMissing = true)
public class PermissionAutoConfiguration {

    @Bean
    public PermissionScanner permissionScanner(ApplicationContext applicationContext) {
        return new PermissionScanner(applicationContext);
    }

    @Bean
    public PermissionRegistryClient permissionRegistryClient(AuthClient authClient) {
        return new PermissionRegistryClient(authClient);
    }

    @Bean
    public ApplicationRunner permissionRegistrationRunner(PermissionScanner scanner, PermissionRegistryClient client) {
        return args -> {
            var permissions = scanner.scanPermissions();
            client.registerPermissions(permissions);
        };
    }
}
