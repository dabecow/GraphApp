package edu.oreluniver.practice.graphapp.view;

import edu.oreluniver.practice.graphapp.model.Dot;
import javafx.fxml.FXML;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Vector;

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

  public static BufferedImage createImage(JComponent component)
  {
    Dimension d = component.getSize();

    if (d.width == 0 || d.height == 0)
    {
      d = component.getPreferredSize();
      component.setSize( d );
    }

    Rectangle region = new Rectangle(0, 0, d.width, d.height);
    return createImage(component, region);
  }

  /*
   *  Create a BufferedImage for Swing components.
   *  All or part of the component can be captured to an image.
   *
   *  @param  component Swing component to create image from
   *  @param  region The region of the component to be captured to an image
   *  @return	image the image for the given region
   */
  public static BufferedImage createImage(JComponent component, Rectangle region)
  {
    //  Make sure the component has a size and has been layed out.
    //  (necessary check for components not added to a realized frame)

    if (! component.isDisplayable())
    {
      Dimension d = component.getSize();

      if (d.width == 0 || d.height == 0)
      {
        d = component.getPreferredSize();
        component.setSize( d );
      }

      layoutComponent( component );
    }

    BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();

    //  Paint a background for non-opaque components,
    //  otherwise the background will be black

    if (! component.isOpaque())
    {
      g2d.setColor( component.getBackground() );
      g2d.fillRect(region.x, region.y, region.width, region.height);
    }

    g2d.translate(-region.x, -region.y);
    component.print( g2d );
    g2d.dispose();
    return image;
  }

  static void layoutComponent(Component component)
  {
    synchronized (component.getTreeLock())
    {
      component.doLayout();

      if (component instanceof Container)
      {
        for (Component child : ((Container)component).getComponents())
        {
          layoutComponent(child);
        }
      }
    }
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
