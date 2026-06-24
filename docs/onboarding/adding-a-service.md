\
# Adding a Spring Service (Bounded Context)

This guide walks through adding a new bounded context to the AVGC-XR Portal modular monolith.

## 1. Create the Package Structure

```
src/main/java/in/elcot/avgcxr/mycontext/
├── api/
│   └── MyContextController.java
├── application/
│   ├── CreateMyEntityHandler.java
│   ├── GetMyEntityQuery.java
│   └── MyEntityMapper.java
├── domain/
│   ├── model/
│   │   ├── MyEntity.java
│   │   └── MyEntityId.java
│   ├── repository/
│   │   └── MyEntityRepository.java (interface)
│   └── event/
│       └── MyEntityCreatedEvent.java
└── infrastructure/
    ├── persistence/
    │   ├── JpaMyEntityRepository.java
    │   └── MyEntityJpaEntity.java
    └── config/
        └── MyContextConfig.java
```

## 2. Domain Entity

```java
package in.elcot.avgcxr.mycontext.domain.model;

import java.time.Instant;
import java.util.UUID;

public class MyEntity {
    private final MyEntityId id;
    private String name;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;

    public static MyEntity create(String name, String description) {
        var entity = new MyEntity();
        entity.id = new MyEntityId(UUID.randomUUID());
        entity.name = name;
        entity.description = description;
        entity.createdAt = Instant.now();
        entity.updatedAt = Instant.now();
        return entity;
    }

    // Getters, domain methods, no setters (protect invariants)
}
```

## 3. Repository Interface (Port)

```java
package in.elcot.avgcxr.mycontext.domain.repository;

import in.elcot.avgcxr.mycontext.domain.model.MyEntity;
import in.elcot.avgcxr.mycontext.domain.model.MyEntityId;
import java.util.Optional;

public interface MyEntityRepository {
    MyEntity save(MyEntity entity);
    Optional<MyEntity> findById(MyEntityId id);
}
```

## 4. JPA Implementation (Adapter)

```java
package in.elcot.avgcxr.mycontext.infrastructure.persistence;

import in.elcot.avgcxr.mycontext.domain.model.MyEntity;
import in.elcot.avgcxr.mycontext.domain.repository.MyEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaMyEntityRepository implements MyEntityRepository {

    private final SpringDataMyEntityRepository jpaRepository;
    private final MyEntityMapper mapper;

    @Override
    public MyEntity save(MyEntity entity) {
        var jpa = mapper.toJpa(entity);
        return mapper.toDomain(jpaRepository.save(jpa));
    }

    @Override
    public Optional<MyEntity> findById(MyEntityId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }
}
```

## 5. REST Controller

```java
package in.elcot.avgcxr.mycontext.api;

import in.elcot.avgcxr.mycontext.application.GetMyEntityQuery;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/my-context")
public class MyContextController {

    private final GetMyEntityQuery query;

    @GetMapping("/{id}")
    public MyEntityResponse getById(@PathVariable UUID id) {
        return query.execute(new MyEntityId(id));
    }
}
```

## 6. Flyway Migration

Create a migration file in `src/main/resources/db/migration/`:

```sql
-- V{next}__create_my_entity_table.sql
CREATE TABLE my_entity (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_my_entity_name ON my_entity (name);
```

## 7. Register in Spring Context

Add `@ComponentScan` or use `@Configuration` in the bounded context's config package to register beans.
