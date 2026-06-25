package in.elcot.avgcxr.analytics.reporting.application.service;

import in.elcot.avgcxr.analytics.reporting.api.rest.dto.response.ReportingResponse;
import in.elcot.avgcxr.analytics.reporting.application.port.input.GetReportingUseCase;
import in.elcot.avgcxr.analytics.reporting.application.port.output.ReportingRepositoryPort;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ReportingService implements GetReportingUseCase {
  private final ReportingRepositoryPort repo;

  public ReportingService(ReportingRepositoryPort repo) {
    this.repo = repo;
  }

  @Override
  public ReportingResponse generateApplicationReport(
      LocalDate from, LocalDate to, String district) {
    List<Map<String, Object>> rows = repo.getApplicationStats(from, to, district);
    Map<String, Object> summary = repo.getApplicationSummary(from, to);
    return new ReportingResponse("APPLICATION", "Application Report", rows, summary);
  }

  @Override
  public ReportingResponse generateDisbursementReport(LocalDate from, LocalDate to) {
    List<Map<String, Object>> rows = repo.getDisbursementStats(from, to);
    return new ReportingResponse(
        "DISBURSEMENT", "Disbursement Report", rows, Map.of("totalDisbursed", 0));
  }

  @Override
  public byte[] exportApplicationReportCsv(LocalDate from, LocalDate to) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (PrintWriter pw = new PrintWriter(baos, true, StandardCharsets.UTF_8)) {
      pw.println("Application Number,Scheme,Applicant,District,Status,Submitted Date,Amount");
      List<Map<String, Object>> rows = repo.getApplicationStats(from, to, null);
      for (Map<String, Object> row : rows) {
        pw.printf(
            "%s,%s,%s,%s,%s,%s,%s%n",
            row.getOrDefault("app_number", ""),
            row.getOrDefault("scheme", ""),
            row.getOrDefault("applicant", ""),
            row.getOrDefault("district", ""),
            row.getOrDefault("status", ""),
            row.getOrDefault("submitted", ""),
            row.getOrDefault("amount", ""));
      }
    }
    return baos.toByteArray();
  }
}
