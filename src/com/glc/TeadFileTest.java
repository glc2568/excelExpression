package com.glc;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class TeadFileTest {

    private static Logger log = Logger.getLogger(TeadFileTest.class);
//    private static Logger log = Logger.getLogger("lavasoft");
    /**传入txt路径读取txt文件
         * @param txtPath
         * @return 返回读取到的内容
         */
        public static String readTxt(String txtPath) {
            File file = new File(txtPath);
            if(file.isFile() && file.exists()){
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    StringBuffer sb = new StringBuffer();
                    String text = null;
                    while((text = bufferedReader.readLine()) != null){
                        sb.append(text);
                    }
                    return sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        /**使用FileOutputStream来写入txt文件
         * @param txtPath txt文件路径
         * @param content 需要写入的文本
         */
        public static void writeTxt(String txtPath,String content){
            FileOutputStream fileOutputStream = null;
            File file = new File(txtPath);
            try {
                if(file.exists()){
                    //判断文件是否存在，如果不存在就新建一个txt
                    file.createNewFile();
                }
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    //验证方法：先写入文件后读取打印如下：
    public static void main(String[] args) {

        PropertyConfigurator.configure("config/log4j.properties");
int sheetCt=5;
        for (int i=0; i < sheetCt; i++){
            log.info("++"+i);
System.out.println("========"+i);
        }
        writeTxt("/Users/gaoleichao/sh/example.txt", "测试写入txt文件内容");
        String str = readTxt("/Users/gaoleichao/sh/example.sh");
        System.out.println(str);
        writeTxt("/Users/gaoleichao/sh/example.txt", str);
    }


}
