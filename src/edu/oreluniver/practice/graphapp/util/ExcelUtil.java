package edu.oreluniver.practice.graphapp.util;

import edu.oreluniver.practice.graphapp.exceptions.ExcelFileIsCorruptedException;
import edu.oreluniver.practice.graphapp.model.Dot;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    public static void write(File file, List<Dot> dotList, String extension) throws Exception {
        Workbook book;

        if (extension.equals("xls"))
            book = new HSSFWorkbook();
        else if (extension.equals("xlsx"))
            book = new XSSFWorkbook();
        else throw new ExcelFileIsCorruptedException();

        Sheet sheet = book.createSheet("output");

        int i = 0;

        Row row = sheet.createRow(i);


        row.createCell(0).setCellValue("x");
        row.createCell(1).setCellValue("y");

        for (Dot dot: dotList) {
            i++;
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(dot.getPosX());
            row.createCell(1).setCellValue(dot.getPosY());
        }

        book.write(new FileOutputStream(file));
        book.close();

    }

    public static List<Dot> read(File file, String extension) throws Exception {
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

        List<Dot> dotList = new ArrayList<>();

        for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
            try {
                row = sheet.getRow(i);
                Dot dot = new Dot(row.getCell(0).getNumericCellValue(), row.getCell(1).getNumericCellValue());
                dotList.add(dot);
            } catch (Exception e){
                throw new ExcelFileIsCorruptedException();
            }
        }

        book.close();

        return dotList;
    }
}
