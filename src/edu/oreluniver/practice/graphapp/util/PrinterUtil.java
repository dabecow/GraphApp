package edu.oreluniver.practice.graphapp.util;

import edu.oreluniver.practice.graphapp.controller.DotsController;
import edu.oreluniver.practice.graphapp.model.Dot;
import javafx.print.PageLayout;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;

public class PrinterUtil {

    public static boolean printNode(final PrinterJob printerJob, final Node view) {

        final WritableImage snapshot = view.snapshot(null, null);
        final ImageView ivSnapshot = new ImageView(snapshot);
        final PageLayout pageLayout = printerJob.getJobSettings().getPageLayout();
        final double scaleX = pageLayout.getPrintableWidth() / ivSnapshot.getImage().getWidth();
        final double scaleY = pageLayout.getPrintableHeight() / ivSnapshot.getImage().getHeight();
        final double scale = Math.min(scaleX, scaleY);
        if (scale < 1.0) {
            ivSnapshot.getTransforms().add(new Scale(scale, scale));
        }
        return printerJob.printPage(ivSnapshot);
    }

    public static boolean printTable() throws PrinterException {
        Object[] columnNames = {"", "x", "y"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        String format = "%.5f";

        tableModel.addRow(columnNames);

        int i = 1;
        for (Dot dot: DotsController.getInstance().getDotList()) {
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
        return true;
    }

}
