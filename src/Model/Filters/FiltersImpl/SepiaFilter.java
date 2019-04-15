package Model.Filters.FiltersImpl;

import Model.Filters.AbstractFiltres.SinglePixelFilter;

import java.awt.image.BufferedImage;

final public class SepiaFilter extends SinglePixelFilter {

    @Override
    public void runFiltering(BufferedImage image, int startX, int endX, int startY, int endY) {
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {

                //get my RGB values
                final int[] argb = super.getMyRGB(image, x, y);

                //sepia filter
                final double[][] sepia = {
                        {0.393,0.769,0.189},
                        {0.349,0.686,0.168},
                        {0.272,0.534,0.131}
                };

                final int[] sepiaRGB = new int[3];

                for(int i = 0 ; i < sepia.length; i++){
                    double tmp = 0;
                    for(int j = 0 ; j<sepia[1].length; j++) {
                        tmp += sepia[i][j]*argb[j+1];
                    }
                    sepiaRGB[i] = (int) tmp;
                    if(sepiaRGB[i]>255){
                        argb[i+1]=255;
                    }else{
                        argb[i+1] = sepiaRGB[i];
                    }
                }

                //set my new RGB values
                super.setMyRGB(image, x, y, argb);
            }
        }
    }
}
