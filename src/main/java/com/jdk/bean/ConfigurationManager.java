package com.jdk.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Yang fyseus
 * @created 2022/4/5
 * @project MiniDashboard
 */
public class ConfigurationManager {
    private static Properties prop = new Properties();

    static {
        InputStream in = ConfigurationManager.class.getClassLoader().getResourceAsStream("mysql.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    public static int getInteger(String key) {
        return Integer.parseInt(prop.getProperty(key));
    }

    public static long getLong(String key) {
        return Long.parseLong(getProperty(key));
    }
}
