package edu.oreluniver.practice.graphapp.util;

public class FileUtil {
    public static String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
