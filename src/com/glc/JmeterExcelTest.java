package com.glc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.glc.util.MapRemoveNullUtil;
import com.sunline.glc.jmeter.CommSunlineUtils;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class JmeterExcelTest {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JmeterExcelTest.class);
    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("config/log4j.properties");
//        oldActionCase();
//        excelInsertLine();不支持插入


        excelExport();



    }

    public static void oldActionCase(){
        log.info("==========oldActionCase=============beging>>>>>>>>>>>>>>>>>>>>>>3");
        String sheet1 = CommSunlineUtils.readExcelPkgJson("C:\\Users\\GLC\\Desktop\\temp\\testExcel.xlsx", "Sheet1");
        log.info("===="+sheet1);
        log.info("==========oldActionCase=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<3");
        log.info("==========oldActionCase=============beging>>>>>>>>>>>>>>>>>>>>>>2");
        String sheet2 = CommSunlineUtils.readExcelPkgJson("C:\\Users\\GLC\\Desktop\\temp\\testExcel.xlsx", "Sheet1","T-3");
        log.info("===="+ sheet2);
        log.info("==========oldActionCase=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<2");
        String [] str ={"成功","失败"};
        List<String> list =  new ArrayList<String>();
        list.add("Success成功");
        list.add("Fail失败");
        CommSunlineUtils.readOutput("C:\\Users\\GLC\\Desktop\\temp\\testExcel.xlsx", "Sheet1","T-1",list);
//        List<String> sheet11 = CommSunlineUtils.getAllCaseName("/Users/gaoleichao/Desktop/temp/testExcel.xlsx", "Sheet1");
//        log.info("==List<String>=="+ sheet11.toString());
    }

    //读取原excel文件输出到指定文档中
    public static void excelInsertLine() {
        log.info("==========excelInsertLine=============beging>>>>>>>>>>>>>>>>>>>>>>3");
        //        /Users/gaoleichao/Desktop/temp/testExcel.xlsx
        ///Users/gaoleichao/Desktop/job/04.sh-bank/newCore/外围接口组/03.接口映射结果
        String sheet1 = CommSunlineUtils.readExcelPkgJson("/Users/gaoleichao/Desktop/job/04.sh-bank/newCore/外围接口组/03.接口映射结果/老核心接口分析_对公存款&对私存款-merge.xls", "index");
//        log.info("===="+JSONArray.toJSON(sheet1));
        log.info("==========excelInsertLine=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<3");
        log.info("==========excelInsertLine=============beging>>>>>>>>>>>>>>>>>>>>>>2");
        Map map = JSON.parseObject(sheet1);
        log.info("====map" + map);
        log.info("====map" + map.get("服务场景码"));
        MapRemoveNullUtil.removeNullKey(map);
        int i = 0;
        String sheetName = "";
        for (Object keyIndex : map.keySet()) {

//            log.info("=="+map.size()+"==keyIndex---"+keyIndex+">>>->>>"+i++);
            if (map.size() - 1 == i) {
                log.error("stop");
            }

            String[] strArray = String.valueOf(keyIndex).split(CommSunlineUtils.splitStr);
            if (strArray.length == 3) {
                sheetName = strArray[2];
            } else {
                log.info("列表为空，对应sheet页面不存在！！！"+String.valueOf(keyIndex));
                continue;
            }

            if (!sheetName.equals("S10121T61019")) continue;
            String keyIndexSheet = CommSunlineUtils.readExcelPkgJson("/Users/gaoleichao/Desktop/job/04.sh-bank/newCore/外围接口组/03.接口映射结果/老核心接口分析_对公存款&对私存款-merge.xls", sheetName + "");
            Map keySheet = JSON.parseObject(keyIndexSheet);
            if (keySheet == null) continue;
            MapRemoveNullUtil.removeNullKey(keySheet);
            for (Object key : keySheet.keySet()) {
                log.info("====key---" + key + "---value---" + keySheet.get(key));

                Map aloneCell = JSON.parseObject(keySheet.get(key) + "");

                for (Object aloneKey : aloneCell.keySet()) {

                    log.info("====aloneKey---" + aloneKey + "---aloneCell---" + aloneCell.get(aloneKey));

                    CommSunlineUtils.chooseReadOutput("/Users/gaoleichao/Desktop/job/04.sh-bank/newCore/外围接口组/03.接口映射结果/老核心接口分析_对公存款&对私存款-merge的副本.xls", "S10121T61019", "45-@@-11-@@-新核心英文字段");

                }

            }
        }
    }


            //读取原excel文件输出到指定文档中
    public static void excelExport(){
        log.info("==========excelExport=============beging>>>>>>>>>>>>>>>>>>>>>>3");
    //        /Users/gaoleichao/Desktop/temp/testExcel.xlsx
    ///Users/gaoleichao/Desktop/job/04.sh-bank/newCore/外围接口组/03.接口映射结果
    String sheet1 = CommSunlineUtils.readExcelPkgJson("D:\\shbank\\老核心接口分析_对公存款&对私存款-merge.xls", "index");
//        log.info("===="+JSONArray.toJSON(sheet1));
        log.info("==========excelExport=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<3");
        log.info("==========excelExport=============beging>>>>>>>>>>>>>>>>>>>>>>2");
    Map map = JSON.parseObject(sheet1);
        log.info("====map"+map);
        log.info("====map"+map.get("服务场景码"));
        MapRemoveNullUtil.removeNullKey(map);
    int i =0;
    String sheetName ="";
        for (Object keyIndex:map.keySet()) {

//            log.info("=="+map.size()+"==keyIndex---"+keyIndex+">>>->>>"+i++);
        if (map.size()-1==i){
            log.error("stop");
        }

        String []strArray = String.valueOf(keyIndex).split(CommSunlineUtils.splitStr);
        if (strArray.length ==3){
            sheetName = strArray[2];
        }else {
            log.info("列表为空，对应sheet页面不存在！！！");
            continue;
        }

        if (!sheetName.equals("S10121T61019"))continue;
        String keyIndexSheet = CommSunlineUtils.readExcelPkgJson("D:\\shbank\\老核心接口分析_对公存款&对私存款-merge.xls", sheetName+"");
        Map keySheet = JSON.parseObject(keyIndexSheet);
        if (keySheet==null)continue;
        MapRemoveNullUtil.removeNullKey(keySheet);
        for (Object key:keySheet.keySet()) {
            log.info("====key---"+key+"---value---"+keySheet.get(key));

            Map aloneCell = JSON.parseObject(keySheet.get(key)+"");

            for (Object aloneKey:aloneCell.keySet()) {

                log.info("====aloneKey---"+aloneKey+"---aloneCell---"+aloneCell.get(aloneKey));

                CommSunlineUtils.chooseReadOutput("D:\\shbank\\老核心接口分析_对公存款&对私存款-merge的副本.xls", sheetName,aloneCell.get(aloneKey)+"");

            }

        }

        }




    }





















}