package in.elcot.avgcxr.platformsecurity.rbac;

import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Set;

@Service
public class RbacService {

    public boolean hasPermission(Role role, Permission permission) {
        return getPermissionsForRole(role).contains(permission);
    }

    public Set<Permission> getPermissionsForRole(Role role) {
        return switch (role) {
            case SUPER_ADMIN -> EnumSet.allOf(Permission.class);
            case ADMIN -> EnumSet.of(
                    Permission.SCHEME_READ, Permission.SCHEME_WRITE, Permission.SCHEME_PUBLISH,
                    Permission.APPLICATION_READ, Permission.APPLICATION_WRITE, Permission.APPLICATION_REVIEW, Permission.APPLICATION_APPROVE,
                    Permission.USER_READ, Permission.USER_WRITE,
                    Permission.WORKFLOW_MANAGE,
                    Permission.REPORT_READ, Permission.REPORT_EXPORT,
                    Permission.NOTIFICATION_SEND,
                    Permission.FILE_UPLOAD, Permission.FILE_DELETE,
                    Permission.ANALYTICS_READ,
                    Permission.HELPDESK_MANAGE,
                    Permission.GRIEVANCE_MANAGE,
                    Permission.ECOSYSTEM_READ, Permission.ECOSYSTEM_WRITE
            );
            case DISTRICT_OFFICER -> EnumSet.of(
                    Permission.APPLICATION_READ, Permission.APPLICATION_REVIEW,
                    Permission.USER_READ,
                    Permission.REPORT_READ,
                    Permission.ANALYTICS_READ,
                    Permission.HELPDESK_MANAGE,
                    Permission.GRIEVANCE_MANAGE,
                    Permission.ECOSYSTEM_READ
            );
            case NODAL_OFFICER -> EnumSet.of(
                    Permission.APPLICATION_READ, Permission.APPLICATION_REVIEW, Permission.APPLICATION_APPROVE,
                    Permission.USER_READ,
                    Permission.REPORT_READ, Permission.REPORT_EXPORT,
                    Permission.ANALYTICS_READ,
                    Permission.ECOSYSTEM_READ
            );
            case APPLICANT -> EnumSet.of(
                    Permission.SCHEME_READ,
                    Permission.APPLICATION_READ, Permission.APPLICATION_WRITE,
                    Permission.USER_READ,
                    Permission.FILE_UPLOAD,
                    Permission.ECOSYSTEM_READ
            );
            case PUBLIC -> EnumSet.of(
                    Permission.SCHEME_READ,
                    Permission.ECOSYSTEM_READ
            );
        };
    }

    public boolean hasAnyPermission(Role role, Permission... permissions) {
        Set<Permission> rolePerms = getPermissionsForRole(role);
        for (Permission p : permissions) {
            if (rolePerms.contains(p)) return true;
        }
        return false;
    }
}