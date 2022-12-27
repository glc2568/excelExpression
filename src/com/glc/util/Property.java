package com.glc.util;

import java.io.BufferedReader;
import java.io.File;
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
    /**
     * 获取数据库配置
     * @return
     * @throws IOException
     */
    public static Properties getDataPropery() throws IOException {
        Properties properties = new Properties();
        // 使用InPutStream流读取properties文件
        BufferedReader bufferedReader = new BufferedReader(new FileReader(System.getProperty("user.dir")+File.separator+"config"+File.separator+"config.properties"));

//        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:"+ File.separator+"glc"+File.separator+"config.properties"));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
