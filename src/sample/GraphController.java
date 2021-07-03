package sample;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.input.MouseButton;
import org.gillius.jfxutils.chart.ChartPanManager;
import org.gillius.jfxutils.chart.JFXChartUtil;

public class GraphController {

  @FXML
  private LineChart<Double, Double> lineChart;

  @FXML
  private Button resetGraphButton;

  @FXML
  private Button moveToDefaultButton;

  @FXML
  private TextField leftBoundTextField;

  @FXML
  private TextField rightBoundTextField;

  @FXML
  private Button setGapButton;

  @FXML
  private Label currentDotLabel;

  @FXML
  private Button deleteButton;

  @FXML
  private Button zoomInButton;

  @FXML
  private Button zoomOutButton;

  @FXML
  private Label gapLabel;

  @FXML
  private NumberAxis xNumberAxis;

  @FXML
  private NumberAxis yNumberAxis;

  private Controller controller;

  private double zoomingVelocity = 5;
  private double leftBound;
  private double rightBound;

  @FXML
  private void initialize(){
    ChartPanManager panner = new ChartPanManager(lineChart);
    //while presssing the left mouse button, you can drag to navigate
    panner.setMouseFilter(mouseEvent -> {
      if (mouseEvent.getButton() == MouseButton.PRIMARY) {//set your custom combination to trigger navigation
        // let it through
      } else {
        mouseEvent.consume();
      }
    });
    panner.start();



    JFXChartUtil.setupZooming(lineChart, mouseEvent -> {
      if (mouseEvent.getButton() != MouseButton.SECONDARY)//set your custom combination to trigger rectangle zooming
        mouseEvent.consume();
    });

    Pattern pattern = Pattern.compile("-?\\d*|\\d+\\.\\d*");

    UnaryOperator<Change> change = new UnaryOperator<Change>() {
      @Override
      public Change apply(Change change) {
        return pattern.matcher(change.getControlNewText()).matches() ? change : null;
      }
    };

    leftBoundTextField.setTextFormatter(new TextFormatter<>(change));
    rightBoundTextField.setTextFormatter(new TextFormatter<>(change));

    leftBound = -Double.MAX_VALUE;
    rightBound = Double.MAX_VALUE;

  }

  public void setController(Controller controller) {
    this.controller = controller;
  }


  public void updateGraph(){

    lineChart.getData().clear();
    List<Dot> dots = controller.getTableController().dots;

    if (dots == null || dots.isEmpty())
      return;

    XYChart.Series<Double, Double> series
        = new Series<>(FXCollections.observableArrayList());

    for (Dot dot: dots) {

      if (dot.getX() >= leftBound && dot.getX() <= rightBound)
        series.getData().add(new Data<>(dot.getX(), dot.getY()));
    }

    lineChart.getData().add(series);
  }

  @FXML
  public void updateCurrentDotLabel(Dot dot){
    this.currentDotLabel.setText("(" + dot.getX() + ", " + dot.getY() + ")");
  }

  @FXML
  public void deleteCurrentDot(){
    currentDotLabel.setText("Current dot");
    controller.getTableController().deleteCurrentDot();
  }

  private void changeAxisBounds(NumberAxis numberAxis, double value){
    numberAxis.setLowerBound(numberAxis.getLowerBound() - value);
    numberAxis.setUpperBound(numberAxis.getUpperBound() + value);
  }

  @FXML
  public void zoomGraphIn(){
//    xNumberAxis.setScaleX(xNumberAxis.getScaleX() + 5);
//    xNumberAxis.setLowerBound(xNumberAxis.getLowerBound() - zoomingVelocity);
    changeAxisBounds(xNumberAxis, -zoomingVelocity);
    changeAxisBounds(yNumberAxis, -zoomingVelocity);
  }

  @FXML
  public void zoomGraphOut(){
    changeAxisBounds(xNumberAxis, zoomingVelocity);
    changeAxisBounds(yNumberAxis, zoomingVelocity);
  }

  @FXML
  public void resetGraph(){
    xNumberAxis.setLowerBound(-10);
    xNumberAxis.setUpperBound(10);
    yNumberAxis.setLowerBound(-10);
    yNumberAxis.setUpperBound(10);
  }

  @FXML
  public void updateGapLabel(){
    String textToInsert = "";

    if (leftBound == - Double.MAX_VALUE)
      textToInsert += "(-inf; ";
    else
      textToInsert += "[" + leftBound + "; ";

    if (rightBound == Double.MAX_VALUE)
      textToInsert += "+inf)";
    else
      textToInsert += rightBound + "]";

    gapLabel.setText(textToInsert);
  }

  @FXML
  public void setBounds(){

    double leftValue;
    double rightValue;

    if (controller.isFieldEmpty(leftBoundTextField))
      leftValue = - Double.MAX_VALUE;
    else
      leftValue = Double.parseDouble(leftBoundTextField.getText());

    if (controller.isFieldEmpty(rightBoundTextField))
      rightValue = Double.MAX_VALUE;
    else
      rightValue = Double.parseDouble(rightBoundTextField.getText());


    if (rightValue < leftValue) {
      controller.markWrongFields(leftBoundTextField, rightBoundTextField);
      return;
    }

    leftBound = leftValue;
    rightBound = rightValue;
    updateGapLabel();
    leftBoundTextField.clear();
    rightBoundTextField.clear();

    updateGraph();
  }

}
