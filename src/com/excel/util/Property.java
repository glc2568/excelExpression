package com.excel.util;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 获取配置文件类
 */
public class Property {
    private static final Logger log = LoggerFactory.getLogger(Property.class);

    /**
     * 获取数据库配置
     * @return
     * @throws IOException
     */
    public static Properties getDataPropery() throws IOException {
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/Users/gaoleichao/Desktop/job/temp/excelExpression/config/config.properties"));
        // 使用properties对象加载输入流
        properties.load(bufferedReader);
        //获取key对应的value值
//      properties.getProperty(String key);
        return properties;
    }


    /**
     *  获取log配置
     * @return
     * @throws IOException
     */
    public static void initLog4j() {
        //check configuration file
        Path configurationPath = Paths.get("/Users/gaoleichao/Desktop/job/temp/excelExpression/config/log4j.properties");
        if (!Files.exists(configurationPath) || !Files.isRegularFile(configurationPath)) {
            return;
        }

        try {
            System.setProperty("log4j.defaultInitOverride", "1");
//            DOMConfigurator.configure(configurationPath.toString());
            PropertyConfigurator.configure(configurationPath.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
