package org.astore.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final String CONFIG_FILE_PATH = "src/main/resources/configurations.properties";
    private static Properties properties;

    static {
        properties = loadProperties();
    }

    private static Properties loadProperties() {
        try (InputStream inputStream = new FileInputStream(CONFIG_FILE_PATH)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            System.err.println("Error loading configuration.properties file: " + e.getMessage());
            return null;
        }
    }

    public static String getBaseURI() {
        return properties.getProperty("baseURI");
    }

    public static String getAdminRegisterEndPoint(){
        return properties.getProperty("admin.register");
    }

    public static String getAdminLoginEndPoint(){
        return properties.getProperty("admin.login");
    }

    public static String getAdminUserDetailsEndpoint(){
        return properties.getProperty("admin.userDetails");
    }

    public static String getCreateCategoryEndPoint(){
        return properties.getProperty("category.create");
    }

    public static String getDeleteCategoryEndPoint(){
        return properties.getProperty("category.delete");
    }

    public static String getReturnCategoryEndPoint(){
        return properties.getProperty("category.return");
    }

    public static String getCreateProductEndPoint(){
        return properties.getProperty("product.return");
    }



    public static String getUserName(){
        return  properties.getProperty("username");
    }

    public static String getPassword(){
        return  properties.getProperty("password");
    }

    public static String getFirstName(){
        return  properties.getProperty("firstName");
    }

    public static String getLastName(){
        return  properties.getProperty("lastName");
    }

    public static String getPhone(){
        return  properties.getProperty("phone");
    }

    public static String getEmail(){
        return  properties.getProperty("email");
    }

}
