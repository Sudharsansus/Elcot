package in.elcot.avgcxr.common.infrastructure.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("rbac")
public class RbacExpressionHandler {
    public boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
    public boolean hasAnyRole(String... roles) {
        for (String role : roles) { if (hasRole(role)) return true; }
        return false;
    }
    public boolean isOwner(String userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getName().equals(userId);
    }
}

