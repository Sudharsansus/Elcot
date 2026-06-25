package in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.mapper;

import in.elcot.avgcxr.support.helpdesk.domain.model.HelpdeskTicket;
import in.elcot.avgcxr.support.helpdesk.infrastructure.persistence.entity.HelpdeskTicketEntity;

public final class HelpdeskTicketMapper {
  private HelpdeskTicketMapper() {}

  public static HelpdeskTicket toDomain(HelpdeskTicketEntity e) {
    if (e == null) return null;
    return HelpdeskTicket.builder()
        .id(e.getId())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }

  public static HelpdeskTicketEntity toEntity(HelpdeskTicket d) {
    if (d == null) return null;
    HelpdeskTicketEntity e = new HelpdeskTicketEntity();
    e.setId(d.getId());
    e.setCreatedAt(d.getCreatedAt());
    e.setUpdatedAt(d.getUpdatedAt());
    return e;
  }
}
