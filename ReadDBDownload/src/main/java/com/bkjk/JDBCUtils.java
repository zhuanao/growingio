package com.bkjk;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


/**
 * 有预处理的JDBC工具类
 *
 * @author taiyu.lei
 *
 */
public class JDBCUtils {

    private static final String CLASSFORNAME;
    private static final String SQLURL;
    private static final String USERNAME;
    private static final String PASSWORD;
    // 赋值工作只进行一次，所以给常量的赋值写在静态代码块中
    static {
        Properties pp = new Properties();
        try {
            // 加载配置文件
            pp.load(new FileInputStream("D:\\IDEA_CODE\\ReadDBDownload\\src\\jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        CLASSFORNAME = pp.getProperty("CLASSFORNAME");
        SQLURL = pp.getProperty("SQLURL");
        USERNAME = pp.getProperty("USERNAME");
        PASSWORD = pp.getProperty("PASSWORD");
        /**
         * 注册驱动，注册一次就行，所以写到静态代码块里
         */
        try {
            Class.forName(CLASSFORNAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建链接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(SQLURL, USERNAME, PASSWORD);
        return con;
    }

    /**
     * 释放资源
     *
     * @param con
     *            创建链接的对象
     * @param ps
     *            执行SQL语句的对象
     * @param rs
     *            结果集对象
     */
    public static void relase(Connection con, PreparedStatement ps, ResultSet rs) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            con = null;
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ps = null;
        }
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
    }

    /**
     * 释放资源
     *
     * @param con
     *            创建链接的对象
     * @param ps
     *            执行SQL语句的对象
     */
    public static void relase(Connection con, PreparedStatement ps) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            con = null;
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ps = null;
        }
    }
}
