package org.aio.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class ResourceManager {

    private static final String DIRECTORY = Paths.get(
            System.getProperty("user.home"),
            "OSBot",
            "Data",
            "explv_aio"
    ).toString();

    public static BufferedImage getImage(final String imageName) {
        BufferedImage image = getImageFromDataDirectory(imageName);

        if (image == null) {
            image = getImageFromJar(imageName);

            if (image != null) {
                saveImageToDataDirectory(image, imageName);
            }
        }

        return image;
    }

    public static boolean saveImageToDataDirectory(final BufferedImage bufferedImage, final String imageName) {
        File imageFile = Paths.get(DIRECTORY, "/resources/", imageName).toFile();

        File directory = imageFile.getParentFile();

        if (directory != null && !directory.isDirectory() && !imageFile.mkdirs()) {
            System.out.println("Failed to make resources directory");
            return false;
        }


        try {
            ImageIO.write(bufferedImage, getFileExtension(imageFile), imageFile);
        } catch (IOException e) {
            System.out.println("Failed to save image to data directory");
            return false;
        }

        return true;
    }

    private static String getFileExtension(final File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    private static BufferedImage getImageFromJar(final String imageName) {
        BufferedImage image = null;
        try (InputStream inputStream = ResourceMode.class.getResourceAsStream("/resources/" + imageName)) {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            System.out.println("Failed to read image from .jar");
        }
        return image;
    }

    private static BufferedImage getImageFromDataDirectory(final String imageName) {
        File imageFile = Paths.get(DIRECTORY, "/resources/", imageName).toFile();

        if (!imageFile.exists()) {
            System.out.println("Image does not exist in data directory");
            return null;
        }

        try {
            return ImageIO.read(imageFile);
        } catch (IOException e) {
            System.out.println("Failed to read image in data directory");
        }

        return null;
    }
}
