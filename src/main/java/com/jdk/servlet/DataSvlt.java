package com.jdk.servlet;

import com.jdk.bean.JDBCHelper;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yang fyseus
 * @created 2022/4/6
 * @project MiniDashboard
 */

/**
 * 接受用户请求，查询数据库数据，并返回结果给访问界面
 */
public class DataSvlt extends HttpServlet {
    private static final long serialVersionUID = 1L;
    JDBCHelper jdbcHelper = JDBCHelper.getInstance();
    ResultSet rs = null;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DataSvlt() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest, HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int newsCount = getNewsCount();

        Map<String, Object> map = new HashMap<>();

        map.put("newssum", newsCount);
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = response.getWriter();
        pw.write(JSONObject.fromObject(map).toString());
        pw.flush();
        pw.close();

    }

    /**
     * 新闻曝光度
     */
    public int getNewsCount() {
        String sql = "select count(1) from newscount";
        try {
            ResultSet rs = getResultSet(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 返回ResultSet对象
     */

    public ResultSet getResultSet(String sql) throws Exception {
        jdbcHelper.executeQuery(sql, null, new JDBCHelper.QueryCallback() {
            @Override
            public void process(ResultSet rs1) throws Exception {
                rs = rs1;
            }
        });
        return rs;
    }

}
