package Model.Filters.FilterInterface;

import java.awt.image.BufferedImage;

public interface Filter {
    void runFiltering(BufferedImage image, int startX, int endX, int startY, int endY);

    int[] getMyRGB(BufferedImage image, int x, int y);

    void setMyRGB(BufferedImage image, int x, int y, int[] argb);
}
