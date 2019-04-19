package Model.Filters.AbstractFiltres;

import Model.Filters.FilterInterface.Filter;
import javafx.scene.image.PixelReader;

public abstract class MultiPixelFilter implements Filter {

    public int[] getMyRGB(PixelReader pixelReader, int x, int y) {
        final int p = pixelReader.getArgb(x, y);
        final int[] argb = splitRgb(p);
        return argb;
    }
}
