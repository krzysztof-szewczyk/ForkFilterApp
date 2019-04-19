package Model.Filters.FilterInterface;

import java.awt.image.BufferedImage;

public interface Filter {

    void runFiltering(BufferedImage image, int startX, int endX, int startY, int endY);

    default int[] splitRgb(int p){
        final int[] argb = new int[4];
        argb[0] = (p >> 24) & 0xff;
        argb[1] = (p >> 16) & 0xff;
        argb[2] = (p >> 8) & 0xff;
        argb[3] = p & 0xff;
        return argb;
    }

    default void setMyRGB(BufferedImage image, int x, int y, int[] argb){
        final int p = (argb[0] << 24) | (argb[1] << 16) | (argb[2] << 8) | argb[3];
        image.setRGB(x, y, p);
    }
}
