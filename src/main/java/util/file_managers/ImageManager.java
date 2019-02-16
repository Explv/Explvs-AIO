package util.file_managers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageManager extends ResourceManager {

    public static BufferedImage loadImage(final String relativeImagePath) {
        try (InputStream imageInputStream = loadFile(relativeImagePath)) {
            if (imageInputStream == null) {
                return null;
            }

            return ImageIO.read(imageInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
