package util.file_managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ResourceManager {

    private static final String DIRECTORY = Paths.get(
            System.getProperty("user.home"),
            "OSBot",
            "Data",
            "explv_aio",
            "resources"
    ).toString();

    public static InputStream loadFile(final String relativeFilePath) {
        if (fileExistsInDataDir(relativeFilePath)) {
            return loadFileFromDataDir(relativeFilePath);
        }

        InputStream jarInputStream = loadFileFromJar(relativeFilePath);

        if (jarInputStream != null) {
            saveFileToDataDirectory(jarInputStream, relativeFilePath);
            return loadFileFromDataDir(relativeFilePath);
        }

        System.out.println("Failed to load file");
        return null;
    }

    private static InputStream loadFileFromJar(final String relativeFilePath) {
        String jarFilePath = "/resources/" + relativeFilePath;
        System.out.println(String.format("Loading '%s' from jar", jarFilePath));
        return ResourceManager.class.getResourceAsStream(jarFilePath);
    }

    private static boolean fileExistsInDataDir(final String relativeFilePath) {
        File file = Paths.get(DIRECTORY, relativeFilePath).toFile();
        return file.exists();
    }

    private static InputStream loadFileFromDataDir(final String relativeFilePath) {
        File file = Paths.get(DIRECTORY, relativeFilePath).toFile();

        System.out.println(String.format("Loading '%s'", file.toString()));

        if (!file.exists()) {
            System.out.println(
                    String.format("'%s' does not exist", file.toString())
            );
            return null;
        }

        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            System.out.println(
                    String.format("Failed to load file '%s' from '%s'", relativeFilePath, DIRECTORY)
            );
        }

        return null;
    }

    private static synchronized boolean saveFileToDataDirectory(final InputStream inputStream, final String relativeFilePath) {
        if (inputStream == null) {
            return false;
        }

        Path outputFilePath = Paths.get(DIRECTORY, relativeFilePath);
        File parentDir = outputFilePath.getParent().toFile();

        if (!parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                System.out.println("Failed to make directory: " + parentDir.toString());
                return false;
            }
        }

        try {
            Files.copy(
                    inputStream,
                    Paths.get(DIRECTORY, relativeFilePath),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
