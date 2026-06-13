
package com.overthinker.cloud.auditlog.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.util.Optional;

/**
 * A utility class to retrieve user information from the request context.
 * This class assumes that user information is passed in the request headers
 * from the API gateway.
 */
public final class UserContextHolder {

    private static final String USER_ID_HEADER = "X-User-Id";

    private UserContextHolder() {
        // Private constructor to prevent instantiation
    }

    /**
     * Retrieves the user ID from the "X-User-Id" header of the current request.
     *
     * @return An Optional containing the user ID if the header is present, otherwise an empty Optional.
     */
    public static Optional<String> getUserId() {
        return getRequest()
                .map(request -> request.getHeader(USER_ID_HEADER));
    }

    /**
     * A convenience method that returns the user ID directly, or a default value if not found.
     *
     * @param defaultValue The value to return if the user ID is not present in the request.
     * @return The user ID from the header, or the default value.
     */
    public static String getUserIdOrDefault(String defaultValue) {
        return getUserId().orElse(defaultValue);
    }

    /**
     * Gets the current HttpServletRequest from the RequestContextHolder.
     *
     * @return An Optional containing the HttpServletRequest if it exists, otherwise an empty Optional.
     */
    private static Optional<HttpServletRequest> getRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }
}
