package Model.Filters.AbstractFiltres;

import Model.Filters.FilterInterface.Filter;

import java.awt.image.BufferedImage;

public abstract class SinglePixelFilter implements Filter {

    @Override
    public int[] getMyRGB(BufferedImage image, int x, int y) {
        final int p = image.getRGB(x, y);
        final int[] argb = new int[4];
        argb[0] = (p >> 24) & 0xff;
        argb[1] = (p >> 16) & 0xff;
        argb[2] = (p >> 8) & 0xff;
        argb[3] = p & 0xff;
        return argb;
    }

    @Override
    public void setMyRGB(BufferedImage image, int x, int y, int[] argb) {
        final int p = (argb[0] << 24) | (argb[1] << 16) | (argb[2] << 8) | argb[3];
        image.setRGB(x, y, p);
    }
}
