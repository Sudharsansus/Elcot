package in.elcot.avgcxr.platform.user.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.assertj.core.api.Assertions.*;

@DisplayName("User domain")
class UserTest {

    @Nested
    @DisplayName("registration")
    class Registration {
        @Test
        @DisplayName("starts in PENDING_VERIFICATION state")
        void startsPending() {
            var user = new User(
                UserId.generate(),
                "testuser",
                "test@example.com",
                "+919876543210",
                "Test User",
                java.util.Set.of("PUBLIC")
            );
            assertThat(user.getStatus()).isEqualTo(User.UserStatus.PENDING_VERIFICATION);
            assertThat(user.isProfileCompleted()).isFalse();
        }

        @Test
        @DisplayName("activates on activate()")
        void activatesOnActivate() {
            var user = new User(
                UserId.generate(),
                "u", "u@x.com", "+91", "U",
                java.util.Set.of("PUBLIC")
            );
            user.activate();
            assertThat(user.getStatus()).isEqualTo(User.UserStatus.ACTIVE);
        }
    }

    @Nested
    @DisplayName("login tracking")
    class LoginTracking {
        @Test
        @DisplayName("recordLogin() sets lastLoginAt to the current time")
        void recordLoginUpdatesTimestamp() {
            var user = new User(
                UserId.generate(), "u", "u@x.com", "+91", "U",
                java.util.Set.of("PUBLIC")
            );
            user.recordLogin();
            // Deterministic: recordLogin() must stamp a fresh "now" value (no Thread.sleep needed).
            assertThat(user.getLastLoginAt())
                .isNotNull()
                .isBetween(java.time.Instant.now().minusSeconds(5), java.time.Instant.now().plusSeconds(1));
        }
    }

    @Nested
    @DisplayName("profile updates")
    class ProfileUpdates {
        @Test
        @DisplayName("completeProfile sets flag true")
        void completeProfile() {
            var user = new User(
                UserId.generate(), "u", "u@x.com", "+91", "U",
                java.util.Set.of("PUBLIC")
            );
            user.completeProfile();
            assertThat(user.isProfileCompleted()).isTrue();
        }

        @Test
        @DisplayName("updateProfile changes name fields")
        void updateProfile() {
            var user = new User(
                UserId.generate(), "u", "u@x.com", "+91", "Old",
                java.util.Set.of("PUBLIC")
            );
            user.updateProfile("New Name", "\u0baa\u0bc1\u0ba4\u0bbf\u0baf \u0baa\u0bc6\u0baf\u0bb0\u0bcd", "Chennai");
            assertThat(user.getFullName()).isEqualTo("New Name");
            assertThat(user.getFullNameTamil()).isEqualTo("\u0baa\u0bc1\u0ba4\u0bbf\u0baf \u0baa\u0bc6\u0baf\u0bb0\u0bcd");
            assertThat(user.getDistrict()).isEqualTo("Chennai");
        }
    }

    @Nested
    @DisplayName("role immutability")
    class RoleImmutability {
        @Test
        @DisplayName("roles set is unmodifiable from outside")
        void rolesAreDefensiveCopied() {
            var original = new java.util.HashSet<String>();
            original.add("PUBLIC");
            var user = new User(UserId.generate(), "u", "u@x.com", "+91", "U", original);
            original.add("ADMIN"); // mutate the source set
            assertThat(user.getRoles()).containsExactly("PUBLIC");
        }
    }

    @Nested
    @DisplayName("identity")
    class Identity {
        @Test
        @DisplayName("UserId.generate() produces unique IDs")
        void generatedIdsAreUnique() {
            var a = UserId.generate();
            var b = UserId.generate();
            assertThat(a).isNotEqualTo(b);
            assertThat(a.value()).isNotEqualTo(b.value());
        }

        @Test
        @DisplayName("UserId.of(String) parses a UUID")
        void userIdParsesUuid() {
            var s = "123e4567-e89b-12d3-a456-426614174000";
            var id = UserId.of(s);
            assertThat(id.toString()).isEqualTo(s);
        }
    }
}
