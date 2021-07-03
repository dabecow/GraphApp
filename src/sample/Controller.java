package sample;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import sample.TableController;

public class Controller {

  @FXML
  private TableController tableController;
  @FXML
  private GraphController graphController;

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

  public void markWrongFields(TextField... textFields){
    // code
  }
  //  @FXML
//  MenuBar menuBar;
//
//  @FXML
//  MenuItem fileMenu;



}
