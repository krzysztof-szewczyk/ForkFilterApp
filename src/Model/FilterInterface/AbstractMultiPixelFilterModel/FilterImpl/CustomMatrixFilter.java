package Model.FilterInterface.AbstractMultiPixelFilterModel.FilterImpl;

import Model.FilterInterface.AbstractMultiPixelFilterModel.MultiPixelFilter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.awt.image.BufferedImage;

class CustomMatrixFilter extends MultiPixelFilter {

    private int[][] filterMatrix;
    private double rate = 0;

    public CustomMatrixFilter(int[][] filterMatrix) {
        this.filterMatrix = filterMatrix;

        for(int i=0 ; i<filterMatrix.length ; i++){
            for(int j=0 ; j<filterMatrix.length ; j++){
                if(this.rate<filterMatrix[i][j])
                    this.rate = filterMatrix[i][j];
            }
        }
        this.rate = 1/rate;
        System.out.println("Rate is: " + rate);
    }

    @Override
    public void runFiltering(BufferedImage image, int startX, int endX, int startY, int endY) {

        int filterSize = filterMatrix.length;

        int[] argb;
        for(int i=startX ; i<=endX ; i++){
            for(int j=startY ; j<=endY ; j++){
                argb = calculateNewRgb(i,j);
            }
        }

//        int startX = x-(filterSize-1)/2;
//        int startY = y-(filterSize-1)/2;
//        long mean = 0;
//        for(int i=0 ; i<filterSize; i++){
//            for(int j=0 ; j<filterSize ; j++){
//                mean += image.getRGB(startX+i,startY+j);
//            }
//        }
    }

    int[] calculateNewRgb(int x, int y){

        int filterSize = filterMatrix.length;

        int[][][] argbs = new int[filterSize][filterSize][4];

        for(int i=0 ; i<filterMatrix.length ; i++){
            for(int j=0 ; j<filterMatrix.length ; j++){
                int argb = super.getMyRGB()
            }
        }

    }

}
