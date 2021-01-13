package com.excel;


import com.excel.expression.ExcelExpression;
import com.excel.readandwrite.ExcelRW;
import com.excel.readandwrite.ReadExcel;

public class Main {

    public static void main(String[] args) {
        System.out.println("hello world");
        ExcelExpression LD=new ExcelExpression();

//       ExcelRW rw=new ExcelRW();
//       rw.getXlsxAndXls("/Users/gaoleichao/Desktop/job/adpm/adpm汇总.xlsx");

               ReadExcel rw=new ReadExcel();
//       rw.read("/Users/gaoleichao/Desktop/job/adpm/adpm汇总.xlsx",0);
    }

}


