package com.example.Deal.Utils;

import com.example.Deal.DTO.request.DealSearch;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

/**
 * Provides means to obtain user access level data.
 */
public final class RoleAccess {

    private RoleAccess() {}

    /**
     * Checks if user has access to some method.
     *
     * @param search contains filtering fields
     * @return result of check (true - access permitted, false - access denied)
     */
    public static boolean hasAccess(DealSearch search) {
        List<String> roles = getRoles();
        if (roles.contains("DEAL_SUPERUSER") || roles.contains("SUPERUSER")) {
            return true;
        } else if (roles.contains("CREDIT_USER")) {
            if (search.getType().isEmpty()) {
                return true;
            } else if (search.getType().equals(Collections.singletonList("CREDIT"))) {
                return true;
            }
        } else if (roles.contains("OVERDRAFT_USER")) {
            if (search.getType().isEmpty()) {
                return true;
            } else if (search.getType().equals(Collections.singletonList("OVERDRAFT"))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves all user roles as String list.
     * Extract data from Security context holder.
     *
     * @return extracted data
     */
    private static List<String> getRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().map(Object::toString).toList();
    }

}
