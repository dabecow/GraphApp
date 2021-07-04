package edu.oreluniver.practice.graphapp.exceptions;

public class ExcelFileIsCorruptedException extends Exception{
    public ExcelFileIsCorruptedException() {
        super("Excel file may be corrupted. Check if it has x and y rows in first sheet.");
    }

    public ExcelFileIsCorruptedException(String message) {
        super(message);
    }
}
