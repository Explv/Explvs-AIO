package util.file_managers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageManager extends ResourceManager {

    private static ImageManager instance;

    private final Map<String, BufferedImage> imageCache = new HashMap<>();

    private ImageManager() {
    }

    synchronized public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }

    public BufferedImage loadImage(final String relativeImagePath) {
        if (imageCache.containsKey(relativeImagePath)) {
            return imageCache.get(relativeImagePath);
        }

        try (InputStream imageInputStream = loadFile(relativeImagePath)) {
            if (imageInputStream == null) {
                return null;
            }

            BufferedImage image =  ImageIO.read(imageInputStream);

            imageCache.put(relativeImagePath, image);

            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
