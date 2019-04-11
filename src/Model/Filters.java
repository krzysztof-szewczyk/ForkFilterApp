package Model;

import java.awt.image.BufferedImage;

public class Filters {

    public void Negative(BufferedImage img, int startX, int endX, int startY, int endY) {
            System.out.println("Computing area: " + startX + ", " + endX + " , " + startY + ", " + endY);
            for (int y = startY; y < endY; y++) {
                for (int x = startX; x < endX; x++) {
                    int p = img.getRGB(x, y);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;

                    //set new RGB value
                    p = (a << 24) | (r << 16) | (g << 8) | b;
                    img.setRGB(x, y, p);
                }
            }
    }

    public void Sepia(BufferedImage img, int startX, int endX, int startY, int endY){
        for(int y = startY; y < endY; y++){
            for(int x = startX; x < endX; x++){
                int p = img.getRGB(x,y);

                int a = (p>>24)&0xff;
                int r = (p>>16)&0xff;
                int g = (p>>8)&0xff;
                int b = p&0xff;

                //calculate tr, tg, tb
                int tr = (int)(0.393*r + 0.769*g + 0.189*b);
                int tg = (int)(0.349*r + 0.686*g + 0.168*b);
                int tb = (int)(0.272*r + 0.534*g + 0.131*b);

                //check condition
                if(tr > 255){
                    r = 255;
                }else{
                    r = tr;
                }

                if(tg > 255){
                    g = 255;
                }else{
                    g = tg;
                }

                if(tb > 255){
                    b = 255;
                }else{
                    b = tb;
                }

                //set new RGB value
                p = (a<<24) | (r<<16) | (g<<8) | b;

                img.setRGB(x, y, p);
            }
        }
    }
}
