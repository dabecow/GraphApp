package edu.oreluniver.practice.graphapp.view;

import edu.oreluniver.practice.graphapp.model.Dot;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.TextField;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;

public class MainController {

  @FXML
  private TableController tableController;
  @FXML
  private GraphController graphController;

  private Dot currentDot;

  public Dot getCurrentDot() {
    return currentDot;
  }

  public void resetCurrentDot(){
    this.currentDot = null;
    graphController.updateCurrentDotLabel();
  }

  public void setCurrentDot(Dot dot) {
    this.currentDot = dot;
  }

  @FXML
  private void initialize(){
    tableController.setController(this);
    graphController.setController(this);
  }

  public TableController getTableController() {
    return tableController;
  }

  public GraphController getGraphController() {
    return graphController;
  }

  public boolean isFieldEmpty(TextField textField){
    return textField.getText() == null
        || textField.getText().trim().isEmpty();
  }

  public void markWrongFields(TextField... textFields) {
    for (TextField tf : textFields) {
      if (tf.getText().equals(""))
      tf.setStyle("-fx-text-box-border: red ;\n" +
              "  -fx-focus-color: red ;" +
              "-fx-alignment: center");
    }
  }

  public void unmarkFields(TextField... textFields){
    for (TextField tf : textFields) {
      tf.setStyle("-fx-alignment: center");
    }
  }


  public void deleteFromDots(Dot dot){
    tableController.dots.remove(dot);
    graphController.updateGraph();
    tableController.updateTable();

  }

  public void deleteCurrentDot(){
    deleteFromDots(currentDot);
    currentDot = null;
  }

  @FXML
  private void newFile(){
    //todo:
  }

  @FXML
  private void openFile(){
    //todo:
  }

  @FXML
  private void closeFile(){
    //todo:
  }

  @FXML
  private void saveFile(){
    //todo:
  }

  @FXML
  private void saveAs(){
    //todo:
  }

  @FXML
  private void printGraph(){
    PrinterJob printerJob = PrinterJob.createPrinterJob();
    PrinterUtil.printViewPage(printerJob, graphController.getLineChart());
    printerJob.endJob();
  }

  @FXML
  private void printTable() throws PrinterException {

    Object[] columnNames = {"", "x", "y"};

    DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

    String format = "%.5f";

    tableModel.addRow(columnNames);

    int i = 1;
    for (Dot dot: getTableController().dots) {
      tableModel.addRow(new Object[]{
              Integer.toString(i),
              String.format(format, dot.getPosX()),
              String.format(format, dot.getPosY())
      });

      i++;
    }

    JTable table = new JTable(tableModel);

    Dimension d = table.getPreferredSize();

    table.setSize(d.width, d.height);

    table.print();

  }

  @FXML
  private void quit(){
    //todo:
  }

  @FXML
  private void showAbout(){
    //todo:
  }
}
