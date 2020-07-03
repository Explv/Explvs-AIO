package util.file_managers;

import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
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

        InputStream jarInputStream = loadFileFromJar(relativeFilePath);

        if (jarInputStream != null) {
            saveFileToDataDirectory(jarInputStream, relativeFilePath);
            return loadFileFromDataDir(relativeFilePath);
        }

        // TODO: Loading files from local .jars currently seems to be broken
        // As a stop-gap, we download the files from GitHub if they don't exist locally
        // or are empty.
        downloadResourcesFromGitHubToDataDirectory();
        return loadFileFromDataDir(relativeFilePath);
    }

    private static InputStream loadFileFromJar(final String relativeFilePath) {
        String jarFilePath = "/resources/" + relativeFilePath;
        System.out.println(jarFilePath);
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

    private static void downloadResourcesFromGitHubToDataDirectory() {
        try {
            URL url = new URL(GITHUB_URL + "/blob/master/resources-archive.zip?raw=true");
            System.out.println("Downloading file from url: " + url.toString() + ", to: " + DIRECTORY);

            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

            File outputFile = Paths.get(DIRECTORY, "resources-archive.zip").toFile();

            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            System.out.println(String.format("Extracting file: %s, to: %s", outputFile.getAbsolutePath(), DIRECTORY));

            ZipFile zipFile = new ZipFile(outputFile);
            zipFile.extractAll(DIRECTORY);

            outputFile.delete();
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
