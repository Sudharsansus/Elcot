package in.elcot.avgcxr.platform.user.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}), @UniqueConstraint(columnNames = {"email"}), @UniqueConstraint(columnNames = {"mobile_number"})})
public class UserEntity {
    @Id @Column(name = "id", columnDefinition = "UUID") private UUID id;
    @Column(name = "username", nullable = false, length = 100) private String username;
    @Column(name = "email", nullable = false, length = 255) private String email;
    @Column(name = "mobile_number", nullable = false, length = 15) private String mobileNumber;
    @Column(name = "full_name", nullable = false, length = 200) private String fullName;
    @Column(name = "full_name_tamil", length = 300) private String fullNameTamil;
    @Column(name = "is_active", nullable = false) private boolean active;
    @Column(name = "is_verified", nullable = false) private boolean verified;
    @Column(name = "last_login_at") private java.time.Instant lastLoginAt;
    @Column(name = "failed_login_attempts", nullable = false) private int failedLoginAttempts;
    @Column(name = "locked_until") private java.time.Instant lockedUntil;
    @Column(name = "last_failed_login_at") private java.time.Instant lastFailedLoginAt;
    @Column(name = "password_hash", length = 255) private String passwordHash;
    @ElementCollection(fetch = FetchType.EAGER) @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id")) @Column(name = "role") private Set<String> roles;
    @Column(name = "status", nullable = false, length = 30) private String status;
    @Column(name = "department", length = 100) private String department;
    @Column(name = "designation", length = 100) private String designation;
    @Column(name = "district", length = 100) private String district;
    @Column(name = "profile_completed", nullable = false) private boolean profileCompleted;
    @Column(name = "created_at", nullable = false) private Instant createdAt;
    @Column(name = "updated_at", nullable = false) private Instant updatedAt;
    public UUID getId() { return id; } public void setId(UUID v) { id = v; }
    public String getUsername() { return username; } public void setUsername(String v) { username = v; }
    public String getEmail() { return email; } public void setEmail(String v) { email = v; }
    public String getMobileNumber() { return mobileNumber; } public void setMobileNumber(String v) { mobileNumber = v; }
    public String getFullName() { return fullName; } public void setFullName(String v) { fullName = v; }
    public String getFullNameTamil() { return fullNameTamil; } public void setFullNameTamil(String v) { fullNameTamil = v; }
    public Set<String> getRoles() { return roles; } public void setRoles(Set<String> v) { roles = v; }
    public String getStatus() { return status; } public void setStatus(String v) { status = v; }
    public String getDepartment() { return department; } public void setDepartment(String v) { department = v; }
    public String getDesignation() { return designation; } public void setDesignation(String v) { designation = v; }
    public String getDistrict() { return district; } public void setDistrict(String v) { district = v; }
    public boolean isProfileCompleted() { return profileCompleted; } public void setProfileCompleted(boolean v) { profileCompleted = v; }
    public Instant getCreatedAt() { return createdAt; } public void setCreatedAt(Instant v) { createdAt = v; }
    public Instant getUpdatedAt() { return updatedAt; } public void setUpdatedAt(Instant v) { updatedAt = v; }
    public boolean isActive() { return active; } public void setActive(boolean v) { active = v; }
    public boolean isVerified() { return verified; } public void setVerified(boolean v) { verified = v; }
    public Instant getLastLoginAt() { return lastLoginAt; } public void setLastLoginAt(Instant v) { lastLoginAt = v; }
    public String getPasswordHash() { return passwordHash; } public void setPasswordHash(String v) { passwordHash = v; }
    public int getFailedLoginAttempts() { return failedLoginAttempts; } public void setFailedLoginAttempts(int v) { failedLoginAttempts = v; }
    public java.time.Instant getLockedUntil() { return lockedUntil; } public void setLockedUntil(java.time.Instant v) { lockedUntil = v; }
    public java.time.Instant getLastFailedLoginAt() { return lastFailedLoginAt; } public void setLastFailedLoginAt(java.time.Instant v) { lastFailedLoginAt = v; }
}

