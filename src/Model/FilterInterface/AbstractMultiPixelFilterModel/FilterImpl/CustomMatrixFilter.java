package Model.FilterInterface.AbstractMultiPixelFilterModel.FilterImpl;

import Model.FilterInterface.AbstractMultiPixelFilterModel.MultiPixelFilter;
import java.awt.image.BufferedImage;

public class CustomMatrixFilter extends MultiPixelFilter {

    private int[][] filterMatrix;
    private double rate = 0;
    private BufferedImage image;

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

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void runFiltering(BufferedImage image, int startX, int endX, int startY, int endY) {

        this.setImage(image);

        int[] argb;
        for(int i = startX ; i < endX ; i++){
            for(int j = startY ; j < endY ; j++){
                argb = calculateNewRgb(i,j);
                System.out.println(argb.length);
                setMyRGB(image, i, j, argb);
            }
        }
    }

    int[] calculateNewRgb(int x, int y) {
        BufferedImage image = this.getImage();
        int filterSize = filterMatrix.length;

        long[] argbs = {0,0,0,0};

        int startFilterX = x - (filterSize - 1) / 2;
        int startFilterY = y - (filterSize - 1) / 2;

        for (int i = startFilterX; i < filterMatrix.length; i++) {

            if(i<0)
                continue;

            for (int j = startFilterY; j < filterMatrix.length; j++) {

                if(j<0)
                    continue;

                int[] tmp = super.getMyRGB(image, i, j);
                for (int k=0 ; k<argbs.length ; k++){
                    argbs[k] += (tmp[k]*filterMatrix[i][j]);
                }
            }
            System.out.println(argbs[1] + " " + argbs[3]);
        }

        int[] newArgb = new int[4];
        for(int i=0 ; i<argbs.length ; i++){
            newArgb[i] = (int)((double) argbs[i] * rate);
        }


        return newArgb;
    }
}
