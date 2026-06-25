package in.elcot.avgcxr.analytics.reporting.infrastructure.persistence.adapter;

import in.elcot.avgcxr.analytics.reporting.application.port.output.ReportDataRepositoryPort;
import in.elcot.avgcxr.analytics.reporting.domain.model.ReportData;
import in.elcot.avgcxr.analytics.reporting.infrastructure.persistence.mapper.ReportDataMapper;
import in.elcot.avgcxr.analytics.reporting.infrastructure.persistence.repository.JpaReportDataRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportDataRepositoryAdapter implements ReportDataRepositoryPort {
  private final JpaReportDataRepository jpaRepository;

  @Override
  public ReportData save(ReportData entity) {
    return ReportDataMapper.toDomain(jpaRepository.save(ReportDataMapper.toEntity(entity)));
  }

  @Override
  public Optional<ReportData> findById(UUID id) {
    return jpaRepository.findById(id).map(ReportDataMapper::toDomain);
  }

  @Override
  public Page<ReportData> findAll(Pageable pageable) {
    return jpaRepository.findAll(pageable).map(ReportDataMapper::toDomain);
  }

  @Override
  public void deleteById(UUID id) {
    jpaRepository.deleteById(id);
  }
}
