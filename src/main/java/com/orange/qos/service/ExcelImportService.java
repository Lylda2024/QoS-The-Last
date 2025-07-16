package com.orange.qos.service;

import com.orange.qos.service.dto.SiteDTO;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelImportService {

    public List<SiteDTO> readSitesFromExcel(MultipartFile file) {
        List<SiteDTO> sites = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // skip header row

            while (rows.hasNext()) {
                Row row = rows.next();
                SiteDTO site = new SiteDTO();

                site.setNomSite(getString(row, 0));
                site.setCodeOCI(getString(row, 1));
                site.setLatitude(getFloat(row, 2));
                site.setLongitude(getFloat(row, 3));
                site.setVille(getString(row, 4));
                site.setTypeSite(getString(row, 5));

                // Validation simple des coordonnées
                if (site.getNomSite() != null && site.getCodeOCI() != null && isValidCoordinate(site.getLatitude(), site.getLongitude())) {
                    sites.add(site);
                } else {
                    System.out.printf("Ligne ignorée - nomSite/codeOCI manquant ou coordonnées invalides : %s%n", site);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur de lecture du fichier Excel", e);
        }

        return sites;
    }

    private String getString(Row row, int index) {
        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return (cell != null) ? cell.toString().trim() : null;
    }

    private Double getDouble(Row row, int index) {
        try {
            Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) return null;
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else {
                return Double.parseDouble(cell.getStringCellValue().trim());
            }
        } catch (Exception e) {
            return null;
        }
    }

    private Float getFloat(Row row, int index) {
        Double d = getDouble(row, index);
        return d != null ? d.floatValue() : null;
    }

    private boolean isValidCoordinate(Float latitude, Float longitude) {
        if (latitude == null || longitude == null) return false;
        if (latitude == 0f || longitude == 0f) return false;
        if (latitude < -90f || latitude > 90f) return false;
        if (longitude < -180f || longitude > 180f) return false;
        return true;
    }
}
