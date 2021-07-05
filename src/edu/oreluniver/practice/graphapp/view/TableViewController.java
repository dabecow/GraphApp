package edu.oreluniver.practice.graphapp.view;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import edu.oreluniver.practice.graphapp.controller.DotsController;
import edu.oreluniver.practice.graphapp.model.Dot;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;


public class TableViewController {

  private MainViewController mainViewController;

  public void setController(MainViewController mainViewController) {
    this.mainViewController = mainViewController;
  }

  @FXML
  private TableView<Dot> tableView;

  @FXML
  private TableColumn<Dot, Double> columnX;

  @FXML
  private TableColumn<Dot, Double> columnY;

  @FXML
  private TextField xDotTextField;

  @FXML
  private TextField yDotTextField;

  @FXML
  private void initialize(){

    columnX.setCellValueFactory(new PropertyValueFactory<>("posX"));
    columnY.setCellValueFactory(new PropertyValueFactory<>("posY"));


    setCellFactory(columnX);

    setCellFactory(columnY);

    columnX.setOnEditCommit(event -> {
      Dot oldDot = event.getRowValue();
      Dot newDot = new Dot(event.getNewValue(), oldDot.getPosY());
      updateDot(mainViewController.getCurrentDot(), newDot);
    });

    columnY.setOnEditCommit(event -> {
      Dot oldDot = event.getRowValue();
      Dot newDot = new Dot(oldDot.getPosX(), event.getNewValue());
      updateDot(mainViewController.getCurrentDot(), newDot);
    });

    setPattern(xDotTextField, yDotTextField);

  }

  static void setPattern(TextField xDotTextField, TextField yDotTextField) {
    Pattern pattern = Pattern.compile("-?\\d*|\\d+\\.\\d*");

    UnaryOperator<Change> change = change1 -> pattern.matcher(change1.getControlNewText()).matches() ? change1 : null;

    xDotTextField.setTextFormatter(new TextFormatter<>(change));
    yDotTextField.setTextFormatter(new TextFormatter<>(change));
  }

  private void setCellFactory(TableColumn<Dot, Double> column) {
    column.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {

      @Override public String toString(final Double value) {
        return String.format("%.5f", value);
      }

      @Override
      public Double fromString(String string) {
        string = string.replaceAll(",", ".");
        return Double.parseDouble(string);
      }

    }));
  }

  private void updateDot(Dot oldDot, Dot newDot){
    DotsController.getInstance().editDot(oldDot, newDot);
    updateTable();
    mainViewController.getGraphController().updateGraph();
    mainViewController.resetCurrentDot();
  }

  @FXML
  public void updateTable(){

    tableView.getItems().removeAll();
    tableView.setItems(
            FXCollections.observableArrayList(DotsController.getInstance().getDotList()));
    tableView.getColumns().get(0).setVisible(false);
    tableView.getColumns().get(0).setVisible(true);

  }

  public TableView<Dot> getTableView() {
    return tableView;
  }

  @FXML
  private void addEnteredDot(){

    if (mainViewController.isFieldEmpty(xDotTextField) || mainViewController.isFieldEmpty(yDotTextField)){
      mainViewController.markWrongFields(xDotTextField, yDotTextField);
      return;
    }

    double xValue = Double.parseDouble(xDotTextField.getText());
    double yValue = Double.parseDouble(yDotTextField.getText());

    xDotTextField.clear();
    yDotTextField.clear();

    DotsController.getInstance().addDot(new Dot(xValue, yValue));

    updateTable();
    mainViewController.getGraphController().updateGraph();
    mainViewController.unmarkFields(xDotTextField, yDotTextField);
  }

  public void debugDots(){
    System.out.println("debug:");

    for (Dot dot: DotsController.getInstance().getDotList()
    ) {
      System.out.println(dot.getPosX() + ":" + dot.getPosY());
    }
  }

  @FXML
  private void onEnter(){

    if (!mainViewController.isFieldEmpty(xDotTextField) && !mainViewController.isFieldEmpty(yDotTextField))
      addEnteredDot();

  }

  @FXML
  private void changeCurrentDot(){
    mainViewController.setCurrentDot(tableView.getSelectionModel().getSelectedItem());
    mainViewController.getGraphController().updateCurrentDotLabel();
  }


}
