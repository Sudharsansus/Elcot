package in.elcot.avgcxr.platformcore.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Base class for domain entities that need audit metadata and domain-event collection.
 *
 * <p><b>Pure domain class — no JPA, no Spring annotations.</b> The JPA auditing hooks
 * ({@code @CreatedDate}, {@code @LastModifiedDate}, etc.) live on the corresponding JPA entity
 * classes in {@code infrastructure/persistence/entity/}, not here.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AuditableEntity {

  private UUID id;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String createdBy;
  private String updatedBy;

  private final List<Object> domainEvents = new ArrayList<>();

  protected void registerEvent(Object event) {
    domainEvents.add(event);
  }

  public List<Object> getDomainEvents() {
    return List.copyOf(domainEvents);
  }

  public void clearDomainEvents() {
    domainEvents.clear();
  }
}
