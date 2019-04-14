package Model.FilterInterface.AbstractMultiPixelFilterModel.FilterImpl;

import Model.FilterInterface.AbstractMultiPixelFilterModel.MultiPixelFilter;
import javafx.scene.image.PixelReader;

import java.awt.image.BufferedImage;

public class CustomMatrixFilter extends MultiPixelFilter {

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
        for(int i=0 ; i<filterMatrix.length ; i++){
            for(int j=0 ; j<filterMatrix.length ; j++){
                if(max<filterMatrix[i][j]){
                    max = filterMatrix[i][j];
                }else if(min>filterMatrix[i][j]){
                    min = filterMatrix[i][j];
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

    int[] calculateNewRgb(BufferedImage image, int x, int y) {

        int filterSize = filterMatrix.length;

        double[] argbs = {0,0,0,0};

        int startFilterX = x - (filterSize - 1) / 2;
        int startFilterY = y - (filterSize - 1) / 2;

        int indX=0;
        for (int i = startFilterX; i < (startFilterX+filterMatrix.length); i++) {

            if(i<0 || i>=image.getWidth()){
                continue;
            }

            int indY = 0;
            for (int j = startFilterY; j < (startFilterY+filterMatrix.length); j++) {

                if(j<0 || j>=image.getHeight())
                    continue;

                int p = this.pixelReader.getArgb(i, j);
                int[] argb = new int[4];
                argb[0] = (p >> 24) & 0xff;
                argb[1] = (p >> 16) & 0xff;
                argb[2] = (p >> 8) & 0xff;
                argb[3] = p & 0xff;

                for (int k=0 ; k<argbs.length ; k++){

                    int tmp = isMinFilterValueNegative ? argb[k]*filterMatrix[indX][indY]+255*minValue : argb[k]*filterMatrix[indX][indY]-255*minValue;
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
