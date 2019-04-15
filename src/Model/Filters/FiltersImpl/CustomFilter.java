package Model.Filters.FiltersImpl;

import Model.Filters.AbstractFiltres.SinglePixelFilter;

import java.awt.image.BufferedImage;

final public class CustomFilter extends SinglePixelFilter {

    private int[] addRgb;

    public CustomFilter(int[] addRgb) {
        this.addRgb = addRgb;
    }

    @Override
    public void runFiltering(BufferedImage image, int startX, int endX, int startY, int endY) {

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {

                //get my RGB values
                final int[] argb = super.getMyRGB(image, x, y);

                // negative filter
                for(int i=1 ; i<argb.length ; i++){
                    final int newValue = argb[i] + addRgb[i-1];
                    if(newValue>255){
                        argb[i] = 255;
                    }else if(newValue<0){
                        argb[i] = 0;
                    }else{
                        argb[i] = argb[i] + addRgb[i-1];
                    }
                }

                //set my new RGB values
                super.setMyRGB(image, x, y,argb);
            }
        }
    }
}