package com.excel.readandwrite;

import com.excel.pojo.Adpm;

import java.util.List;

public class AdpmList {

    public static Adpm getAdpm(List<String> row,String no){
        int j = 0;
        Adpm adpm = new Adpm();
        adpm.setDepartment(row.get(j).toString());
        adpm.setCompany(row.get(j + 1).toString());
        adpm.setMode(row.get(j + 2).toString());
        adpm.setWorkType(row.get(j + 3).toString());
        adpm.setDevelopArea(row.get(j + 4).toString());
        adpm.setPersonLevel(row.get(j + 5).toString());
        adpm.setName(row.get(j + 6).toString());
        adpm.setUserName(row.get(j + 7).toString());
        adpm.setWorkDate(row.get(j + 8).toString());
        adpm.setWeek(row.get(j + 9).toString());
        adpm.setTaskCategories(row.get(j + 10).toString());
        adpm.setTaskCategory(row.get(j + 11).toString());
        adpm.setTaskName(row.get(j + 12).toString());
        adpm.setTaskNumber(row.get(j + 13).toString());
        adpm.setTaskDesc(row.get(j + 14).toString());
        adpm.setActualHours(row.get(j + 15).toString());
        adpm.setDemandType(row.get(j + 16).toString());
        adpm.setDemandNumber(row.get(j + 17).toString());
        adpm.setDemandName(row.get(j + 18).toString());
        adpm.setApplyName(row.get(j + 19).toString());
        adpm.setApplyID(row.get(j + 20).toString());
        adpm.setNo(no);
        return adpm;
    }

}
