package edu.oreluniver.practice.graphapp.model;

import java.io.Serializable;

public class Dot implements Serializable {

  private double posX;
  private double posY;

  public Dot(double posX, double posY) {
    this.posX = posX;
    this.posY = posY;
  }

  public double getPosX() {
    return posX;
  }

  public void setPosX(double posX) {
    this.posX = posX;
  }

  public double getPosY() {
    return posY;
  }

  public void setPosY(double posY) {
    this.posY = posY;
  }
}
