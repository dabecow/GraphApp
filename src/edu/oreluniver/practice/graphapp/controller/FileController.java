package edu.oreluniver.practice.graphapp.controller;


import edu.oreluniver.practice.graphapp.dao.GraphDao;
import edu.oreluniver.practice.graphapp.exceptions.ExtensionNotSupportedException;
import edu.oreluniver.practice.graphapp.exceptions.FileIsCorruptedException;
import edu.oreluniver.practice.graphapp.util.ExcelUtil;
import edu.oreluniver.practice.graphapp.util.FileUtil;

import java.io.*;

public class FileController {

    private GraphDao graphDao;

    private static volatile FileController instance;


    public FileController() {
        graphDao = GraphDao.getInstance();
    }

    public static FileController getInstance() {

        if (instance == null){
            synchronized (FileController.class){
                instance = new FileController();
            }
        }

        return instance;
    }

    public void save(File file) throws Exception {

        String extension = FileUtil.getFileExtension(file.getName());

        switch (extension) {
            case "xls":
            case "xlsx":

                ExcelUtil.write(file, GraphDao.getInstance().getGraph(), extension);

                break;
            case "bikov":

                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(graphDao);
                fos.close();

                break;
            default:
                throw new ExtensionNotSupportedException();
        }
    }

    public void load(File file) throws Exception {

        String extension = FileUtil.getFileExtension(file.getName());

        switch (extension) {
            case "xls":
            case "xlsx":

                GraphDao.getInstance().setGraph(
                        ExcelUtil.read(file, extension));

                break;
            case "bikov":
                try {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    graphDao.setGraph(((GraphDao) ois.readObject()).getGraph());
                    fis.close();
                } catch (Exception e){
                    throw new FileIsCorruptedException();
                }
                break;
            default:
                throw new ExtensionNotSupportedException();
        }

    }
}
