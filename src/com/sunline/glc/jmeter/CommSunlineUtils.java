package com.sunline.glc.jmeter;


import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;




import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 读取 excel表格，兼容2003和2007
 * @author gaolc
 */
public class CommSunlineUtils {

    private static Logger log = Logger.getLogger(CommSunlineUtils.class);

    //1代表执行excel测试案例使用，2代表处理excel表格数据
    public static final int flag = 2;

    //目录页sheet名称
    public static final String sheetCatalog= "index";

    //指定行为列key值
    public static final int keyCellName= 5;
    public static final String str = "英文名称";
    //下标一
    public static final String head = "head";
    //    public static final String input = "输入";
    public static final String inputNm = "输入";
    public static final String input = "input";

    //下标二
    public static final String outputNm = "输出";
    public static final String output = "output";



    //单元格下标分隔符，如：行号-@@-列号-@@-单元格值
    public static String splitStr = "-@@-";
    /** 总行数 */
    private int totalRows = 0;

    /** 总列数 */
    private int totalCells = 0;

    /** 错误信息 */
    private static String errorInfo;

    /** 构造方法 */
    public CommSunlineUtils() {}



    /**
     * 得到总行数
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * 得到总列数
     */
    public int getTotalCells() {
        return totalCells;
    }

    /**
     * 得到错误信息
     */
    public String getErrorInfo() {
        return errorInfo;
    }

    /**
     *
     * 验证excel文件
     *
     * @param：filePath 文件完整路径
     * @return 返回 true 表示文件没有问题
     */
    public static boolean validateExcel(String filePath) {
        /** 检查文件名是否为空或者是否是Excel格式的文件 */
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorInfo = "文件不是excel格式";
            return false;
        }

        /** 检查文件是否存在 */
        File file = new File(filePath);

        if (file == null || !file.exists()) {
            errorInfo = "文件不存在";
            return false;
        }

