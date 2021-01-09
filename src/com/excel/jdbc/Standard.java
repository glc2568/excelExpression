package com.excel.jdbc;


import com.excel.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Standard {
    private static final Logger log = LoggerFactory.getLogger(Standard.class);
    public static void main(String[] args) throws IOException {
        String sql = "select * from klnf_dkjcsx";
        getSqlcon(sql);
    }

    public static void getSqlcon(String sql){
        //获取连接
        Connection con = null;
        //创建statement对象
        Statement statement = null;
        ResultSet resultSet = null;
        //注册mysql驱动
        try{
            Class.forName(String.valueOf(Property.getDataPropery().get("mysql.driver-class-name")));
            //配置数据库参数
            String url = String.valueOf(Property.getDataPropery().get("mysql.url"));
            String username = String.valueOf(Property.getDataPropery().get("mysql.username"));
            String password = String.valueOf(Property.getDataPropery().get("mysql.password"));
            con = DriverManager.getConnection(url, username, password);
            statement = con.createStatement();
            //发送并执行sql
//            String sql = "select * from klnf_dkjcsx";
//            statement.executeUpdate();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String name = resultSet.getString("chanpdma");
                String age = resultSet.getString("chanpmch");
                System.out.println(name);
                System.out.println(age);

            }
        }catch(Exception e){
            log.debug("hello");
            log.error("数据库连接报错：{}",e.getMessage());
            e.getStackTrace();
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
            if (statement != null) {
                try {
                    statement.close();
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
