package com.basic.service;

import com.basic.model.Candidate;
import com.basic.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final CandidateRepository candidateRepository;

    public byte[] exportCandidatesToExcel() throws Exception {

        List<Candidate> candidates = candidateRepository.findAll();

        log.info("Exporting {} candidates to Excel", candidates.size());

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Candidates");

            // ─── Header Style ──────────────────────────────
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            // ─── Alternate Row Style ───────────────────────
            CellStyle altRowStyle = workbook.createCellStyle();
            altRowStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
            altRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // ─── Headers ───────────────────────────────────
            String[] headers = {
                "ID", "First Name", "Last Name", "Date of Birth",
                "PAN Card", "Current Company", "On Notice Period",
                "Notice Days", "Last Working Day",
                "Current Salary (Rs)", "Expected Salary (Rs)", "Submitted At", "Resume URL", "Portfolio Link", "LinkedIn Link"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // ─── Data Rows ─────────────────────────────────
            int rowNum = 1;
            for (Candidate c : candidates) {
                Row row = sheet.createRow(rowNum);

                // Alternate row color
                if (rowNum % 2 == 0) {
                    for (int i = 0; i < headers.length; i++) {
                        Cell cell = row.createCell(i);
                        cell.setCellStyle(altRowStyle);
                    }
                }

                row.createCell(0).setCellValue(c.getId());
                row.createCell(1).setCellValue(c.getFirstName());
                row.createCell(2).setCellValue(c.getLastName());
                row.createCell(3).setCellValue(
                    c.getDob() != null ? c.getDob().toString() : ""
                );
                row.createCell(4).setCellValue(c.getPanCard());
                row.createCell(5).setCellValue(c.getCurrentCompany());
                row.createCell(6).setCellValue(
                    Boolean.TRUE.equals(c.getOnNoticePeriod()) ? "Yes" : "No"
                );
                row.createCell(7).setCellValue(
                    c.getNoticePeriodDays() != null ? c.getNoticePeriodDays() : 0
                );
                row.createCell(8).setCellValue(
                    c.getLastWorkingDay() != null ? c.getLastWorkingDay().toString() : "N/A"
                );
                row.createCell(9).setCellValue(c.getCurrentSalary());
                row.createCell(10).setCellValue(c.getExpectedSalary());
                row.createCell(11).setCellValue(
                    c.getSubmittedAt() != null ? c.getSubmittedAt().toString() : ""
                );
             // Was showing filename - now show clickable URL
                row.createCell(12).setCellValue(
                    c.getResumeUrl() != null ? c.getResumeUrl() : "Not uploaded"
                );
                	row.createCell(13).setCellValue(
                	    c.getPortfolioLink() != null ? c.getPortfolioLink() : ""
                	);
                	row.createCell(14).setCellValue(
                	    c.getLinkedinLink() != null ? c.getLinkedinLink() : ""
                	);

                rowNum++;
            }

            // ─── Manual Column Widths (no autoSizeColumn - needs fonts not available on Linux) ─
            sheet.setColumnWidth(0, 3000);   // ID
            sheet.setColumnWidth(1, 6000);   // First Name
            sheet.setColumnWidth(2, 6000);   // Last Name
            sheet.setColumnWidth(3, 5000);   // DOB
            sheet.setColumnWidth(4, 6000);   // PAN Card
            sheet.setColumnWidth(5, 8000);   // Current Company
            sheet.setColumnWidth(6, 5000);   // On Notice
            sheet.setColumnWidth(7, 5000);   // Notice Days
            sheet.setColumnWidth(8, 6000);   // Last Working Day
            sheet.setColumnWidth(9, 7000);   // Current Salary
            sheet.setColumnWidth(10, 7000);  // Expected Salary
            sheet.setColumnWidth(11, 8000);  // Submitted At
            sheet.setColumnWidth(12, 7000);
            sheet.setColumnWidth(13, 8000);
            sheet.setColumnWidth(14, 8000);

            // ─── Freeze header row ─────────────────────────
            sheet.createFreezePane(0, 1);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }
}