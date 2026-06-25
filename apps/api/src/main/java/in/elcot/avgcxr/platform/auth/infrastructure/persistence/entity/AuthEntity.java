package in.elcot.avgcxr.platform.auth.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "auth_tokens")
public class AuthEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id", columnDefinition = "UUID")
  private String id;

  @Column(name = "user_id", nullable = false, columnDefinition = "UUID")
  private String userId;

  @Column(name = "access_token", nullable = false, length = 2048)
  private String accessToken;

  @Column(name = "refresh_token", nullable = false, length = 2048)
  private String refreshToken;

  @Column(name = "expires_at", nullable = false)
  private Instant expiresAt;

  @Column(name = "revoked", nullable = false)
  private boolean revoked;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public String getId() {
    return id;
  }

  public void setId(String v) {
    id = v;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String v) {
    userId = v;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String v) {
    accessToken = v;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String v) {
    refreshToken = v;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(Instant v) {
    expiresAt = v;
  }

  public boolean isRevoked() {
    return revoked;
  }

  public void setRevoked(boolean v) {
    revoked = v;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant v) {
    createdAt = v;
  }
}
