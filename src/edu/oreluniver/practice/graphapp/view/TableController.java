package edu.oreluniver.practice.graphapp.view;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import edu.oreluniver.practice.graphapp.model.Dot;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;


public class TableController {

  public List<Dot> dots = new ArrayList<>();

  private MainController mainController;

  public void setController(MainController mainController) {
    this.mainController = mainController;
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
  private Button addDotButton;

  @FXML
  private Button updateTableButton;

  @FXML
  private void initialize(){

    columnX.setCellValueFactory(new PropertyValueFactory<>("x"));
    columnY.setCellValueFactory(new PropertyValueFactory<>("y"));


    setCellFactory(columnX);

    setCellFactory(columnY);

    columnX.setOnEditCommit(event -> {
      Dot oldDot = event.getRowValue();
      Dot newDot = new Dot(event.getNewValue(), oldDot.getY());
      updateDot(mainController.getCurrentDot(), newDot, dots);
    });

    columnY.setOnEditCommit(event -> {
      Dot oldDot = event.getRowValue();
      Dot newDot = new Dot(oldDot.getX(), event.getNewValue());
      updateDot(mainController.getCurrentDot(), newDot, dots);
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

  private void updateDot(Dot oldDot, Dot newDot, List<Dot> dots){
    dots.set(dots.indexOf(oldDot), newDot);
    updateTable();
    mainController.getGraphController().updateGraph();
    mainController.resetCurrentDot();
  }

  @FXML
  public void updateTable(){

    dots.sort(Comparator.comparing(Dot::getX));

    tableView.getItems().removeAll();
    tableView.setItems(FXCollections.observableArrayList(dots));
    tableView.getColumns().get(0).setVisible(false);
    tableView.getColumns().get(0).setVisible(true);

  }

  public TableView<Dot> getTableView() {
    return tableView;
  }

  @FXML
  private void addEnteredDot(){

    if (mainController.isFieldEmpty(xDotTextField) || mainController.isFieldEmpty(yDotTextField)){
      mainController.markWrongFields(xDotTextField, yDotTextField);
      return;
    }

    double xValue = Double.parseDouble(xDotTextField.getText());
    double yValue = Double.parseDouble(yDotTextField.getText());

    xDotTextField.clear();
    yDotTextField.clear();

    dots.add(new Dot(xValue, yValue));
    dots.sort(Comparator.comparing(Dot::getX));

    updateTable();
    mainController.getGraphController().updateGraph();
    mainController.unmarkFields(xDotTextField, yDotTextField);
  }

  public void debugDots(){
    System.out.println("debug:");

    for (Dot dot: dots
    ) {
      System.out.println(dot.getX() + ":" + dot.getY());
    }
  }

  @FXML
  private void onEnter(){

    if (!mainController.isFieldEmpty(xDotTextField) && !mainController.isFieldEmpty(yDotTextField))
      addEnteredDot();

  }

  @FXML
  private void changeCurrentDot(){
    mainController.setCurrentDot(tableView.getSelectionModel().getSelectedItem());
    mainController.getGraphController().updateCurrentDotLabel();
  }


}
