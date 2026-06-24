package in.elcot.avgcxr.platform.notification.infrastructure.persistence.mapper;

import in.elcot.avgcxr.platform.notification.domain.model.Notification;
import in.elcot.avgcxr.platform.notification.infrastructure.persistence.entity.NotificationEntity;

public final class NotificationMapper {
    private NotificationMapper() {}

    public static Notification toDomain(NotificationEntity e) {
        if (e == null) return null;
        return Notification.builder()
                .id(e.getId())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }

    public static NotificationEntity toEntity(Notification d) {
        if (d == null) return null;
        NotificationEntity e = new NotificationEntity();
        e.setId(d.getId());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        return e;
    }
}
