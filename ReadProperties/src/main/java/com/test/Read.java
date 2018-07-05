package com.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;

public class Read {


    public static void main(String[] args) throws JSONException {

//        getConfKey();

//        System.out.println(getNextDay());

        System.out.println(new SimpleDateFormat("yyyyMMdd").format(System.currentTimeMillis()));
    }


    public Map getConfKey(String path) throws JSONException {

        String filepath = path;

        File file = new File(filepath);

        Map<String, String> configMap = new ReadProperties(filepath).getConfigMap();

//        ArrayList<String> keylist = new ArrayList<String>();

//        for(Map.Entry<String, String> entry:configMap.entrySet()) {
//
//            keylist.add(entry.getKey());
//        }

        return configMap;
    }

    /*
        获取系统日期前一天年月日
     */
    public String getNextDay() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,-1);
        return new SimpleDateFormat("yyyyMMdd").format(calendar.getTime());
    }
}
