package edu.oreluniver.practice.graphapp.exceptions;

public class NoSuchDotException extends Exception {
    public NoSuchDotException() {
        super("No such dot");
    }

    public NoSuchDotException(String message) {
        super(message);
    }
}
