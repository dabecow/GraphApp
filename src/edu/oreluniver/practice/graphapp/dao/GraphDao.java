package edu.oreluniver.practice.graphapp.dao;

import edu.oreluniver.practice.graphapp.model.Graph;

import java.io.Serializable;

public class GraphDao implements Serializable {

    Graph graph;

    public GraphDao() {
        this.graph = new Graph();
    }

    private static volatile GraphDao instance;

    public static GraphDao getInstance() {

        if (instance == null) {
            synchronized (GraphDao.class) {
                    instance = new GraphDao();
            }
        }

        return instance;
    }


    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }
}
