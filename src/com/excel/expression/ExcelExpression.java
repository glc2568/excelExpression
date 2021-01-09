package com.excel.expression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExcelExpression extends JFrame  implements ActionListener {
    JTextField type;
    JTextField beginDate;// 计划开始日期
    JTextField day;// 往前（负值）/后（正值）多少天日期
    JTextField systemDate;// 系统日期
    JTextField endDate;// 计划完成日期
    JTextField acTualFinish;// 实际完成百分比
    JTextField holidays;// 为空时默认（不剔除节假日，周六周日）
    JComboBox comboBox;
    //窗口：
    JFrame window;
    Button btn1,btn2;//按钮
    JTextArea	tarea;//文本框
    //初始化
    public ExcelExpression(){
        window=new JFrame("ExcelExpression");
        window.setLayout(null);
        window.setSize(600, 600);//设置大小
        window.setLocationRelativeTo(null);//设置居中
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置可关闭
        window.setLayout(null);//设置绝对布局（窗口里面的内容不会随着窗口的改变而改变）


        JLabel beginDateIn =new JLabel("计划开始日期：");
        beginDateIn.setBounds(20,15,110,50);
        window.add(beginDateIn);
        beginDate=new JTextField();
        beginDate.setBounds(115, 25, 100, 30);
        window.add(beginDate);

        JLabel dayIn =new JLabel("天数：");
        dayIn.setBounds(20,45,110,50);
        window.add(dayIn);
        day=new JTextField();
        day.setBounds(115, 55, 100, 30);
        window.add(day);

        JLabel systemDateIn =new JLabel("系统日期：");
        systemDateIn.setBounds(20,75,110,50);
        window.add(systemDateIn);
        systemDate=new JTextField();
        systemDate.setBounds(115, 85, 100, 30);
        window.add(systemDate);

        JLabel endDateIn =new JLabel("计划完成日期：");
        endDateIn.setBounds(20,105,110,50);
        window.add(endDateIn);
        endDate=new JTextField();
        endDate.setBounds(115, 115, 100, 30);
        window.add(endDate);

        JLabel acTualFinishIn =new JLabel("实际完成百分比：");
        acTualFinishIn.setBounds(20,135,110,50);
        window.add(acTualFinishIn);
        acTualFinish=new JTextField();
        acTualFinish.setBounds(115, 145, 100, 30);
        window.add(acTualFinish);

        JLabel holidaysIn =new JLabel("节假日：");
        holidaysIn.setBounds(20,165,110,50);
        window.add(holidaysIn);
        holidays=new JTextField();
        holidays.setBounds(115, 175, 100, 30);
        window.add(holidays);

//        JLabel typeIn =new JLabel("选择类型：");
//        typeIn.setBounds(350,15,100,50);
//        window.add(typeIn);
//        type=new JTextField();
//        type.setBounds(420, 25, 100, 30);
//        window.add(type);


        JLabel label=new JLabel("选择类型：");
        label.setBounds(350,15,110,50);
        window.add(label);
        comboBox=new JComboBox();
        comboBox.addItem(1);
        comboBox.addItem(2);
        comboBox.addItem(3);
        comboBox.addItem(4);
        comboBox.addItem(5);
        comboBox.setBounds(410, 25, 65, 30);
        window.add(comboBox);
        comboBox.addActionListener(this);


        JLabel desc1 =new JLabel("1.返回开始日期N天后的结束日期");
        desc1.setBounds(350,35,300,100);
        window.add(desc1);
        JLabel desc2 =new JLabel("2.返回开始日期到结束日期的天数");
        desc2.setBounds(350,55,300,100);
        window.add(desc2);
        JLabel desc3 =new JLabel("3.返回当前系统进行了多少天 ");
        desc3.setBounds(350,75,300,100);
        window.add(desc3);
        JLabel desc4 =new JLabel("4.返回当前系统的进度百分比 ");
        desc4.setBounds(350,95,300,100);
        window.add(desc4);
        JLabel desc5 =new JLabel("5.返回系统正常/延期天数/进行中/完成 ");
        desc5.setBounds(350,115,300,100);
        window.add(desc5);

        btn1 = new Button("转    换");//创建按钮
        btn1.setBounds(450, 250,100,40);
        window.add(btn1);

//        btn2 = new Button("退    出");//创建按钮
//        btn2.setBounds(450, 250,100,40);
//        window.add(btn2);
//        btn2.addActionListener(this);//设置按钮点击监听事件

        tarea = new JTextArea("");//创建文本框
        tarea.setBounds(20, 320, 560, 240);//设置文本框位置
        tarea.setLineWrap(true);
        window.add(tarea);
        btn1.addActionListener(this);//设置按钮点击监听事件
        window.setResizable(true);//设置窗口不可拉伸改变大小
        window.setVisible(true);//设置面板可见

    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn1) {//按钮1事件处理
            String str;
            try {
                 int index = comboBox.getSelectedIndex();
                 int []array = {1,2,3,4,5};
                ExcelCalc ec = new ExcelCalc();
                String result  = "="+ec.rateStatusExpression(array[index], beginDate.getText(), day.getText(), systemDate.getText(), endDate.getText(), acTualFinish.getText(), holidays.getText());
                tarea.setText(result);
            } catch (Exception e1) {//如果有错误，这里进行处理
                e1.printStackTrace();
                tarea.setText(e1.getMessage());;//打印错误信息
            }
        }

        if (e.getSource() == btn2) {//按钮3事件处理
            System.exit(0);//关闭程序
        }
    }


}

