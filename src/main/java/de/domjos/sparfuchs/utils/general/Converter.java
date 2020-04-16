package de.domjos.sparfuchs.utils.general;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class Converter {

    public static byte[] convertImageToByteArray(Image image) throws Exception {
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", s);
        byte[] res  = s.toByteArray();
        s.close();
        return res;
    }
}
