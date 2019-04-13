package Controller;

import Model.FilterInterface.Filter;

import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;

public class FilterManager extends RecursiveAction {
//    FiltersTmp filtersTmp = new FiltersTmp();
    int threshold;
    int startX, endX, startY, endY;
    BufferedImage image;
    Filter filter;
    static int i=0;

    public FilterManager(BufferedImage image, Filter filter, int startX, int endX, int startY, int endY, int threshold) {
        this.image = image;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.threshold = threshold;
        this.filter = filter;
//        System.out.println(this.getClass().getName());
    }

    @Override
    protected void compute() {
        // sequently
        if (endX - startX <= threshold) {
            System.out.println(i++);
//            System.out.println("Computing area: " + startX + ", " + endX + " , " + startY + ", " + endY);
            filter.runFiltering(image, startX, endX, startY, endY);
//            filtersTmp.Sepia(img, startX, endX, startY, endY);
        }
        // simultaneously
        else{
            int middleX = (startX+endX)/2;
            invokeAll(new FilterManager(image, filter, startX, middleX, startY, endY, threshold), new FilterManager(image, filter, middleX, endX, startY, endY, threshold));
        }

    }
}

