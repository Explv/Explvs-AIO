package util.file_managers;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
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

    private static final String GITHUB_URL = "https://github.com/Explv/Explvs-AIO";

    public static InputStream loadFile(final String relativeFilePath) {
        if (fileExistsInDataDir(relativeFilePath)) {
            return loadFileFromDataDir(relativeFilePath);
        }

        // TODO: Loading files from local .jars currently seems to be broken
        // As a stop-gap, we download the files from GitHub if they don't exist locally
        // or are empty.
        saveFileFromGitHubToDataDirectory(relativeFilePath);
        return loadFileFromDataDir(relativeFilePath);

//
//        InputStream jarInputStream = loadFileFromJar(relativeFilePath);
//
//        if (jarInputStream != null) {
//            saveFileToDataDirectory(jarInputStream, relativeFilePath);
//            return loadFileFromDataDir(relativeFilePath);
//        }
//
//        System.out.println("Failed to load file");
//        return null;
    }

    private static InputStream loadFileFromJar(final String relativeFilePath) {
        String jarFilePath = "/" + relativeFilePath;
        return ResourceManager.class.getResourceAsStream(jarFilePath);
    }

    private static boolean fileExistsInDataDir(final String relativeFilePath) {
        File file = Paths.get(DIRECTORY, relativeFilePath).toFile();
        return file.exists() && file.length() > 0;
    }

    private static InputStream loadFileFromDataDir(final String relativeFilePath) {
        File file = Paths.get(DIRECTORY, relativeFilePath).toFile();

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

    private static void saveFileFromGitHubToDataDirectory(final String relativeFilePath) {
        try {
            URL url = new URL(GITHUB_URL + "/blob/master/src/main/resources/" + relativeFilePath + "?raw=true");
            System.out.println("Saving file from url: " + url.toString());

            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(Paths.get(DIRECTORY, relativeFilePath).toFile());
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
