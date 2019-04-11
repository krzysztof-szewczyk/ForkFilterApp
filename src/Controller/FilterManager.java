package Controller;

import Model.Filters;

import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;

public class FilterManager extends RecursiveAction {
    Filters filters = new Filters();
    int threshold;
    int startX, endX, startY, endY;
    BufferedImage img;

    public FilterManager(BufferedImage im, int xs, int xe, int ys, int ye, int t) {
        img = im;
        startX = xs;
        endX = xe;
        startY = ys;
        endY = ye;
        threshold = t;
    }

    @Override
    protected void compute() {
        if (endX - startX <= threshold) {
            System.out.println("Computing area: " + startX + ", " + endX + " , " + startY + ", " + endY);
            filters.Negative(img, startX, endX, startY, endY);
            filters.Sepia(img, startX, endX, startY, endY);
        }
        else{
            int middleX = (startX+endX)/2;
            invokeAll(new FilterManager(img, startX, middleX, startY, endY, threshold), new FilterManager(img, middleX, endX, startY, endY, threshold));
        }

    }
}

