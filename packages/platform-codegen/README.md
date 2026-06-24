# Platform Code Generation

Code generation utilities for AVGC-XR Portal development.

## Features

- OpenAPI/Swagger spec to TypeScript interfaces
- Database schema to JPA entity generation
- Bilingual i18n key stub generation
- Flyway migration scaffolding

## Usage (Maven Plugin)

```xml
<plugin>
    <groupId>in.elcot.avgcxr</groupId>
    <artifactId>platform-codegen</artifactId>
    <executions>
        <execution>
            <goals><goal>generate-types</goal></goals>
        </execution>
    </executions>
</plugin>
```
