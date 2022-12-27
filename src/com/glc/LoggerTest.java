package com.glc;

import com.sunline.glc.jmeter.CommSunlineUtils;
import org.apache.log4j.PropertyConfigurator;

import java.util.HashMap;
import java.util.Map;


public class LoggerTest {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(LoggerTest.class);

    public static void main(String[] args) {
        PropertyConfigurator.configure("config/log4j.properties");
        CommSunlineUtils re = new CommSunlineUtils();
        Map<String, HashMap<String, String>> sheet1 = re.readExcelPkg("/Users/gaoleichao/Desktop/temp/testExcel.xlsx", "Sheet1");
        log.info("===="+sheet1);
        Map<String, String> sheet2 = re.readExcelPkg("/Users/gaoleichao/Desktop/temp/testExcel.xlsx", "Sheet1","3");
        log.info("===="+sheet2);

    }
}