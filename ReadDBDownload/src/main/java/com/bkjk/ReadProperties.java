package com.bkjk;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.json.JSONObject;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadProperties {

    public static List<JSONObject> getProperties() {

        List<JSONObject> takejson = new ArrayList<>();

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Properties pp = new Properties();
            //加载配置文件
            pp.load(new FileInputStream("D:\\IDEA_CODE\\ReadDBDownload\\src\\druid.properties"));
            //使用连接池
            DataSource ds = DruidDataSourceFactory.createDataSource(pp);
            //创建连接
            con = ds.getConnection();
            String sql = "select * from gioproperties;";

            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
               String jsonstr = "{'id':'"+rs.getInt("id")+"','se':'"+rs.getString("se")+"','pub':'"+
                       rs.getString("pub")+"','projectid':'"+rs.getString("projectid")+"','ai':'"+
                       rs.getString("ai")+"'}";
               takejson.add(new JSONObject(jsonstr));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.relase(con,ps,rs);
        }

        return takejson;
    }

    public static void main(String[] args) {
        getProperties();
    }
}
