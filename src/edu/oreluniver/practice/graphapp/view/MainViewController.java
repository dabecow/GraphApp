package edu.oreluniver.practice.graphapp.view;

import edu.oreluniver.practice.graphapp.controller.FileController;
import edu.oreluniver.practice.graphapp.controller.DotsController;
import edu.oreluniver.practice.graphapp.exceptions.CreatingTheFileException;
import edu.oreluniver.practice.graphapp.model.Dot;
import edu.oreluniver.practice.graphapp.util.PrinterUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.awt.print.PrinterException;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

  @FXML
  private TableViewController tableViewController;
  @FXML
  private GraphViewController graphViewController;

  private Stage mainStage;

  private FileController fileController;

  private DotsController dotsController;

  private Dot currentDot;

  File currentFile;

  FileChooser fileChooser;


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    tableViewController.setController(this);
    graphViewController.setController(this);
    dotsController = DotsController.getInstance();
    fileController = FileController.getInstance();
    fileChooser = new FileChooser();
    fileChooser.setTitle("Open or create new file");
    FileChooser.ExtensionFilter bikovFilter =
            new FileChooser.ExtensionFilter("Own files (*.bikov)", "*.bikov");
    FileChooser.ExtensionFilter excelOldFilter =
            new FileChooser.ExtensionFilter("Old excel files (*.xls)", "*.xls");
    FileChooser.ExtensionFilter excelNewFilter =
            new FileChooser.ExtensionFilter("New excel files (*.xlsx)", "*.xlsx");

    fileChooser.getExtensionFilters().addAll(bikovFilter, excelOldFilter, excelNewFilter);
  }

  public void setMainStage(Stage mainStage) {
    this.mainStage = mainStage;
  }

  public Dot getCurrentDot() {
    return currentDot;
  }

  public void resetCurrentDot(){
    this.currentDot = null;
    graphViewController.updateCurrentDotLabel();
  }

  public void setCurrentDot(Dot dot) {
    this.currentDot = dot;
  }

  public TableViewController getTableController() {
    return tableViewController;
  }

  public GraphViewController getGraphController() {
    return graphViewController;
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


  public void deleteFromDots(Dot dot) throws Exception {
    dotsController.removeDot(dot);
    graphViewController.updateGraph();
    tableViewController.updateTable();

  }

  public void deleteCurrentDot() throws Exception {
    deleteFromDots(currentDot);
    currentDot = null;
  }

  public void updateUi(){
    tableViewController.updateTable();
    graphViewController.updateGraph();
    currentDot = null;
    graphViewController.updateCurrentDotLabel();
    graphViewController.updateGapLabel();
  }

  private boolean askWithAlertDialog(){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? " +
              "All the unsaved data will be lost.", ButtonType.YES, ButtonType.NO);
      alert.showAndWait();

      return alert.getResult().equals(ButtonType.YES);
  }

  @FXML
  private void newFile() {
    if (!askWithAlertDialog())
      return;

    currentFile = fileChooser.showSaveDialog(null);
    try {
      if (!currentFile.createNewFile())
        throw new CreatingTheFileException();
    } catch (Exception e){
      currentFile = null;
      showErrorMessage(e.getMessage());
    }
  }

  public void showErrorMessage(String cause){
    Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred: " + cause,
            ButtonType.OK);
    alert.showAndWait();
  }

  @FXML
  private void openFile(){
    File file = fileChooser.showOpenDialog(null);

    if (file == currentFile || file == null || !file.exists())
      return;

    try {
      FileController.getInstance().load(file);
      currentFile = file;
      mainStage.setTitle(currentFile.getName());
      updateUi();
    } catch (Exception e){
      Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage(),
              ButtonType.OK);
      alert.showAndWait();
      currentFile = null;
    }

//    todo: set title
  }

  @FXML
  private void closeFile(){
    if (!askWithAlertDialog())
      return;

    currentFile = null;

    mainStage.setTitle("GraphApp");

    dotsController.resetDotsList();
    updateUi();
  }

  @FXML
  private void saveFile(){
    if (currentFile == null) {
      saveAs();
      return;
    }
    try {
      fileController.save(currentFile);
    } catch (Exception e){
      showErrorMessage(e.getMessage());
    }
  }

  @FXML
  private void saveAs(){
    File file = fileChooser.showSaveDialog(null);

    if (file == null)
      return;

    currentFile = file;
    mainStage.setTitle(currentFile.getName());


    try {
      fileController.save(file);
      currentFile = file;
    } catch (Exception e) {
      showErrorMessage(e.getMessage());
    }
  }

  @FXML
  private void printGraph(){
    PrinterJob printerJob = PrinterJob.createPrinterJob();
    PrinterUtil.printNode(printerJob, graphViewController.getLineChart());
    printerJob.endJob();
  }

  @FXML
  private void printTable() throws PrinterException {

    PrinterUtil.printTable();

  }

  @FXML
  private void quit(){
    if (!askWithAlertDialog())
      return;

    mainStage.hide();
  }

  @FXML
  private void showAbout(){
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("About");
    alert.setHeaderText("GraphApp");
    alert.setContentText("Разработано во время летней практики ОГУ 2021.\n\n" +
            "Для работы с файлами и печати воспользуйтесь меню File. \n" +
            "График можно либо импортировать, либо внести самостоятельно:" +
            " воспользуйтесь таблицей справа и кнопкой Add dot. Значения" +
            " точек можно менять двойным щелчком по ячейке и нажатию Enter.\n\n" +
            "График интерактивный - можно увеличивать или уменьшать масштаб," +
            " перемещать точки с помощью мыши, перемещаться по графику с зажатой" +
            " левой кнопкой мыши.\n\n" +
            "Можно установить промежуток, в котором будет рисоваться график." +
            " Для этого необходимо вписать значения в соответствующие поля и нажать на кнопку Set gap." +
            " Если значение какого-то поля не установлено, то соответствующий конец промежутка будет" +
            " установлен в + или - бесконечность.");
    
    alert.showAndWait();
  }


}
