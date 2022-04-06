package com.jdk.bean;

import java.sql.*;
import java.util.LinkedList;

/**
 * @author Yang fyseus
 * @created 2022/4/6
 * @project MiniDashboard
 */
public class JDBCHelper {

    // 加载驱动
    static {
        try {
            ConfigurationManager.getProperty(Constants.JDBC_DRIVER);
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 实现JDBCHelper的单例化
    private static JDBCHelper instance = null;
    public static JDBCHelper getInstance() {
        if (instance == null) {
            synchronized (JDBCHelper.class) {
                if (instance == null) {
                    instance = new JDBCHelper();
                }
            }
        }
        return instance;
    }

    // 创建数据库连接池
    private LinkedList<Connection> datasource = new LinkedList<>();
    private JDBCHelper() {
        int size = ConfigurationManager.getInteger(Constants.JDBC_DATASOURCE_SIZE);
        for (int i = 0; i < size; i++) {
            String url = ConfigurationManager.getProperty(Constants.JDBC_URL);
            String user = ConfigurationManager.getProperty(Constants.JDBC_USER);
            String password = ConfigurationManager.getProperty(Constants.JDBC_PASSWORD);
            try {
                Connection con = DriverManager.getConnection(url, user, password);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    // 提供获取数据库连接
    public synchronized Connection getConnection() {
        while (datasource.size() == 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return datasource.poll();
    }

    // 执行查询SQL语句
    public synchronized void executeQuery(String sql, Object[] params, QueryCallback callback) throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    pstmt.setObject(i + 1, params[i]);
                }
            }
            rs = pstmt.executeQuery();
            callback.process(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                datasource.push(conn);
            }
        }
    }

    public static interface QueryCallback {
        void process(ResultSet rs) throws Exception;
    }
}
