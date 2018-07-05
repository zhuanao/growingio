package com.bkjk;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DownMain {

    public static void main(String[] args) throws JSONException, IOException {

        DownloadGio gio;
        //获取配置json串
        List<JSONObject> jsonObjects = ReadProperties.getProperties();

        //day 或者 hour
        for (String export_type : getExport_type() ) {

            //导出day数据
            if (export_type.equals("day")) {
                //遍历配置信息
                for (JSONObject job : jsonObjects) {
                    //visit page action ....
                    for (String data_type : getData_type()) {
                        gio = new DownloadGio(job.getString("id"),job.getString("se"),job.getString("pub"),job.getString("projectid"),job.getString("ai"),export_type,data_type,args[0]+"",args[1]+"");
                        gio.downLoad();
                    }
                }
                //导出hour数据
            } else if (export_type.equals("hour")){
                //遍历配置信息
                for (JSONObject job : jsonObjects) {
                    //visit page action ....
                    for (String data_type : getData_type()) {
                        //获取各个hour
                        for (String hour : gethour()) {
                            gio = new DownloadGio(job.getString("id"),job.getString("se"),job.getString("pub"),job.getString("projectid"),job.getString("ai"),export_type,data_type,args[0]+"",args[1]+hour+"");
                            gio.downLoad();
                        }
                    }
                }
            }
        }

    }

    //获取导出类型
    public static List<String> getExport_type() {

        List<String> export_type = new ArrayList<>();
        export_type.add("day");
        export_type.add("hour");
        return export_type;
    }

    //获取数据类型
    public static List<String> getData_type() {

        List<String> data_type = new ArrayList<>();
        data_type.add("visit");
        data_type.add("page");
        data_type.add("action");
        data_type.add("action_tag");
        data_type.add("custom_event");
        data_type.add("ads_track_activation");
        data_type.add("ads_track_click");
        return data_type;
    }

    //获取小时
    public static List<String> gethour() {

        List<String> hour = new ArrayList<>();

        for (int i = 1; i <= 24 ; i++) {
            if (i <= 9) {
                hour.add("0"+i);
            } else {
                hour.add(i+"");
            }
        }
        return hour;
    }
}

