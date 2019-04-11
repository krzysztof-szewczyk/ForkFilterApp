package Model;

import View.FileChooser;
import Controller.FilterManager;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ForkJoinPool;

public class ForkJoin {
    int threadNumber = 10;

    public ForkJoin(){
        ForkJoinPool fjp = new ForkJoinPool();
        FileChooser fc = new FileChooser();
        int threshold = fc.img.getWidth() / threadNumber;

        FilterManager task = new FilterManager(fc.img, 0, fc.img.getWidth(), 0, fc.img.getHeight(), threshold);
        long beginT = System.nanoTime();
        fjp.invoke(task);
//        task.join();
        long endT = System.nanoTime();

        System.out.println("Time: "+(endT-beginT)/1000000);
        File output = new File("grayscale.jpg");
        try {
            ImageIO.write(fc.img, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
