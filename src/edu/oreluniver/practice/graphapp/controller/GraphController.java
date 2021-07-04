package edu.oreluniver.practice.graphapp.controller;


import edu.oreluniver.practice.graphapp.dao.GraphDao;
import edu.oreluniver.practice.graphapp.model.Dot;
import edu.oreluniver.practice.graphapp.model.Graph;

public class GraphController {

    GraphDao graphDao;

    public GraphController() {
        graphDao = GraphDao.getInstance();
    }

    private static volatile GraphController instance;

    public static GraphController getInstance() {
        if (instance == null){
            synchronized (GraphController.class){
                instance = new GraphController();
            }
        }

        return instance;
    }

    public void addDot(Dot dot){
        graphDao.getGraph().addDot(dot);
    }

    public void editDot(Dot oldDot, Dot newDot){
        graphDao.getGraph().editDot(oldDot, newDot);
    }

    public void removeDot(Dot dot) throws Exception {
        graphDao.getGraph().removeDot(dot);
    }

    public Graph getGraph(){
        return graphDao.getGraph();
    }
}
