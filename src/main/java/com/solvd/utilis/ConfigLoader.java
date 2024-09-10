package com.solvd.utilis;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static Properties properties = new Properties();

    static {
        try(InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
