package in.elcot.avgcxr.analytics.reporting.api.rest;

import in.elcot.avgcxr.analytics.reporting.application.service.ExportService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Export endpoints — produce XLSX and PDF downloads for reports. */
@RestController
@RequestMapping("/api/v1/reports")
public class ExportController {

  private final ExportService exportService;

  public ExportController(ExportService exportService) {
    this.exportService = exportService;
  }

  @PostMapping("/export/xlsx")
  @PreAuthorize("hasAnyRole('ADMIN', 'DISTRICT_OFFICER', 'NODAL_OFFICER')")
  public ResponseEntity<byte[]> exportXlsx(@RequestBody List<Map<String, Object>> rows) {
    byte[] xlsx = exportService.toXlsx(rows);
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=report-" + LocalDate.now() + ".xlsx")
        .contentType(
            MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .body(xlsx);
  }

  @PostMapping("/export/pdf")
  @PreAuthorize("hasAnyRole('ADMIN', 'DISTRICT_OFFICER', 'NODAL_OFFICER')")
  public ResponseEntity<byte[]> exportPdf(@RequestBody ExportRequest req) {
    byte[] pdf = exportService.toPdf(req.title() != null ? req.title() : "Report", req.rows());
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=report-" + LocalDate.now() + ".pdf")
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdf);
  }

  public record ExportRequest(String title, List<Map<String, Object>> rows) {}
}
