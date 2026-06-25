package in.elcot.avgcxr.analytics.reporting.application.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * Report export service — produces XLSX and PDF outputs of tabular data.
 *
 * <p>Used by /api/v1/reports/export, /api/v1/applications/export, etc.
 */
@Service
public class ExportService {

  /**
   * Build an XLSX workbook from a list of row maps. The first row is treated as the header (column
   * names from the keys of the first map).
   */
  public byte[] toXlsx(List<Map<String, Object>> rows) {
    try (XSSFWorkbook wb = new XSSFWorkbook();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      XSSFSheet sheet = wb.createSheet("Report");
      if (rows.isEmpty()) {
        wb.write(out);
        return out.toByteArray();
      }
      String[] headers = rows.get(0).keySet().toArray(new String[0]);
      Row header = sheet.createRow(0);
      for (int i = 0; i < headers.length; i++) {
        Cell cell = header.createCell(i);
        cell.setCellValue(headers[i]);
      }
      for (int r = 0; r < rows.size(); r++) {
        Row row = sheet.createRow(r + 1);
        Map<String, Object> data = rows.get(r);
        for (int c = 0; c < headers.length; c++) {
          Cell cell = row.createCell(c);
          Object v = data.get(headers[c]);
          if (v == null) {
            cell.setBlank();
          } else if (v instanceof Number n) {
            cell.setCellValue(n.doubleValue());
          } else {
            cell.setCellValue(v.toString());
          }
        }
      }
      for (int i = 0; i < headers.length; i++) {
        sheet.autoSizeColumn(i);
      }
      wb.write(out);
      return out.toByteArray();
    } catch (Exception ex) {
      throw new IllegalStateException("XLSX generation failed", ex);
    }
  }

  /** Build a PDF report with title + table from row maps. */
  public byte[] toPdf(String title, List<Map<String, Object>> rows) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      Document doc = new Document(PageSize.A4.rotate());
      PdfWriter.getInstance(doc, out);
      doc.open();
      Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
      doc.add(new Paragraph(title, titleFont));
      doc.add(new Paragraph("Generated: " + LocalDateTime.now()));
      doc.add(new Paragraph(" "));
      if (rows.isEmpty()) {
        doc.add(new Paragraph("No data"));
      } else {
        String[] headers = rows.get(0).keySet().toArray(new String[0]);
        PdfPTable table = new PdfPTable(headers.length);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        for (String h : headers) {
          PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
          cell.setHorizontalAlignment(Element.ALIGN_CENTER);
          table.addCell(cell);
        }
        for (Map<String, Object> data : rows) {
          for (String h : headers) {
            Object v = data.get(h);
            table.addCell(new Phrase(v == null ? "" : v.toString()));
          }
        }
        doc.add(table);
      }
      doc.close();
    } catch (Exception ex) {
      throw new IllegalStateException("PDF generation failed", ex);
    }
    return out.toByteArray();
  }
}
