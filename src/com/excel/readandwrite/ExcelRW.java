package com.excel.readandwrite;

import com.alibaba.fastjson.JSON;
import com.excel.pojo.Adpm;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelRW {

    public void getXlsxAndXls(String filePath) {
        if (filePath.indexOf(".xlsx") != -1) {
            readFromExcelXSSF(filePath);
        } else {
            readFromExcelHSSF(filePath);

        }
    }

    // 实现读学生文件，将读出的信息存放于Adpm集合中
    public static List<Adpm> readFromExcelHSSF(String fileName) {
        List<Adpm> list = new ArrayList<>();
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            HSSFWorkbook book = new HSSFWorkbook(in);
            // 得到第一个Sheet页
            HSSFSheet sheet = book.getSheetAt(0);
            HSSFRow row;

            for (int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Adpm adpm = new Adpm();
                row = sheet.getRow(i);
                int j = row.getFirstCellNum();
                adpm.setDepartment(row.getCell(j).toString());
                adpm.setCompany(row.getCell(j + 1).toString());
                adpm.setMode(row.getCell(j + 2).toString());
                adpm.setWorkType(row.getCell(j + 3).toString());
                adpm.setDevelopArea(row.getCell(j + 4).toString());
                adpm.setPersonLevel(row.getCell(j + 5).toString());
                adpm.setName(row.getCell(j + 6).toString());
                adpm.setUserName(row.getCell(j + 7).toString());
                adpm.setWorkDate(row.getCell(j + 8).toString());
                adpm.setWeek(row.getCell(j + 9).toString());
                adpm.setTaskCategories(row.getCell(j + 10).toString());
                adpm.setTaskCategory(row.getCell(j + 11).toString());
                adpm.setTaskName(row.getCell(j + 12).toString());
                adpm.setTaskNumber(row.getCell(j + 13).toString());
                adpm.setTaskDesc(row.getCell(j + 14).toString());
                adpm.setActualHours(row.getCell(j + 15).toString());
                adpm.setDemandType(row.getCell(j + 16).toString());
                adpm.setDemandNumber(row.getCell(j + 17).toString());
                adpm.setDemandName(row.getCell(j + 18).toString());
                adpm.setApplyName(row.getCell(j + 19).toString());
                adpm.setApplyID(row.getCell(j + 20).toString());
                System.out.println("adpmRead====\t"+ JSON.toJSONString(adpm));
                list.add(adpm);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public static List<Adpm> readFromExcelXSSF(String fileName) {
        List<Adpm> list = new ArrayList<>();
        FileInputStream in = null;
        try {
            in = new FileInputStream(fileName);
            XSSFWorkbook book = new XSSFWorkbook(in);
            // 得到第一个Sheet页
            XSSFSheet sheet = book.getSheetAt(0);
            XSSFRow row;
            for (int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Adpm adpm = new Adpm();
                row = sheet.getRow(i);
                int j = row.getFirstCellNum();
                adpm.setDepartment(row.getCell(j).toString());
                adpm.setCompany(row.getCell(j + 1).toString());
                adpm.setMode(row.getCell(j + 2).toString());
                adpm.setWorkType(row.getCell(j + 3).toString());
                adpm.setDevelopArea(row.getCell(j + 4).toString());
                adpm.setPersonLevel(row.getCell(j + 5).toString());
                adpm.setName(row.getCell(j + 6).toString());
                adpm.setUserName(row.getCell(j + 7).toString());
                adpm.setWorkDate(row.getCell(j + 8).toString());
                adpm.setWeek(row.getCell(j + 9).toString());
                adpm.setTaskCategories(row.getCell(j + 10).toString());
                adpm.setTaskCategory(row.getCell(j + 11).toString());
                adpm.setTaskName(row.getCell(j + 12).toString());
                adpm.setTaskNumber(row.getCell(j + 13).toString());
                adpm.setTaskDesc(row.getCell(j + 14).toString());
                adpm.setActualHours(row.getCell(j + 15).toString());
                adpm.setDemandType(row.getCell(j + 16).toString());
                adpm.setDemandNumber(row.getCell(j + 17).toString());
                adpm.setDemandName(row.getCell(j + 18).toString());
                adpm.setApplyName(row.getCell(j + 19).toString());
                adpm.setApplyID(row.getCell(j + 20).toString());
                System.out.println("adpmRead====\t"+ JSON.toJSONString(adpm));
                list.add(adpm);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    /*
     * 将List集合数据写入excel（单个sheet）
     *
     * @param excelTitle    文件表头
     * @param list          要写入的数据集合
     * @param sheetName     sheet名称
     */
    // 将集合中的数据写入到excel文件中
    public void WriteExcel(List<Adpm> list, String fileName) {
        Workbook workbook = new HSSFWorkbook();

        //create sheet
        String sheetName = "adpm";
        String[] excelTitle = {"部门","公司","驻场模式","工作类型","开发领域","人员等级","姓名","用户名","工作日期","星期",
                "任务描述","实际工时","需求类型","需求编号","需求名称","应用名称","应用标识"};
        Sheet sheet = workbook.createSheet(sheetName);
        int rowIndex = 0; // 标识位，用于标识sheet的行号
        // 遍历数据集，将其写入excel中
        try {
            // 写表头数据
            Row titleRow = sheet.createRow(rowIndex);
            for (int i = 0; i < excelTitle.length; i++) {
                // 创建表头单元格,填值
                titleRow.createCell(i).setCellValue(excelTitle[i]);
            }
            rowIndex++;
            // 循环写入主表数据
            for (Iterator<Adpm> iterator = list.iterator();
                 iterator.hasNext(); ) {
                Adpm adpm = iterator.next();
                // create sheet row
                Row row = sheet.createRow(rowIndex);
                // create sheet column(单元格)
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(adpm.getDepartment());
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(adpm.getCompany());
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(adpm.getMode());
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(adpm.getWorkType());
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(adpm.getDevelopArea());
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(adpm.getPersonLevel());
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(adpm.getName());
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(adpm.getUserName());
                Cell cell8 = row.createCell(8);
                cell8.setCellValue(adpm.getWorkDate());
                Cell cell9 = row.createCell(9);
                cell9.setCellValue(adpm.getWeek());
                Cell cell10 = row.createCell(10);
                cell10.setCellValue(adpm.getTaskCategories());
                Cell cell11 = row.createCell(11);
                cell11.setCellValue(adpm.getTaskCategory());
                Cell cell12 = row.createCell(12);
                cell12.setCellValue(adpm.getTaskName());
                Cell cell13 = row.createCell(13);
                cell13.setCellValue(adpm.getTaskNumber());
                Cell cell14 = row.createCell(14);
                cell14.setCellValue(adpm.getTaskDesc());
                Cell cell15 = row.createCell(15);
                cell15.setCellValue(adpm.getActualHours());
                Cell cell16 = row.createCell(16);
                cell16.setCellValue(adpm.getDemandType());
                Cell cell17 = row.createCell(17);
                cell17.setCellValue(adpm.getDemandNumber());
                Cell cell18 = row.createCell(18);
                cell18.setCellValue(adpm.getDemandName());
                Cell cell19 = row.createCell(19);
                cell19.setCellValue(adpm.getApplyName());
                Cell cell20 = row.createCell(20);
                cell20.setCellValue(adpm.getApplyID());
                rowIndex++;
            }
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
