package in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.mapper;

import in.elcot.avgcxr.analytics.dashboard.domain.model.DashboardData;
import in.elcot.avgcxr.analytics.dashboard.infrastructure.persistence.entity.DashboardDataEntity;

public final class DashboardDataMapper {
    private DashboardDataMapper() {}

    public static DashboardData toDomain(DashboardDataEntity e) {
        if (e == null) return null;
        return DashboardData.builder()
                .id(e.getId())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public static DashboardDataEntity toEntity(DashboardData d) {
        if (d == null) return null;
        DashboardDataEntity e = new DashboardDataEntity();
        e.setId(d.getId());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}
