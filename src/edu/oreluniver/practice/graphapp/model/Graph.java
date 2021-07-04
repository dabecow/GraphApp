package edu.oreluniver.practice.graphapp.model;

import edu.oreluniver.practice.graphapp.exceptions.NoSuchDotException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Graph implements Serializable {


    private List<Dot> dotList;

    public Graph() {
        this.dotList = new ArrayList<>();
    }

    public void sortDotList(){
        dotList.sort(Comparator.comparing(Dot::getPosX));
    }

    public void addDot(Dot dot) {
        dotList.add(dot);
        sortDotList();
    }

    public void editDot(Dot oldDot, Dot newDot){

        dotList.set(dotList.indexOf(oldDot), newDot);

        sortDotList();
    }

    public void removeDot(Dot dot) throws Exception {

        if (!dotList.remove(dot))
            throw new NoSuchDotException();

        sortDotList();
    }

    public List<Dot> getDotList() {
        return dotList;
    }

    public void setDotList(List<Dot> dotList) {
        this.dotList = dotList;
    }
}