        return true;
    }

    /**
     * 根据文件名读取excel文件
     *
     * @param filePath 文件完整路径
     * @param ignoreRows 读取数据忽略的行数，比喻第一行不需要读入，忽略的行数为1
     * @param sheetName 为空时，默认读取所有sheet页码
     * @return：List  最后读取的结果，数据结构：List<List<String>>
     */
    public static Map<String,HashMap<String,String>> readInput(String filePath, int ignoreRows, String sheetName, String caseNo) {

        Map<String,HashMap<String,String>>  dataMap = new HashMap<String, HashMap<String, String>>();
        InputStream is = null;

        try {
            /** 验证文件是否合法 */
            if (!validateExcel(filePath)) {
                System.out.println(errorInfo);
                return null;
            }

            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (isExcel2007(filePath)) {
                isExcel2003 = false;
            }

            /** 调用本类提供的根据流读取的方法 */
            File file = new File(filePath);
            is = new FileInputStream(file);
            dataMap = readExcel(is, isExcel2003, ignoreRows,sheetName,caseNo);
            is.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {

                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }

            }
        }

        /** 返回最后读取的结果 */
        return dataMap;
    }



    /**
     * 根据流读取Excel文件
     *
     * @param inputStream
     * @param isExcel2003  是否是2003的表格（xls格式）
     * @param ignoreRows 读取数据忽略的行数，比喻第一行不需要读入，忽略的行数为1
     * @return：List
     */
    public static Map<String,HashMap<String,String>> readExcel(InputStream inputStream, boolean isExcel2003, int ignoreRows, String sheetName, String caseNo) {
        log.info("==========readExcel=============beging>>>>>>>>>>>>>>>>>>>>>>5");
        Map<String,HashMap<String,String>>  dataMap = new HashMap<String, HashMap<String, String>>();

        try {

            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;

            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
            int sheetCt = wb.getNumberOfSheets();
            Map<String, String> allSheetNameANDIndex = getAllSheetNameANDIndex(wb);
            String sheetIndex = allSheetNameANDIndex.get(sheetName);
            if(sheetName != null){
                if(sheetIndex ==null)return null;
                Map<String, String> allCellNameANDIndex = new HashMap<>();
                if (flag==1){
                    allCellNameANDIndex = getAllCellNameANDIndex(wb.getSheetAt(Integer.parseInt(sheetIndex)));
                }else if (flag==2){
                    allCellNameANDIndex = getChooseAllCellNameANDIndex(wb.getSheetAt(Integer.parseInt(sheetIndex)));
                }
                if(caseNo !=null){
                    String caseIndex = allCellNameANDIndex.get(caseNo);
                    if (flag==1){
                        read(dataMap,wb, ignoreRows, sheetIndex,caseIndex,caseNo);
                    }else if (flag==2){
                        chooseRead(dataMap,wb, ignoreRows, sheetIndex,caseIndex,caseNo);
                    }
                }else{
                    for (Map.Entry<String, String> entry : allCellNameANDIndex.entrySet()) {
                        String caseNoName = entry.getKey();
                        String caseIndex = entry.getValue();
                        if (flag==1){
                            read(dataMap,wb, ignoreRows, sheetIndex,caseIndex,caseNo);
                        }else if (flag==2){
                            chooseRead(dataMap,wb, ignoreRows, sheetIndex,caseIndex,caseNo);
                        }
                    }
                }


            }else{
                //读取所有sheet页，暂未赋值所有sheet页返回（不使用）
                for (Map.Entry<String, String> entry : allSheetNameANDIndex.entrySet()) {
//                        String sheetNoName = entry.getKey();
                    String sheetNameValue = entry.getValue();
                    Map<String, String> allCellNameANDIndex = getAllCellNameANDIndex(wb.getSheet(sheetNameValue));
                    for (Map.Entry<String, String> cellEntry : allCellNameANDIndex.entrySet()) {
                        String caseNoName = cellEntry.getKey();
                        String caseIndex = cellEntry.getValue();
                        if (flag==1){
                            read(dataMap,wb, ignoreRows, sheetIndex,caseIndex,caseNo);
                        }else if (flag==2){
                            chooseRead(dataMap,wb, ignoreRows, sheetIndex,caseIndex,caseNo);
                        }                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("==========readExcel=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<5");
        return dataMap;
    }



    /**
     * 直接读取指定路径下Sheet页，指定案例编号的案例，以Key-Value的形式返回
     * @param path 路径
     * @param sheetName Sheet名
     * @return
     *
     */
    public static List<String> getAllCaseName(String path, String sheetName) {
        log.info("==========getAllCaseName=============beging>>>>>>>>>>>>>>>>>>>>>>2");
        List<String> list = new ArrayList<String>();
        Map<String, HashMap<String, String>> stringHashMapMap = readExcelPkg(path, sheetName);
        for(String key :stringHashMapMap.keySet()){
            list.add(key);
        }
        list.sort(Comparator.naturalOrder());
        log.info("=========list===>>>>>>>>>>>>>>>>>>>>>>" + list.toString());
        log.info("==========getAllCaseName=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<2");
        return list;
    }


    /**
     * 直接读取指定路径下Sheet页，指定案例编号的案例，以Key-Value的形式返回
     * @param path 路径
     * @param sheetName Sheet名
     * @param caseNo 案例编号
     * @return
     *
     */
    public static Map<String,String> readExcelPkg(String path, String sheetName, String caseNo) {
        log.info("==========readExcelPkg=============beging>>>>>>>>>>>>>>>>>>>>>>3");

        Map<String,String> map = new HashMap<String, String>() ;
        Map<String, HashMap<String, String>> stringHashMapMap = readInput(path, 0, sheetName, caseNo);
        map = stringHashMapMap.get(caseNo);
        log.info("=========map===>>>>>>>>>>>>>>>>>>>>>>"+stringHashMapMap.toString());
        log.info("==========readExcelPkg=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<3");

        return map;
    }

    /**
     * 直接读取指定路径下Sheet页，指定案例编号的案例，以Key-Value的形式返回
     * @param path 路径
     * @param sheetName Sheet名
     * @param caseNo 案例编号
     * @return
     *
     */
    public static String readExcelPkgJson(String path, String sheetName, String caseNo) {
        log.info("==========readExcelPkgJson=============beging>>>>>>>>>>>>>>>>>>>>>>3");
        Map<String,String> map = new HashMap<String, String>() ;
        Map<String, HashMap<String, String>> stringHashMapMap = readInput(path, 0, sheetName, caseNo);
        map = stringHashMapMap.get(caseNo);
        log.info("=========map===>>>>>>>>>>>>>>>>>>>>>>"+JSON.toJSONString(map));
        log.info("==========readExcelPkgJson=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<3");

        return JSON.toJSONString(map);
    }

    /**
     *读取指定路径下的整个Sheet页，返回一个Map<String,Map<String,String>>其中外层Map的Key为案例编号，
     * 内层Map的Key为Excel的列名
     * @param path 路径
     * @param sheetName Sheet名
     * @return MAP
     */
    public static Map<String,HashMap<String,String>>  readExcelPkg(String path, String sheetName) {
        log.info("==========readExcelPkg=============beging>>>>>>>>>>>>>>>>>>>>>>2");

        Map<String, HashMap<String, String>> stringHashMapMap = readInput(path, 0, sheetName, null);
        log.info("=========map===>>>>>>>>>>>>>>>>>>>>>>"+ JSON.toJSONString(stringHashMapMap));
        log.info("==========readExcelPkg=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<2");
        return stringHashMapMap;
    }

    /**
     *读取指定路径下的整个Sheet页，返回一个Map<String,Map<String,String>>其中外层Map的Key为案例编号，
     * 内层Map的Key为Excel的列名
     * @param path 路径
     * @param sheetName Sheet名
     * @return JSON
     */
    public static String  readExcelPkgJson(String path, String sheetName) {
        log.info("==========readExcelPkgJson=============beging>>>>>>>>>>>>>>>>>>>>>>2");

        Map<String, HashMap<String, String>> stringHashMapMap = readInput(path, 0, sheetName, null);
        log.info("=========map===>>>>>>>>>>>>>>>>>>>>>>"+ JSON.toJSONString(stringHashMapMap));
        log.info("==========readExcelPkgJson=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<2");
        return JSON.toJSONString(stringHashMapMap);
    }


    /**
     * 将excel文件中sheet页名称和下标以<sheet名，下标>返回
     * @param wb
     * @return
     */
    public static Map<String,String> getAllSheetNameANDIndex(Workbook wb){
        log.info("==========getAllSheetNameANDIndex=============beging>>>>>>>>>>>>>>>>>>>>>>");
        log.info("=========wb===>>>>>>>>>>>>>>>>>>>>>>"+wb.toString());
        Map<String,String> map = new HashMap<String, String>() ;
        int sheetCt =wb.getNumberOfSheets();
        if(sheetCt >0){
            for(int i =0; i < sheetCt; i++){
                Sheet sheet = wb.getSheetAt(i);
                map.put(sheet.getSheetName(),i+"");
            }
        }
        log.info("=========map===>>>>>>>>>>>>>>>>>>>>>>"+map.toString());
        log.info("==========getAllCellNameANDIndex=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return map;
    }



    /**
     * 将excel文件中指定sheet页第一列单元格内容和下标以<案例编号，下标>返回
     * @param sheet
     * @return
     */
    public static Map<String,String> getAllCellNameANDIndex(Sheet sheet){
        log.info("==========getAllCellNameANDIndex=============beging>>>>>>>>>>>>>>>>>>>>>>");
        log.info("=========sheet===>>>>>>>>>>>>>>>>>>>>>>");

        Map<String,String> map = new HashMap<String, String>() ;
        int totalRows = sheet.getPhysicalNumberOfRows();
        Row row = null;
        Cell cell1 = null;
        if(sheet !=null){
            for (int i=0; i < totalRows; i++){
                row = sheet.getRow(i);
                cell1 = row.getCell(0);
                cell1.setCellType(Cell.CELL_TYPE_STRING);
                String cellValue0 = cell1.getStringCellValue();
                map.put(cellValue0,i+"");
            }
        }
        log.info("=========map===>>>>>>>>>>>>>>>>>>>>>>"+map.toString());
        log.info("==========getAllCellNameANDIndex=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return map;
    }
    /**
     * 读取数据
     *
     * @param ignoreRows
     * @param ignoreRows 读取数据忽略的行数，比喻第一行不需要读入，忽略的行数为1
     * @reture：List<List<String>>
     */
    private static Map<String,HashMap<String,String>> read(Map<String, HashMap<String, String>> dataMap, Workbook wb, int ignoreRows, String sheetindex, String caseIndex, String caseNo) {
        log.info("==========read=============beging>>>>>>>>>>>>>>>>>>>>>>");

        Map<String, HashMap<String, String>> temMap = new HashMap<String, HashMap<String, String>>();

        if(dataMap == null){
            dataMap = new HashMap<String, HashMap<String, String>>();

        }
        wb.getNumCellStyles();
        /** 得到指定shell，得到第一个输入0 */
        Sheet sheet = wb.getSheetAt(Integer.parseInt(sheetindex));
        /** 得到Excel的行数 */
        Integer totalRows = sheet.getPhysicalNumberOfRows();
        Integer totalCells = null;
        /** 得到Excel的列数，不从表格的第一行得到列数，从忽略之后的，要读取的第一行 获取列数*/
        if (totalRows >= 1 && sheet.getRow(ignoreRows) != null) {
            totalCells = sheet.getRow(ignoreRows).getPhysicalNumberOfCells();
        }
        //获取第一行的值作为对应下列的key
        Row keyRow = sheet.getRow(0);
        Row valueRow = sheet.getRow(Integer.parseInt(caseIndex));
        if (valueRow == null) return null;
        HashMap<String,String> map = new HashMap<String, String>() ;
        log.info("map======================================="+ totalCells);
        /** 循环Excel的列 */
        for (int c = 0; c <= totalCells; c++) {
            //第一行key列
            Cell cellFirstRow =keyRow.getCell(c);
            Cell cell = valueRow.getCell(c);
            String cellKey = "";
            String cellValue = "";
            if(cellFirstRow ==null)continue;
            cellKey = getValueToString(cellFirstRow);
            if (null != cell) {
                cellValue = getValueToString(cell);
                map.put(cellKey,cellValue);
            }else{
                map.put(cellKey,"");
            }
        }
        dataMap.put(caseNo, map);            /** 保存第r行的第c列 */
        log.info("map==========@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=============="+dataMap.toString());
        log.info("==========read=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        return dataMap;
    }

    /**
     * 将excel文件中指定sheet页第一列单元格内容和下标以<案例编号，下标>返回
     * @param sheet
     * @return
     */
    public static Map<String,String> getChooseAllCellNameANDIndex(Sheet sheet){
        log.info("==========getChooseAllCellNameANDIndex=============beging>>>>>>>>>>>>>>>>>>>>>>");
        log.info("=========sheet===>>>>>>>>>>>>>>>>>>>>>>");

        Map<String,String> map = new HashMap<String, String>() ;
        int totalRows = sheet.getPhysicalNumberOfRows();
        Row row = null;
        Cell cell1 = null;
        if(sheet !=null){
            for (int i=0; i < totalRows; i++){
                row = sheet.getRow(i);
                if (row == null)continue;
                cell1 = row.getCell(0);
                if (cell1 == null)continue;
                cell1.setCellType(Cell.CELL_TYPE_STRING);
                String cellValue0 = cell1.getStringCellValue();
                //行号-@@-列号-@@-单元格值.注意：存储处理对应sheet页名
                map.put((i+1)+splitStr+0+splitStr+cellValue0,i+"");
            }
        }
        log.info("=========map===>>>>>>>>>>>>>>>>>>>>>>"+map.toString());
        log.info("==========getChooseAllCellNameANDIndex=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return map;
    }
    /**
     * 读取数据
     *
     * @param ignoreRows
     * @param ignoreRows 读取数据忽略的行数，比喻第一行不需要读入，忽略的行数为1
     * @reture：List<List<String>>
     */
    private static Map<String,HashMap<String,String>> chooseRead(Map<String, HashMap<String, String>> dataMap, Workbook wb, int ignoreRows, String sheetindex, String caseIndex, String caseNo) {
        log.info("==========chooseRead=============beging>>>>>>>>>>>>>>>>>>>>>>");

        Map<String, HashMap<String, String>> temMap = new HashMap<String, HashMap<String, String>>();

        if(dataMap == null){
            dataMap = new HashMap<String, HashMap<String, String>>();

        }
        wb.getNumCellStyles();
        /** 得到指定shell，得到第一个输入0 */
        Sheet sheet = wb.getSheetAt(Integer.parseInt(sheetindex));
        /** 得到Excel的行数 */
        Integer totalRows = sheet.getPhysicalNumberOfRows();
        Integer totalCells = null;
        /** 得到Excel的列数，不从表格的第一行得到列数，从忽略之后的，要读取的第一行 获取列数*/
        if (totalRows >= 1 && sheet.getRow(ignoreRows) != null) {
            totalCells = sheet.getRow(ignoreRows).getPhysicalNumberOfCells();
        }

        int inputIndex = 0;
        int outputIndex = 0;
        //获取第一行的值作为对应下列的key，指定哪一行作为key值L
        int chooseRow = 0;
            if (!sheet.getSheetName().equals(sheetCatalog)){
                chooseRow = keyCellName;//实际取第6行第值，上面有存在默认忽略首行判断
                //获取下标位置
                for (int c = chooseRow; c <= totalRows; c++) {
                    Row nkeyRow = sheet.getRow(c);
                    if (nkeyRow==null)continue;
                    if (inputNm.equals(nkeyRow.getCell(nkeyRow.getFirstCellNum()).getStringCellValue())){
                        inputIndex=c;
                    }else if(outputNm.equals(nkeyRow.getCell(nkeyRow.getFirstCellNum()).getStringCellValue())){
                        outputIndex=c;
                    }

                }

            }



        Row keyRow = sheet.getRow(chooseRow);

        Row valueRow = sheet.getRow(Integer.parseInt(caseIndex));
        if (valueRow == null) return null;
        HashMap<String,String> map = new HashMap<String, String>() ;
        log.info("map======================================="+ totalCells);
        /** 循环Excel的列 */
            for (int c = 0; c <= totalCells; c++) {

                //第一行key列
                Cell cellFirstRow =keyRow.getCell(c);
                Cell cell = valueRow.getCell(c);
                String cellKey = "";
                String cellValue = "";
                if(cellFirstRow ==null || cell == null)continue;

                //获取下标均需要➕1
                Row rw = cellFirstRow.getRow();
                int keyColumnIndex =cellFirstRow.getColumnIndex();
                int keyRowIndex = cellFirstRow.getRowIndex();
                int valueColumnIndex =  cell.getColumnIndex();
                int valueRowIndex = cell.getRowIndex();
                if (valueRowIndex == 100){
                    log.info("100row下标");
                }
                CellStyle style = cellFirstRow.getCellStyle();
                int type = cellFirstRow.getCellType();
                String key = getValueToString(cellFirstRow);
                String value = getValueToString(cell);

                //设置指定的行做为key，结合关键字进行下标设定“关键字”拼接=====================================================================
                if (!sheet.getSheetName().equals(sheetCatalog)){
                    if (key.trim().equals(str)){
                        if (caseNo==null || caseNo =="") {
                            if (valueRowIndex < inputIndex) {
                                caseNo = head;
                            } else if (valueRowIndex > inputIndex && valueRowIndex < outputIndex) {
                                caseNo = input;
                            } else {
                                caseNo = output;
                            }
                        }
                            caseNo=caseNo+splitStr+value;
                    }
                }
                //保证输出的下标一一对应
                valueColumnIndex=valueColumnIndex+1;
                valueRowIndex=valueRowIndex+1;
                //单元格下标分隔符，如：行号-@@-列号-@@-单元格值
                cellKey = valueRowIndex+splitStr+valueColumnIndex+splitStr+ key;
                //单元格下标分隔符，如：行号-@@-列号-@@-单元格值
                if (null != cell) {
                    cellValue = valueRowIndex+splitStr+valueColumnIndex+splitStr+ value;
                    map.put(cellKey,cellValue);
                }else{
                    map.put(cellKey,"");
                }

                if(caseNo ==null || caseNo==""){//如为空，取默认第一列作为key
                    caseNo= cellValue;
                }
            }
        dataMap.put(caseNo, map);            /** 保存第r行的第c列 */
        log.info("map==========@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@=============="+dataMap.toString());
        log.info("==========chooseRead=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        return dataMap;
    }



    public static String getValueToString(Cell cell){
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        String cellValue = "";
            if (null != cell) {
                // 以下是判断数据的类型
                switch (cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC: // 数字

                        // 如果数字是日期类型，就转换成日期
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                            SimpleDateFormat sdf = null;
                            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                                sdf = new SimpleDateFormat("HH:mm");
                            } else {// 日期
                                sdf = new SimpleDateFormat("yyyy年MM月dd日");
                            }
                            Date date = cell.getDateCellValue();
                            cellValue = sdf.format(date);
                        } else if (cell.getCellStyle().getDataFormat() == 31) {
                            // 处理自定义日期格式：yyyy年m月d日(通过判断单元格的格式id解决，id的值是31)
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                            double value = cell.getNumericCellValue();
                            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                            cellValue = sdf.format(date);
                        } else {
                            Double value = cell.getNumericCellValue();
                            CellStyle style = cell.getCellStyle();
                            DecimalFormat format = new DecimalFormat();
                            String temp = style.getDataFormatString();
//                             单元格设置成常规
                            if (temp.equals("General")) {
                                format.applyPattern("#");
                            }
//                                    cellValue = format.format(value);
                            cellValue = value+"";
                        }
                        break;

                    case HSSFCell.CELL_TYPE_STRING: // 字符串
                        cellValue = cell.getStringCellValue();
                        break;

                    case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                        cellValue = cell.getBooleanCellValue() + "";
                        break;

                    case HSSFCell.CELL_TYPE_FORMULA: // 公式
                        cellValue = cell.getCellFormula() + "";
                        break;

                    case HSSFCell.CELL_TYPE_BLANK: // 空值
                        cellValue = "";
                        break;

                    case HSSFCell.CELL_TYPE_ERROR: // 故障
                        cellValue = "非法字符";
                        break;

                    default:
                        cellValue = "未知类型";
                        break;
                }
            }
        return cellValue;
    }



    /**
     * 是否是2003的excel，返回true是2003
     *
     * @param filePath 文件完整路径
     * @return boolean true代表是2003
     */
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * 是否是2007的excel，返回true是2007
     *
     * @param filePath 文件完整路径
     * @return boolean true代表是2007
     */
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }





    public static Map<String,HashMap<String,String>> writeExcel(InputStream inputStream, boolean isExcel2003, String sheetName, String caseNo, List<String> writeStrings, String pathname) throws Exception {
        log.info("==========writeExcel=============beging>>>>>>>>>>>>>>>>>>>>>>5");
        Map<String,HashMap<String,String>>  dataMap = new HashMap<String, HashMap<String, String>>();

        try {

            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;

            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
            Font font= wb.createFont();
            int sheetCt = wb.getNumberOfSheets();
            Map<String, String> allSheetNameANDIndex = getAllSheetNameANDIndex(wb);
            String sheetIndex = allSheetNameANDIndex.get(sheetName);
            if(sheetName != null){
                if(sheetIndex ==null)return null;
                Sheet sheet = wb.getSheetAt(Integer.parseInt(sheetIndex));

                int totalRows = sheet.getPhysicalNumberOfRows();
                Map<String, String> allCellNameANDIndex = getAllCellNameANDIndex(sheet);
                int caseLineIndex = Integer.parseInt(allCellNameANDIndex.get(caseNo));
                int totalCells = sheet.getRow(caseLineIndex).getPhysicalNumberOfCells();
                write(wb,sheet,pathname,writeStrings,caseLineIndex,totalCells+1,font);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("==========writeExcel=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<5");
        return dataMap;
    }

    /**
     *
     * @param inputStream
     * @param isExcel2003
     * @param sheetName
     * @param pathname
     * @return
     * 指定对应单元格进行写入，格式：行号-@@-列号-@@-单元格值
     */
    public static Map<String,HashMap<String,String>> insertLineWriteExcel(InputStream inputStream, boolean isExcel2003, String sheetName, String caseValue, String pathname) throws Exception {
        log.info("==========chooseWriteExcel=============beging>>>>>>>>>>>>>>>>>>>>>>5");
        Map<String,HashMap<String,String>>  dataMap = new HashMap<String, HashMap<String, String>>();

        try {

            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;

            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
                Font font= wb.createFont();
            int sheetCt = wb.getNumberOfSheets();

            Map<String, String> allSheetNameANDIndex = getAllSheetNameANDIndex(wb);
            String sheetIndex = allSheetNameANDIndex.get(sheetName);
            if(sheetName != null){
                if(sheetIndex ==null)return null;
                Sheet sheet = wb.getSheetAt(Integer.parseInt(sheetIndex));
                int totalRows = sheet.getPhysicalNumberOfRows();
                Map<String, String> allCellNameANDIndex = getAllCellNameANDIndex(sheet);

                //指定单元格下标解析。单元格下标分隔符，如：行号-@@-列号-@@-单元格值
                String [] strArray  =caseValue.split(splitStr);
                int rowIndex=0;
                int columnIndex=0;
                String resultValue = "";
                if (strArray.length >0) {
                    rowIndex = Integer.parseInt(strArray[0]) - 1;
                    columnIndex = Integer.parseInt(strArray[1]) - 1;
                    if (strArray.length ==3){
                        resultValue = strArray[2];
                    }
                }else {
                    log.info("写入单元格异常，无下标且内容为空");
                    return null;
                }



                chooseWrite(wb,sheet,pathname,resultValue,rowIndex,columnIndex,font);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("==========chooseWriteExcel=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<5");
        return dataMap;
    }

    /**
     *
     * @param inputStream
     * @param isExcel2003
     * @param sheetName
     * @param pathname
     * @return
     * 指定对应单元格进行写入，格式：行号-@@-列号-@@-单元格值
     */
    public static Map<String,HashMap<String,String>> chooseWriteExcel(InputStream inputStream, boolean isExcel2003, String sheetName, String caseValue, String pathname) throws Exception {
        log.info("==========chooseWriteExcel=============beging>>>>>>>>>>>>>>>>>>>>>>5");
        Map<String,HashMap<String,String>>  dataMap = new HashMap<String, HashMap<String, String>>();

        try {

            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
            Font font = wb.createFont();//声明格式
            int sheetCt = wb.getNumberOfSheets();
            Map<String, String> allSheetNameANDIndex = getAllSheetNameANDIndex(wb);
            String sheetIndex = allSheetNameANDIndex.get(sheetName);
            if(sheetName != null){
                if(sheetIndex ==null)return null;
                Sheet sheet = wb.getSheetAt(Integer.parseInt(sheetIndex));
                int totalRows = sheet.getPhysicalNumberOfRows();
                Map<String, String> allCellNameANDIndex = getChooseAllCellNameANDIndex(sheet);
                //指定单元格下标解析。单元格下标分隔符，如：行号-@@-列号-@@-单元格值
                String [] strArray  =caseValue.split(splitStr);
                int rowIndex=0;
                int columnIndex=0;
                String resultValue = "";
                if (strArray.length >0) {
                    rowIndex = Integer.parseInt(strArray[0]) - 1;
                    columnIndex = Integer.parseInt(strArray[1]) - 1;
                    if (strArray.length ==3){
                        resultValue = strArray[2];
                    }
                }else {
                    log.info("写入单元格异常，无下标且内容为空");
                    return null;
                }



                chooseWrite(wb,sheet,pathname,resultValue,rowIndex,columnIndex,font);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("==========chooseWriteExcel=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<5");
        return dataMap;
    }

    /**
     * 根据文件名读取excel文件
     *
     * @param filePath 文件完整路径
     * @param sheetName 为空时，默认读取所有sheet页码
     * @return：List  最后读取的结果，数据结构：List<List<String>>
     */
    public static Map<String,HashMap<String,String>> readOutput(String filePath, String sheetName, String caseNo,List<String> writeStrings) {
        log.info("==========readOutput=============beging>>>>>>>>>>>>>>>>>>>>>>");


        Map<String,HashMap<String,String>>  dataMap = new HashMap<String, HashMap<String, String>>();
        InputStream is = null;

        try {
            /** 验证文件是否合法 */
            if (!validateExcel(filePath)) {
                System.out.println(errorInfo);
                return null;
            }

            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (isExcel2007(filePath)) {
                isExcel2003 = false;
            }

            /** 调用本类提供的根据流读取的方法 */
            File file = new File(filePath);
            is = new FileInputStream(file);
                dataMap = writeExcel(is, isExcel2003,sheetName,caseNo,writeStrings,filePath);
            is.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {

                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }

            }
        }
        log.info("=========map===>>>>>>>>>>>>>>>>>>>>>>"+dataMap.toString());
        log.info("==========readOutput=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return dataMap;
    }


    /**
     * 根据文件名读取excel文件输出指定单元格位置
     *
     * @param filePath 文件完整路径
     * @param sheetName 为空时，默认读取所有sheet页码
     * @return：List  最后读取的结果，数据结构：List<List<String>>
     */
    public static Map<String,HashMap<String,String>> chooseReadOutput(String filePath, String sheetName, String writeStrings) {
        log.info("==========chooseReadOutput=============beging>>>>>>>>>>>>>>>>>>>>>>");


        Map<String,HashMap<String,String>>  dataMap = new HashMap<String, HashMap<String, String>>();
        InputStream is = null;

        try {
            /** 验证文件是否合法 */
            if (!validateExcel(filePath)) {
                System.out.println(errorInfo);
                return null;
            }

            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (isExcel2007(filePath)) {
                isExcel2003 = false;
            }

            /** 调用本类提供的根据流读取的方法 */
            File file = new File(filePath);
            is = new FileInputStream(file);
            chooseWriteExcel(is, isExcel2003,sheetName,writeStrings,filePath);
            is.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {

                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }

            }
        }
        log.info("=========map===>>>>>>>>>>>>>>>>>>>>>>"+dataMap.toString());
        log.info("==========chooseReadOutput=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        return dataMap;
    }

    /**
     * WriteExcel excel = new WriteExcel("D:\\myexcel.xlsx");
     * excel.write(new String[]{"1","2"}, 0);//在第1行第1个单元格写入1,第一行第二个单元格写入2
     */
    public static void write(Workbook workbook,Sheet wrSheet,String pathname,List<String> writeStrings, int rowNumber,int cellNumber,Font font) {
        log.info("==========write=============beging>>>>>>>>>>>>>>>>>>>>>>");

        //将内容写入指定的行号中
        Row row = wrSheet.getRow(rowNumber);
//        Row row = wrSheet.createRow(rowNumber);
        //遍历整行中的列序号(从设定的列数开始写)
        for (int j = 0; j < writeStrings.size(); j++) {
            //根据行指定列坐标j,然后在单元格中写入数据
            Cell cell = row.createCell(cellNumber+j);
            String result= writeStrings.get(j);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            if(result.contains("成功") || result.contains("SUCCESS") || result.contains("success") ){
//                font.setFontHeightInPoints((short) 12); // 字体高度
//                font.setFontName("宋体"); // 字体
                font.setColor(HSSFColor.GREEN.index);  //颜色
            }else if(result.contains("失败") || result.contains("Fail") || result.contains("FAIL") || result.contains("fail")){
                font.setColor(HSSFColor.RED.index);  //颜色
            }
            XSSFRichTextString xssfTs= null;
            HSSFRichTextString hssfTs=null;
            try {
                xssfTs= new XSSFRichTextString(result);
                xssfTs.applyFont(0,xssfTs.length(),font);
                cell.setCellValue(xssfTs);
            }catch (Exception e){
                hssfTs= new HSSFRichTextString(result);
                hssfTs.applyFont(0,hssfTs.length(),font);
                cell.setCellValue(hssfTs);
            }
        }
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(pathname);
            workbook.write(stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("==========write=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

    }



    /**
     * WriteExcel excel = new WriteExcel("D:\\myexcel.xlsx");
     * excel.write(new String[]{"1","2"}, 0);//在第1行第1个单元格写入1,第一行第二个单元格写入2
     */
    public static void chooseWrite(Workbook workbook,Sheet wrSheet,String pathname,String writeStrings, int rowNumber,int cellNumber,Font font) {
        log.info("==========chooseWrite=============beging>>>>>>>>>>>>>>>>>>>>>>");

        //将内容写入指定的行号中
        Row row = wrSheet.getRow(rowNumber);//获取原来行数据
//        Row row = wrSheet.createRow(30);//创建空行
//        Row row = wrSheet.createRow(rowNumber);
        //遍历整行中的列序号(从设定的列数开始写)
//        for (int j = 0; j < writeStrings.size(); j++) {
            //根据行指定列坐标j,然后在单元格中写入数据
//            Cell cell = row.createCell(1);
            Cell cell = row.getCell(cellNumber);
            String result= writeStrings;
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//            if(result.contains("成功") || result.contains("SUCCESS") || result.contains("success") ){
                font.setFontHeightInPoints((short) 12); // 字体高度
                font.setFontName("宋体"); // 字体
                font.setColor(HSSFColor.RED.index);  //颜色
//            }else if(result.contains("失败") || result.contains("Fail") || result.contains("FAIL") || result.contains("fail")){
//                font.setColor(HSSFColor.RED.index);  //颜色
//            }
        XSSFRichTextString xssfTs= null;
        HSSFRichTextString hssfTs=null;
        try {
             xssfTs= new XSSFRichTextString(result);
            xssfTs.applyFont(0,xssfTs.length(),font);
            cell.setCellValue(xssfTs);
        }catch (Exception e){
            hssfTs= new HSSFRichTextString(result);
            hssfTs.applyFont(0,hssfTs.length(),font);
            cell.setCellValue(hssfTs);
        }


//        }
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(pathname);
            workbook.write(stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("==========chooseWrite=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

    }



    public static void WriteExcel(Workbook workbook,Sheet wrSheet,String excelPath,String sheetName) throws Exception {

        log.info("==========WriteExcel=============beging>>>>>>>>>>>>>>>>>>>>>>");

        //在excelPath中需要指定具体的文件名(需要带上.xls或.xlsx的后缀)
//        pathname = excelPath;
        String fileType = excelPath.substring(excelPath.lastIndexOf(".") + 1, excelPath.length());
        //创建文档对象
        if (fileType.equals("xls")) {
            //如果是.xls,就new HSSFWorkbook()
            workbook = new HSSFWorkbook();
        } else if (fileType.equals("xlsx")) {
            //如果是.xlsx,就new XSSFWorkbook()
            workbook = new XSSFWorkbook();
        } else {
            throw new Exception("文档格式后缀不正确!!！");
        }
        // 创建表sheet
        workbook.createSheet("sheetName");
        log.info("==========WriteExcel=============end<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

    }























}
