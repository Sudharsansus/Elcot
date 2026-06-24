package in.elcot.avgcxr.platform.referencedata.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "reference_data")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReferenceDataEntity {
    @Id
    private UUID id;

    @Column(name = "category", length = 50, nullable = false)
    private String category;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "name_tamil", length = 300)
    private String nameTamil;

    @Column(name = "parent_code", length = 50)
    private String parentCode;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_active")
    private Boolean isActive;
}
