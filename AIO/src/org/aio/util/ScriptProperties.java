package org.aio.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class ScriptProperties {

    public static final String IGNORE_UPDATE_SCRIPT_VER = "ignore_update_script_ver";

    private static final String PROPERTIES_FILENAME = Paths.get(System.getProperty("user.home"),
                                                               "OSBot",
                                                               "Data",
                                                               "explvs_aio.properties").toString();


    public static void setProperty(final String key, final String value) {
        Properties properties = loadProperties();
        properties.setProperty(key, value);
        saveProperties(properties);
    }

    public static String getProperty(final String key) {
        Properties properties = loadProperties();
        return properties.getProperty(key);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILENAME);
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private static Properties saveProperties(final Properties properties) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(PROPERTIES_FILENAME);
            properties.store(fileOutputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
