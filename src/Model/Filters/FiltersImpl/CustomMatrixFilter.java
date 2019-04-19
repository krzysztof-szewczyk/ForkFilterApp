package Model.Filters.FiltersImpl;

import Model.Filters.AbstractFiltres.MultiPixelFilter;
import javafx.scene.image.PixelReader;

import java.awt.image.BufferedImage;

final public class CustomMatrixFilter extends MultiPixelFilter {

    private int[][] filterMatrix;
    private double rate;
    private int minValue;
    private boolean isMinFilterValueNegative = true;
    private PixelReader pixelReader;

    public CustomMatrixFilter(int[][] filterMatrix, PixelReader pixelReader) {
        this.filterMatrix = filterMatrix;
        this.pixelReader = pixelReader;

        int max = 0;
        int min = 0;
        for (int[] filterVector : filterMatrix) {
            for (int filterElement : filterVector) {
                if (max < filterElement) {
                    max = filterElement;
                } else if (min > filterElement) {
                    min = filterElement;
                }
            }
        }

        if(min>=0){
            isMinFilterValueNegative=false;
            this.minValue = min;
        }else{
            this.minValue = Math.abs(min);
        }
        this.rate = (double) 1/(max-min);
    }

    @Override
    public void runFiltering(BufferedImage image, int startX, int endX, int startY, int endY) {

        int[] argb;
        for(int x = startX ; x < endX ; x++){
            for(int y = startY ; y < endY ; y++){
                argb = calculateNewRgb(image, x,y);
                setMyRGB(image, x, y, argb);
            }
        }
    }

    private int[] calculateNewRgb(BufferedImage image, int x, int y) {

        final int filterSize = filterMatrix.length;

        final double[] argbs = {0,0,0,0};

        final int startFilterX = x - (filterSize - 1) / 2;
        final int startFilterY = y - (filterSize - 1) / 2;

        int indX=0;
        for (int i = startFilterX; i < (startFilterX+filterMatrix.length); i++) {

            // if out of range in X dimension
            if(i<0 || i>=image.getWidth()){
                continue;
            }

            int indY = 0;
            for (int j = startFilterY; j < (startFilterY+filterMatrix.length); j++) {
                // if out of range in Y dimension
                if(j<0 || j>=image.getHeight())
                    continue;

                final int[] argb = super.getMyRGB(this.pixelReader, i, j);

                for (int k=0 ; k<argbs.length ; k++){

                    final int tmp = isMinFilterValueNegative ? argb[k]*filterMatrix[indX][indY]+255*minValue : argb[k]*filterMatrix[indX][indY]-255*minValue;
                    argbs[k] = argbs[k] + (tmp*rate);
                }
                indY++;
            }
            indX++;
        }
        int[] newArgb = new int[4];
        for(int i=0 ; i<argbs.length ; i++){
            newArgb[i] = (int) argbs[i]/(filterSize*filterSize);
        }

        return newArgb;
    }
}
