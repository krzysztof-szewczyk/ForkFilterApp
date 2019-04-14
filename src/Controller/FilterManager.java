package Controller;

import Model.FilterInterface.Filter;
import Model.GuiModel.MyProperties;

import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;

public class FilterManager extends RecursiveAction {
    private int threshold;
    private int startX, endX, startY, endY;
    private BufferedImage image;
    private Filter filter;
    static int i=0;

    public FilterManager(BufferedImage image, Filter filter, int startX, int endX, int startY, int endY, int threshold) {
        this.image = image;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        this.threshold = threshold;
        this.filter = filter;
    }

    @Override
    protected void compute() {
        // sequently
        if (endX - startX <= threshold) {
            i++;
            filter.runFiltering(image, startX, endX, startY, endY);
        }
        // simultaneously
        else{
            int middleX = (startX+endX)/2;
            invokeAll(new FilterManager(image, filter, startX, middleX, startY, endY, threshold), new FilterManager(image, filter, middleX, endX, startY, endY, threshold));
        }
    }
}

