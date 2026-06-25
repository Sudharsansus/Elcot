package in.elcot.avgcxr.platform.user.domain.model;

import java.time.Instant;
import java.util.Set;

/**
 * Pure domain model — no framework imports. Account lockout state is a domain concern (security
 * policy).
 */
public class User {
  private final UserId id;
  private String username,
      email,
      mobileNumber,
      fullName,
      fullNameTamil,
      department,
      designation,
      district;
  private Set<String> roles;
  private UserStatus status;
  private Instant lastLoginAt, createdAt, updatedAt;
  private Instant lockedUntil; // null = not locked; future instant = locked until
  private int failedLoginAttempts; // counter, reset on successful login
  private Instant lastFailedLoginAt;
  private boolean profileCompleted;

  public enum UserStatus {
    ACTIVE,
    INACTIVE,
    LOCKED,
    PENDING_VERIFICATION
  }

  public User(
      UserId id, String username, String email, String mobile, String name, Set<String> roles) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.mobileNumber = mobile;
    this.fullName = name;
    this.roles = Set.copyOf(roles);
    this.status = UserStatus.PENDING_VERIFICATION;
    this.profileCompleted = false;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    this.failedLoginAttempts = 0;
  }

  public UserId getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public String getFullName() {
    return fullName;
  }

  public String getFullNameTamil() {
    return fullNameTamil;
  }

  public Set<String> getRoles() {
    return roles;
  }

  public UserStatus getStatus() {
    return status;
  }

  public String getDistrict() {
    return district;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Instant getLastLoginAt() {
    return lastLoginAt;
  }

  public boolean isProfileCompleted() {
    return profileCompleted;
  }

  public Instant getLockedUntil() {
    return lockedUntil;
  }

  public int getFailedLoginAttempts() {
    return failedLoginAttempts;
  }

  public Instant getLastFailedLoginAt() {
    return lastFailedLoginAt;
  }

  public void activate() {
    this.status = UserStatus.ACTIVE;
    this.updatedAt = Instant.now();
  }

  public void recordLogin() {
    this.lastLoginAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  public void updateProfile(String fn, String fnt, String d) {
    this.fullName = fn;
    this.fullNameTamil = fnt;
    this.district = d;
    this.updatedAt = Instant.now();
  }

  public void completeProfile() {
    this.profileCompleted = true;
    this.updatedAt = Instant.now();
  }

  public void markUpdated() {
    this.updatedAt = Instant.now();
  }

  /** Lock the account until the given instant. Sets status to LOCKED. */
  public void lockUntil(Instant until) {
    this.lockedUntil = until;
    this.status = UserStatus.LOCKED;
    this.updatedAt = Instant.now();
  }

  /** Record a failed login attempt. Returns the new attempt count. */
  public int recordFailedLoginAttempt() {
    this.failedLoginAttempts++;
    this.lastFailedLoginAt = Instant.now();
    this.updatedAt = Instant.now();
    return this.failedLoginAttempts;
  }

  /** Reset failed-attempt counter and clear lockout. Called on successful login. */
  public void resetFailedLogins() {
    this.failedLoginAttempts = 0;
    this.lockedUntil = null;
    if (this.status == UserStatus.LOCKED) {
      this.status = UserStatus.ACTIVE;
    }
    this.updatedAt = Instant.now();
  }

  /** True if the account is currently locked and the lock has not expired. */
  public boolean isLocked() {
    return this.lockedUntil != null && this.lockedUntil.isAfter(Instant.now());
  }
}
