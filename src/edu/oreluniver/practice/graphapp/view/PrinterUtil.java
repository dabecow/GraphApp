package edu.oreluniver.practice.graphapp.view;

import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;

public class PrinterUtil {
    public static boolean printViewPage(final PrinterJob printerJob, final Node view) {
        // the view needs to be scaled to fit the selected page layout of the PrinterJob
        // => the passed view node can't be scaled, this would scale the displayed UI
        // => solution: create a snapshot image for printing and scale this image
        final WritableImage snapshot = view.snapshot(null, null);
        final ImageView ivSnapshot = new ImageView(snapshot);
        // compute the needed scaling (aspect ratio must be kept)
        final PageLayout pageLayout = printerJob.getJobSettings().getPageLayout();
        final double scaleX = pageLayout.getPrintableWidth() / ivSnapshot.getImage().getWidth();
        final double scaleY = pageLayout.getPrintableHeight() / ivSnapshot.getImage().getHeight();
        final double scale = Math.min(scaleX, scaleY);
        // scale the calendar image only when it's too big for the selected page
        if (scale < 1.0) {
            ivSnapshot.getTransforms().add(new Scale(scale, scale));
        }
        return printerJob.printPage(ivSnapshot);
    }


}
