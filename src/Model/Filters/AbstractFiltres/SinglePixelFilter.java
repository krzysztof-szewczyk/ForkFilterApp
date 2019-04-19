package Model.Filters.AbstractFiltres;

import Model.Filters.FilterInterface.Filter;

import java.awt.image.BufferedImage;

public abstract class SinglePixelFilter implements Filter {

    public int[] getMyRGB(BufferedImage image, int x, int y) {
        final int p = image.getRGB(x, y);
        final int[] argb = splitRgb(p);
        return argb;
    }
}
