package edu.oreluniver.practice.graphapp.util;

import edu.oreluniver.practice.graphapp.exceptions.ExcelFileIsCorruptedException;
import edu.oreluniver.practice.graphapp.model.Dot;
import edu.oreluniver.practice.graphapp.model.Graph;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ExcelUtil {
    public static void write(File file, Graph graph, String extension) throws Exception {
        Workbook book;

        if (extension.equals("xls"))
            book = new HSSFWorkbook();
        else if (extension.equals("xlsx"))
            book = new XSSFWorkbook();
        else throw new ExcelFileIsCorruptedException();

        Sheet sheet = book.createSheet("output");

        int i = 0;
        // Нумерация начинается с нуля
        Row row = sheet.createRow(i);

        // Мы запишем имя и дату в два столбца
        // имя будет String, а дата рождения --- Date,
        // формата dd.mm.yyyy

        row.createCell(0).setCellValue("x");
        row.createCell(1).setCellValue("y");

        for (Dot dot: graph.getDotList()) {
            i++;
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(dot.getPosX());
            row.createCell(1).setCellValue(dot.getPosY());
        }

        // Записываем всё в файл
        book.write(new FileOutputStream(file));
        book.close();

    }

    public static Graph read(File file, String extension) throws Exception {
        Workbook book;

        if (extension.equals("xls"))
            book = new HSSFWorkbook(new FileInputStream(file));
        else if (extension.equals("xlsx"))
            book = new XSSFWorkbook(new FileInputStream(file));
        else throw new ExcelFileIsCorruptedException();

        Sheet sheet = book.getSheetAt(0);
        Row row = sheet.getRow(0);

        if (
                !(row.getCell(0).getStringCellValue().equals("x")
                        && row.getCell(1).getStringCellValue().equals("y"))
        )
            throw new ExcelFileIsCorruptedException();

        Graph graph = new Graph();

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            try {
                row = sheet.getRow(i);
                Dot dot = new Dot(row.getCell(0).getNumericCellValue(), row.getCell(1).getNumericCellValue());
                graph.addDot(dot);
            } catch (Exception e){
                throw new ExcelFileIsCorruptedException();
            }
        }

        book.close();

        return graph;
    }
}
