package rs.etf.km123247m.PropertyManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Miloš Krsmanović.
 * May 2014
 * <p/>
 * package: rs.etf.km123247m.PropertyManager
 */
public abstract class PropertyManager {

    public static final String DEFAULT_PROPERTIES_FILE_NAME = "configuration.properties";
    private static String propertiesFileName;
    private static Properties properties = null;

    public static String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    protected static String getPropertiesFileName() {
        if(propertiesFileName == null) {
            propertiesFileName = DEFAULT_PROPERTIES_FILE_NAME;
        }
        return propertiesFileName;
    }

    public static void setPropertiesFileName(String propertiesFileName) {
        PropertyManager.propertiesFileName = propertiesFileName;
    }

    protected static Properties getProperties() {
        if(properties == null) {
            properties = new Properties();
            try {
                InputStream is = new FileInputStream(getPropertiesFileName());
                // load the properties file
                properties.load(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }
}
