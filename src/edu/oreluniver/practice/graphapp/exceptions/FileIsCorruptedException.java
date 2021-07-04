package edu.oreluniver.practice.graphapp.exceptions;

public class FileIsCorruptedException extends Exception{
    public FileIsCorruptedException() {
        super("The file is corrupted.");
    }

    public FileIsCorruptedException(String message) {
        super(message);
    }
}
