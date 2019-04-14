package Model.FilterInterface.AbstractMultiPixelFilterModel;

import Model.FilterInterface.Filter;

import java.awt.image.BufferedImage;

public abstract class MultiPixelFilter implements Filter {

    private BufferedImage image;

//    public int[][][] getMyRGBs(BufferedImage image, int x, int y, int[][] filter) {

//        return argbs;
//    }

    @Override
    public int[] getMyRGB(BufferedImage image, int x, int y) {
        int p = image.getRGB(x, y);
        int[] argb = new int[4];
        argb[0] = (p >> 24) & 0xff;
        argb[1] = (p >> 16) & 0xff;
        argb[2] = (p >> 8) & 0xff;
        argb[3] = p & 0xff;
        return argb;
    }

    @Override
    public void setMyRGB(BufferedImage image, int x, int y, int[] argb) {
        int p = (argb[0] << 24) | (argb[1] << 16) | (argb[2] << 8) | argb[3];
        image.setRGB(x, y, p);
    }
}
