package com.basic.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.basic.service.ExcelExportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ExcelExportService excelExportService;

    @Value("${app.admin.secret-key}")
    private String adminSecretKey;

    // ─── GET /admin/export?key=YOUR_SECRET ────────────────
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam String key) throws Exception {

        // Validate secret key
        if (!adminSecretKey.equals(key)) {
            log.warn("Unauthorized Excel export attempt");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("Admin Excel export triggered");

        byte[] excelData = excelExportService.exportCandidatesToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ));
        headers.setContentDisposition(
            ContentDisposition.attachment()
                .filename("candidates.xlsx")
                .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
}
