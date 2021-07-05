package edu.oreluniver.practice.graphapp.controller;


import edu.oreluniver.practice.graphapp.dao.DotsDao;
import edu.oreluniver.practice.graphapp.model.Dot;

import java.util.List;

public class DotsController {

    DotsDao dotsDao;

    public DotsController() {
        dotsDao = DotsDao.getInstance();
    }

    private static volatile DotsController instance;

    public static DotsController getInstance() {
        if (instance == null){
            synchronized (DotsController.class){
                instance = new DotsController();
            }
        }

        return instance;
    }

    public void addDot(Dot dot){
        dotsDao.addDot(dot);
    }

    public void editDot(Dot oldDot, Dot newDot){
        dotsDao.editDot(oldDot, newDot);
    }

    public void removeDot(Dot dot) throws Exception {
        dotsDao.removeDot(dot);
    }

    public List<Dot> getDotList(){
        return dotsDao.getDotList();
    }

    public void resetDotsList(){
        dotsDao.resetDotsList();
    }
}
