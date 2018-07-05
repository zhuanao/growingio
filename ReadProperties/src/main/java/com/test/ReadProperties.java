package com.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ReadProperties {

    private String file;

    public ReadProperties(String file)
    {
        this.file = file;
    }

    public Map<String, String> getConfigMap() {
        Properties prop = new Properties();
        try {
            InputStream inStream = new FileInputStream(new File(this.file));
            prop.load(inStream);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Map configMap = new HashMap(prop);

        return configMap;
    }

}
