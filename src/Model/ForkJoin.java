package Model;


import View.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class ForkJoin {
    public ForkJoin(){
        ForkJoinPool fjp = new ForkJoinPool();
        FileChooser fc = new FileChooser();
        int threshold = (fc.img.getWidth() / 2) * (fc.img.getHeight() / 2);

        Filter task = new Filter(fc.img, 0, fc.img.getWidth(), 0, fc.img.getHeight(), threshold);
        fjp.invoke(task);

        File output = new File("grayscale.jpg");
        try {
            ImageIO.write(fc.img, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
