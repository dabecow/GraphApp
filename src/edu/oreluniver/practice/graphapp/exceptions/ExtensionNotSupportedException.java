package edu.oreluniver.practice.graphapp.exceptions;

public class ExtensionNotSupportedException extends Exception{

    public ExtensionNotSupportedException() {
        super("This extension is not supported.");
    }

    public ExtensionNotSupportedException(String message) {
        super(message);
    }
}
