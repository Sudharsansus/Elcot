package in.elcot.avgcxr.analytics.reporting.application.service;

import in.elcot.avgcxr.analytics.reporting.api.rest.dto.response.ReportDataResponse;
import in.elcot.avgcxr.analytics.reporting.application.command.CreateReportDataCommand;
import in.elcot.avgcxr.analytics.reporting.application.port.input.CreateReportDataUseCase;
import in.elcot.avgcxr.analytics.reporting.application.port.input.GetReportDataUseCase;
import in.elcot.avgcxr.analytics.reporting.application.port.output.ReportDataRepositoryPort;
import in.elcot.avgcxr.analytics.reporting.domain.exception.ReportDataNotFoundException;
import in.elcot.avgcxr.analytics.reporting.domain.model.ReportData;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Real implementation: persists to PostgreSQL via ReportDataRepositoryPort.
 *
 * <p>HEXAGONAL FIX (HIGH-004 audit): Application layer depends ONLY on the output port. No JPA /
 * Spring Data imports here. The infrastructure layer provides the adapter.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ReportDataUseCaseService implements CreateReportDataUseCase, GetReportDataUseCase {

  private final ReportDataRepositoryPort repo;

  @Override
  @Transactional
  public ReportDataResponse create(CreateReportDataCommand command) {
    log.info("Creating report data: {}", command);
    ReportData data =
        ReportData.builder()
            .id(UUID.randomUUID())
            .createdAt(java.time.LocalDateTime.now())
            .updatedAt(java.time.LocalDateTime.now())
            .build();
    ReportData saved = repo.save(data);
    return new ReportDataResponse(saved.getId(), saved.getCreatedAt(), saved.getUpdatedAt());
  }

  @Override
  public ReportDataResponse getById(UUID id) {
    ReportData data = repo.findById(id).orElseThrow(() -> new ReportDataNotFoundException(id));
    return new ReportDataResponse(data.getId(), data.getCreatedAt(), data.getUpdatedAt());
  }

  @Override
  public Page<ReportDataResponse> findAll(Pageable pageable) {
    return repo.findAll(pageable)
        .map(d -> new ReportDataResponse(d.getId(), d.getCreatedAt(), d.getUpdatedAt()));
  }
}
