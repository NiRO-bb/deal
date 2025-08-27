package com.example.Deal.Utils;

import com.example.Deal.DTO.ContractorRole;
import com.example.Deal.DTO.response.DealGet;
import com.example.Deal.DTO.DealSum;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Provides method for writing data to .xlsx file
 */
public class ExcelWriter {

    private ExcelWriter() {
    }

    /**
     * Creates .zip archive file with passed file.
     *
     * @param file data that must be archived
     * @return .zip archive file - if successful, Optional.empty() - else
     */
    public static Optional<InputStreamResource> archive(File file) {
        File tmpFile = new File("tmp_" + file.getName());

        try (FileInputStream fileInputStream = new FileInputStream(file);
             FileOutputStream fileOutputStream = new FileOutputStream(tmpFile);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            byte[] buffer = new byte[1024];
            int data;
            while ((data = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, data);
            }
            zipOutputStream.close();
            InputStreamResource result = new InputStreamResource(new FileInputStream(tmpFile));
            file.delete();
            tmpFile.delete();
            return Optional.of(result);
        } catch (IOException exception) {
            return Optional.empty();
        }
    }

    /**
     * Writes .xlsx file based on passed data.
     *
     * @param deals DealGet instances that must be written
     * @return created file or null - if error occurred
     */
    public static Optional<File> writer(List<DealGet> deals) {
        try (Workbook book = new XSSFWorkbook()) {
            Sheet sheet = book.createSheet("Deals");
            Row row;

            int rowIndex = 0;
            int cellIndex = 0, saveIndex;

            row = sheet.createRow(rowIndex++);
            row.createCell(cellIndex++).setCellValue("ИД сделки");
            row.createCell(cellIndex++).setCellValue("Описание");
            row.createCell(cellIndex++).setCellValue("Номер договора");
            row.createCell(cellIndex++).setCellValue("Дата договора");
            row.createCell(cellIndex++).setCellValue("Дата и время вступления соглашения в силу");
            row.createCell(cellIndex++).setCellValue("Срок действия сделки");
            row.createCell(cellIndex++).setCellValue("Тип сделки");
            row.createCell(cellIndex++).setCellValue("Статус сделки");
            row.createCell(cellIndex++).setCellValue("Сумма сделки");
            row.createCell(cellIndex++).setCellValue("Наименование валюты");
            row.createCell(cellIndex++).setCellValue("Основная сумма сделки");
            row.createCell(cellIndex++).setCellValue("Наименование контрагента");
            row.createCell(cellIndex++).setCellValue("ИНН контрагента");
            row.createCell(cellIndex++).setCellValue("Роли контрагента");

            for (DealGet deal : deals) {
                cellIndex = 0;
                row = sheet.createRow(rowIndex++);
                row.createCell(cellIndex++).setCellValue(String.valueOf(deal.getId()));
                row.createCell(cellIndex++).setCellValue(deal.getDescription());
                row.createCell(cellIndex++).setCellValue(deal.getAgreementNumber());
                row.createCell(cellIndex++).setCellValue(deal.getAgreementDate().toString());
                row.createCell(cellIndex++).setCellValue(deal.getAgreementStartDt().toString());
                row.createCell(cellIndex++).setCellValue(deal.getAvailabilityDate().toString());
                row.createCell(cellIndex++).setCellValue(deal.getType().getId());
                row.createCell(cellIndex++).setCellValue(deal.getStatus().getId());
                saveIndex = cellIndex;
                for (DealSum sum : deal.getSum()) {
                    cellIndex = saveIndex;
                    row = sheet.createRow(rowIndex++);
                    row.createCell(cellIndex++).setCellValue(sum.getSum().toString());
                    row.createCell(cellIndex++).setCellValue(sum.getCurrencyId());
                    row.createCell(cellIndex++).setCellValue(sum.isMain());
                }
                saveIndex = cellIndex;
                for (DealGet.Contractor contractor : deal.getContractors()) {
                    cellIndex = saveIndex;
                    row = sheet.createRow(rowIndex++);
                    row.createCell(cellIndex++).setCellValue(contractor.getName());
                    row.createCell(cellIndex++).setCellValue(contractor.getInn());
                    StringBuilder builder = new StringBuilder();
                    for (ContractorRole role : contractor.getRoles()) {
                        builder.append(role.getCategory()).append(", ");
                    }
                    if (builder.length() > 1) {
                        builder.delete(builder.lastIndexOf(","), builder.length());
                    }
                    row.createCell(cellIndex++).setCellValue(builder.toString());
                }
            }

            File excel = new File("Deals.xlsx");
            try (FileOutputStream fileOut = new FileOutputStream(excel)) {
                book.write(fileOut);
            }
            return Optional.of(excel);
        } catch (IOException exception) {
            return Optional.empty();
        }
    }

}
