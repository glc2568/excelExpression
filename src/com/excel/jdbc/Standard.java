package com.excel.jdbc;


import com.excel.expression.ExcelCalc;
import com.excel.pojo.Adpm;
import com.excel.readandwrite.AdpmList;
import com.excel.readandwrite.ReadExcel;
import com.excel.util.Property;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public class Standard {
//    private static final Logger log = LoggerFactory.getLogger(Standard.class);
    public static void main(String[] args) throws IOException {

        String  path ="D:\\WorkProject\\shanghai_bank\\new\\9月-长亮.xls";
        ReadExcel rw=new ReadExcel();
//            List<List<String>> lists = rw.read("D:\WorkProject\shanghai_bank\new\9月-长亮.xls",1);
        List<List<String>> lists = rw.read(path,0);
        String fileName = path.substring(path.lastIndexOf(File.separator)+1);
        for (List<String> list:lists){
            Adpm adpm = AdpmList.getAdpm(list,fileName+"_"+lists.size());
            getSqlcon(adpm);
        }

    }

    public static void loadData(String path){
        ReadExcel rw=new ReadExcel();
        List<List<String>> adpmList = rw.read(path, 1);
        String fileName = path.substring(path.lastIndexOf(File.separator) + 1) + "_" + adpmList.size();
        for (int i = 0; i < adpmList.size(); i++) {
            Adpm adpm = AdpmList.getAdpm(adpmList.get(i),fileName);
            Standard.getSqlcon(adpm);
        }
    }

    public static void getSqlcon(Adpm adpm){
        //获取连接
        Connection con = null;
        //创建statement对象
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        //注册mysql驱动
        try{
            Class.forName(String.valueOf(Property.getDataPropery().get("mysql.driver-class-name")));
            //配置数据库参数
            String url = String.valueOf(Property.getDataPropery().get("mysql.url"));
            String username = String.valueOf(Property.getDataPropery().get("mysql.username"));
            String password = String.valueOf(Property.getDataPropery().get("mysql.password"));
            con = DriverManager.getConnection(url, username, password);
            String sql =   "INSERT INTO adpm (`no`,`department`,`company`,`mode`,`workType`," +
                    "`developArea`,`personLevel`,`name`,`userName`,`workDate`," +
                    "`week`,`taskCategories`,`taskCategory`,`taskName`,`taskNumber`," +
                    "`taskDesc`,`actualHours`,`demandType`,`demandNumber`,`demandName`," +
                    "`applyName`,`applyID`) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst  = con.prepareStatement(sql);
            //发送并执行sql
//            String sql = "select * from klnf_dkjcsx";
            pst.setString( 1,adpm.getNo());
            pst.setString( 2,adpm.getDepartment());
            pst.setString( 3,adpm.getCompany());
            pst.setString( 4,adpm.getMode());
            pst.setString( 5,adpm.getWorkType());
            pst.setString( 6,adpm.getDevelopArea());
            pst.setString( 7,adpm.getPersonLevel());
            pst.setString( 8,adpm.getName());
            pst.setString( 9,adpm.getUserName());
            pst.setString( 10,adpm.getWorkDate());
            pst.setString( 11,adpm.getWeek());
            pst.setString( 12,adpm.getTaskCategories());
            pst.setString( 13,adpm.getTaskCategory());
            pst.setString( 14,adpm.getTaskName());
            pst.setString( 15,adpm.getTaskNumber());
            pst.setString( 16,adpm.getTaskDesc());
            pst.setString( 17,adpm.getActualHours());
            pst.setString( 18,adpm.getDemandType());
            pst.setString( 19,adpm.getDemandNumber());
            pst.setString( 20,adpm.getDemandName());
            pst.setString( 21,adpm.getApplyName());
            pst.setString( 22,adpm.getApplyID());
            int result = pst.executeUpdate();
            System.out.println(result > 0 ? "数据插入成功":"数据插入失败");

            //查询
//            resultSet = statement.executeQuery(sql);
//            while (resultSet.next()) {
//                String no = resultSet.getString("no");
//                String department = resultSet.getString("department");
//                System.out.println(name);
//                System.out.println(age);
//
//            }
        }catch(Exception e){
//            log.debug("hello");
//            log.error("数据库连接报错：{}",e.getMessage());

            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        //释放资源
        finally{
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (pst != null) {
                try {
                    pst.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


}
