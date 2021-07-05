package edu.oreluniver.practice.graphapp.exceptions;

public class CreatingTheFileException extends Exception{
    public CreatingTheFileException() {
        super("Error when creating the file");
    }

    public CreatingTheFileException(String message) {
        super(message);
    }
}
