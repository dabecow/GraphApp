package edu.oreluniver.practice.graphapp.view;

import java.util.List;

import edu.oreluniver.practice.graphapp.model.Dot;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

  XYChart.Series<Double, Double> series;

  private MainController mainController;

  private final double boundsDistractionToDisableGrid = 130;
  private final double defaultLowerBound = -10;
  private final double defaultUpperBound = 10;
  private final double zoomingVelocity = 5;

  private double leftBound;
  private double rightBound;

  public LineChart<Double, Double> getLineChart() {
    return lineChart;
  }

  @FXML
  private void updateAxisGrid(){
    if (xNumberAxis.getUpperBound() - xNumberAxis.getLowerBound() > boundsDistractionToDisableGrid){
      lineChart.setHorizontalGridLinesVisible(false);
      xNumberAxis.setTickMarkVisible(false);
    } else {
      lineChart.setHorizontalGridLinesVisible(true);
      xNumberAxis.setTickMarkVisible(true);
    }

    if (yNumberAxis.getUpperBound() - yNumberAxis.getLowerBound() > boundsDistractionToDisableGrid){
      lineChart.setVerticalGridLinesVisible(false);
      yNumberAxis.setTickMarkVisible(false);
    } else {
      lineChart.setVerticalGridLinesVisible(true);
      yNumberAxis.setTickMarkVisible(true);
    }

  }

  @FXML
  private void initialize(){
    ChartPanManager panner = new ChartPanManager(lineChart);
    panner.setMouseFilter(mouseEvent -> {
      updateAxisGrid();
      if (mouseEvent.getButton() == MouseButton.PRIMARY && mainController.getCurrentDot() == null) {
      } else {
        mouseEvent.consume();
      }
    });
    panner.start();

    JFXChartUtil.setupZooming(lineChart, mouseEvent -> {
      updateAxisGrid();
      if (mouseEvent.getButton() != MouseButton.SECONDARY)
        mouseEvent.consume();
    });

    TableController.setPattern(leftBoundTextField, rightBoundTextField);

    leftBound = -Double.MAX_VALUE;
    rightBound = Double.MAX_VALUE;

  }

  public void setController(MainController mainController) {
    this.mainController = mainController;
  }


  private void updateChartOnClickListener(Node node){
    node.setOnMouseClicked(event -> {

      Dot chosenDot = (Dot) node.getUserData();

      lineChart.setAnimated(false);

      mainController.setCurrentDot(chosenDot);

      updateCurrentDotLabel();
    });
  }

  private void updateChartOnDraggedListener(Node node, Data<Double, Double> data){
    node.setOnMouseDragged(e -> {

      Dot chosenDot = (Dot) node.getUserData();

      lineChart.setAnimated(false);

      mainController.setCurrentDot(chosenDot);

      updateCurrentDotLabel();

      Point2D pointInScene = new Point2D(e.getSceneX(), e.getSceneY());
      double xAxisLoc = xNumberAxis.sceneToLocal(pointInScene).getX();
      double yAxisLoc = yNumberAxis.sceneToLocal(pointInScene).getY();
      Double x = (Double) xNumberAxis.getValueForDisplay(xAxisLoc);
      Double y = (Double) yNumberAxis.getValueForDisplay(yAxisLoc);

      mainController.getCurrentDot().setPosX(x);
      mainController.getCurrentDot().setPosY(y);

      updateCurrentDotLabel();
      mainController.getTableController().updateTable();
//        controller.getTableController().debugDots();
      data.setXValue(x);
      data.setYValue(y);

    });
  }

  public void updateGraph(){

    lineChart.getData().clear();
    List<Dot> dots = mainController.getTableController().dots;

    if (dots == null || dots.isEmpty())
      return;

    series
            = new Series<>(FXCollections.observableArrayList());



    for (Dot dot: dots) {

      if (dot.getPosX() >= leftBound && dot.getPosX() <= rightBound) {
        Data<Double, Double> data = new Data<>(dot.getPosX(), dot.getPosY(), dot);

        series.getData().add(data);
      }
    }

    lineChart.getData().add(series);

    for (Data<Double, Double> data : series.getData()) {

      Node node = data.getNode();

      node.setUserData(data.getExtraValue());

      node.setCursor(Cursor.HAND);

      updateChartOnClickListener(node);

      updateChartOnDraggedListener(node, data);

    }
  }

  @FXML
  public void updateCurrentDotLabel(){
    Dot dot = mainController.getCurrentDot();
    if (dot == null) {
      currentDotLabel.setText("No chosen dots");
      return;
    }

    this.currentDotLabel.setText("(" + String.format("%,.2f",
            dot.getPosX()) + ", " + String.format("%,.2f", dot.getPosY()) + ")");

  }

  @FXML
  public void deleteCurrentDot(){
    currentDotLabel.setText("Current dot");
    mainController.deleteCurrentDot();

  }

  private void changeAxisBounds(NumberAxis numberAxis, double value){
    numberAxis.setLowerBound(numberAxis.getLowerBound() - value);
    numberAxis.setUpperBound(numberAxis.getUpperBound() + value);
  }

  @FXML
  public void zoomGraphIn(){
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
    xNumberAxis.setLowerBound(defaultLowerBound);
    xNumberAxis.setUpperBound(defaultUpperBound);
    yNumberAxis.setLowerBound(defaultLowerBound);
    yNumberAxis.setUpperBound(defaultUpperBound);
    updateAxisGrid();
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

    updateSetGapButtonText();

    double leftValue;
    double rightValue;

    if (mainController.isFieldEmpty(leftBoundTextField))
      leftValue = - Double.MAX_VALUE;
    else
      leftValue = Double.parseDouble(leftBoundTextField.getText());

    if (mainController.isFieldEmpty(rightBoundTextField))
      rightValue = Double.MAX_VALUE;
    else
      rightValue = Double.parseDouble(rightBoundTextField.getText());


    if (rightValue < leftValue) {
      mainController.markWrongFields(leftBoundTextField, rightBoundTextField);
      return;
    }

    leftBound = leftValue;
    rightBound = rightValue;
    updateGapLabel();
    leftBoundTextField.clear();
    rightBoundTextField.clear();

    updateGraph();
  }

  private void updateSetGapButtonText(){
    if (leftBoundTextField.getText().equals("")
            && rightBoundTextField.getText().equals(""))
      setGapButton.setText("Set gap");
    else setGapButton.setText("Reset gap");
  }

  @FXML
  private void resetCurrentDot(){
    mainController.resetCurrentDot();
  }
}
