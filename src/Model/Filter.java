package Model;

import java.awt.image.BufferedImage;
import java.util.concurrent.RecursiveAction;

public class Filter extends RecursiveAction {

    private int threshold;
    private int startX, endX, startY, endY;
    private BufferedImage img;

    public Filter(BufferedImage im, int xs, int xe, int ys, int ye, int t) {
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
//            System.out.println("Computing area: " + startX + ", " + endX + " , " + startY + ", " + endY);
            for (int y = startY; y < endY; y++) {
                for (int x = startX; x < endX; x++) {
                    int p = img.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;
//                    a = 0;
//                    r = 0;
//                    g = 0;
//                    b = 0;

                    //set new RGB value
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(x, y, p);

                }
            }

        }
        else{
            int middleX = (startX+endX)/2;
            invokeAll(new Filter(img, startX, middleX, startY, endY, threshold), new Filter(img, middleX, endX, startY, endY, threshold));
        }

    }
}