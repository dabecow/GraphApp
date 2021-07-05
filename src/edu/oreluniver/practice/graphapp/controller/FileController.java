package edu.oreluniver.practice.graphapp.controller;


import edu.oreluniver.practice.graphapp.dao.DotsDao;
import edu.oreluniver.practice.graphapp.exceptions.ExtensionNotSupportedException;
import edu.oreluniver.practice.graphapp.exceptions.FileIsCorruptedException;
import edu.oreluniver.practice.graphapp.model.Dot;
import edu.oreluniver.practice.graphapp.util.ExcelUtil;
import edu.oreluniver.practice.graphapp.util.FileUtil;

import java.io.*;
import java.util.List;

public class FileController {

    private DotsDao dotsDao;

    private static volatile FileController instance;


    public FileController() {
        dotsDao = DotsDao.getInstance();
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

                ExcelUtil.write(file, DotsDao.getInstance().getDotList(), extension);

                break;
            case "bikov":

                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                oos.writeObject(dotsDao.getDotList());
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

                DotsDao.getInstance().setDotList(
                        ExcelUtil.read(file, extension));

                break;
            case "bikov":
                try {
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);

                    dotsDao.setDotList((List<Dot>) ois.readObject());
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
