package View;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//import javax.swing.*;
//import javax.swing.filechooser.FileNameExtensionFilter;

//
///**
// * <code>FileChooser</code> sets file explorer adapted to text files
// */
public class FileChooser {
   public BufferedImage img;

    public FileChooser(){
        img = loadImage("strawberry.jpg");
//        JFrame frame = new JFrame();
//        frame.getContentPane().setLayout(new FlowLayout());
//        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
//        frame.pack();
//        frame.setVisible(true);
    }


    public BufferedImage loadImage(String url) {
        try {
            img = ImageIO.read(new File(url));
        } catch (IOException io){}
        return img;
    }
}
