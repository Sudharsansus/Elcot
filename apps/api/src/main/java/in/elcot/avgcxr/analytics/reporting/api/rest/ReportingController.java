package in.elcot.avgcxr.analytics.reporting.api.rest;

import in.elcot.avgcxr.analytics.reporting.api.rest.dto.response.ReportingResponse;
import in.elcot.avgcxr.analytics.reporting.application.service.ReportingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
@PreAuthorize("hasAnyRole('ADMIN','DISTRICT_OFFICER','NODAL_OFFICER')")
public class ReportingController {
    private final ReportingService reportingService;
    public ReportingController(ReportingService reportingService) { this.reportingService = reportingService; }

    @GetMapping("/applications")
    public ReportingResponse getApplicationReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
        @RequestParam(required = false) String district) {
        return reportingService.generateApplicationReport(from, to, district);
    }

    @GetMapping("/applications/export")
    public ResponseEntity<byte[]> exportApplicationReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        byte[] csv = reportingService.exportApplicationReportCsv(from, to);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=applications-report.csv").contentType(MediaType.parseMediaType("text/csv")).body(csv);
    }

    @GetMapping("/disbursement")
    public ReportingResponse getDisbursementReport(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportingService.generateDisbursementReport(from, to);
    }
}

