package com.glc;

import com.glc.excel.expression.ExcelExpression;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Main.class);
    public static void main(String[] args) {
//        java.util.logging.Logger log = Logger.getLogger("lavasoft");
//        log.setLevel(Level.FINEST);
        log.info("启动中。。。");
        ExcelExpression LD=new ExcelExpression();
//       ExcelRW rw=new ExcelRW();
//       rw.getXlsxAndXls("/Users/gaoleichao/Desktop/job/adpm/adpm汇总.xlsx");
//       rw.read("/Users/gaoleichao/Desktop/job/adpm/adpm汇总.xlsx",0);
    }

}


