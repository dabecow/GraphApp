package edu.oreluniver.practice.graphapp.view;

import edu.oreluniver.practice.graphapp.model.Dot;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;

public class MainController {

  @FXML
  private TableController tableController;
  @FXML
  private GraphController graphController;

  private Dot currentDot;

  NodePrinter nodePrinter;


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
    this.nodePrinter = new NodePrinter();
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
//      tf.setStyle("");
    }
  }

  public void unmarkFields(TextField... textFields){
    for (TextField tf : textFields) {
      tf.setStyle("-fx-alignment: center");
    }
  }
  //  @FXML
//  MenuBar menuBar;
//
//  @FXML
//  MenuItem fileMenu;

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
    PrinterJob.createPrinterJob().printPage(graphController.getLineChart());
//    nodePrinter.setPrintRectangle(new Rectangle(210, 297));
//    nodePrinter.print(PrinterJob.createPrinterJob(), true, graphController.getLineChart());
  }

  @FXML
  private void printTable(){
    nodePrinter.print(PrinterJob.createPrinterJob(), true, tableController.getTableView());
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
