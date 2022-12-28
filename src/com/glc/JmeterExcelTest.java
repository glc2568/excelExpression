package com.glc;

import com.alibaba.fastjson.JSON;
import com.sunline.glc.jmeter.CommSunlineUtils;
import org.apache.log4j.PropertyConfigurator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JmeterExcelTest {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JmeterExcelTest.class);
    public static void main(String[] args) throws Exception {

        PropertyConfigurator.configure("config/log4j.properties");
        log.info("==========readExcelPkg=============beging>>>>>>>>>>>>>>>>>>>>>>3");
        String sheet1 = CommSunlineUtils.readExcelPkgJson("/Users/gaoleichao/Desktop/temp/testExcel.xlsx", "Sheet1");
        log.info("===="+sheet1);
        log.info("==========readExcelPkg=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<3");
        log.info("==========readExcelPkg=============beging>>>>>>>>>>>>>>>>>>>>>>2");
        String sheet2 = CommSunlineUtils.readExcelPkgJson("/Users/gaoleichao/Desktop/temp/testExcel.xlsx", "Sheet1","T-3");
        log.info("===="+ sheet2);
        log.info("==========readExcelPkg=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<2");
        String [] str ={"成功","失败"};
        List<String> list =  new ArrayList<String>();
        list.add("Success成功");
        list.add("Fail失败");
        CommSunlineUtils.readOutput("/Users/gaoleichao/Desktop/temp/testExcel.xlsx", "Sheet1","T-7",list);
//        List<String> sheet11 = CommSunlineUtils.getAllCaseName("/Users/gaoleichao/Desktop/temp/testExcel.xlsx", "Sheet1");
//        log.info("==List<String>=="+ sheet11.toString());

    }
}