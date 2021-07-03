package sample;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

public class TableController {

  public List<Dot> dots = new ArrayList<>();

  private Controller controller;

  public void setController(Controller controller) {
    this.controller = controller;
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

  private Dot currentDot;

  public Dot getCurrentDot() {
    return currentDot;
  }

  @FXML
  private void initialize(){


    Callback<TableColumn<Dot, Double>, TableCell<Dot, Double>> testFactory
        = TextFieldTableCell.forTableColumn(new DoubleStringConverter());

    columnX.setCellValueFactory(new PropertyValueFactory<>("x"));
    columnY.setCellValueFactory(new PropertyValueFactory<>("y"));

    columnX.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    columnY.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

    columnX.setOnEditCommit(event -> {
      Dot oldDot = event.getRowValue();
      Dot newDot = new Dot(event.getNewValue(), oldDot.getY());
      updateDot(oldDot, newDot, dots);
    });

    columnY.setOnEditCommit(event -> {
      Dot oldDot = event.getRowValue();
      Dot newDot = new Dot(oldDot.getX(), event.getNewValue());
      updateDot(oldDot, newDot, dots);
    });

    Pattern pattern = Pattern.compile("-?\\d*|\\d+\\.\\d*");

    UnaryOperator<Change> change = new UnaryOperator<Change>() {
      @Override
      public Change apply(Change change) {
        return pattern.matcher(change.getControlNewText()).matches() ? change : null;
      }
    };

    xDotTextField.setTextFormatter(new TextFormatter<>(change));
    yDotTextField.setTextFormatter(new TextFormatter<>(change));

  }

  private void updateDot(Dot oldDot, Dot newDot, List<Dot> dots){
    dots.set(dots.indexOf(oldDot), newDot);
    updateTable();
  }

  @FXML
  private void updateTable(){
    dots.sort(Comparator.comparing(Dot::getX));

    tableView.getItems().removeAll();
    tableView.setItems(FXCollections.observableArrayList(dots));
//    FXCollections.copy(tableView.getItems(), dots);
//    for (Dot dot: dots) {
//      tableView.getItems().add(dot);
//    }

    controller.getGraphController().updateGraph();
  }

  @FXML
  private void addEnteredDot(){

    if (controller.isFieldEmpty(xDotTextField) || controller.isFieldEmpty(yDotTextField)){
      controller.markWrongFields(xDotTextField, yDotTextField);
      return;
    }

    double xValue = Double.parseDouble(xDotTextField.getText());
    double yValue = Double.parseDouble(yDotTextField.getText());

    xDotTextField.clear();
    yDotTextField.clear();

    dots.add(new Dot(xValue, yValue));
    dots.sort(Comparator.comparing(Dot::getX));
    updateTable();
  }

  private void debugDots(){
    System.out.println("debug:");

    for (Dot dot: dots
    ) {
      System.out.println(dot.getX() + ":" + dot.getY());
    }
  }

  @FXML
  private void onEnter(){

    if (!controller.isFieldEmpty(xDotTextField) && !controller.isFieldEmpty(yDotTextField))
      addEnteredDot();

//    debugDots();
  }

  @FXML
  private void changeCurrentDot(){
    currentDot = tableView.getSelectionModel().getSelectedItem();
    controller.getGraphController().updateCurrentDotLabel(currentDot);
  }

  public void deleteFromDots(Dot dot){
    dots.remove(dot);
    updateTable();
  }

  public void deleteCurrentDot(){
    deleteFromDots(currentDot);
  }
}
