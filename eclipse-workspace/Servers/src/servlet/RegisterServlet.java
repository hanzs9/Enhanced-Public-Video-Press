package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

@WebServlet("/RegisterServlet")  
public class RegisterServlet extends HttpServlet {  
    private static final long serialVersionUID = 1L;  
         
    /** 
     * @see HttpServlet#HttpServlet() 
     */  
    public RegisterServlet() {  
        super();  
    }  
  
    /** 
     *  
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        String location = request.getParameter("location"); // 从 request 中获取名为 account 的参数的值  
        String tag = request.getParameter("tag"); // 从 request 中获取名为 password 的参数的值  
        System.out.println("account:" + location + "\npassword:" + tag); // 打印出来看一看  
  
        String resCode = "";  
        String resMsg = "";  
        String userId = "";  
          
          
        /* 这里我们做一个最简单的注册逻辑，当然，你的实际业务可以相当复杂 */  
        try {  
            Connection connect = DBUtil.getConnect();  
            Statement statement = (Statement) connect.createStatement(); // Statement可以理解为数据库操作实例，对数据库的所有操作都通过它来实现  
            ResultSet result;  
              
            String sqlQuery = "select * from " + DBUtil.TABLE_PASSWORD + " where location='" + location + "'";  
              
            // 查询类操作返回一个ResultSet集合，没有查到结果时ResultSet的长度为0  
            result = statement.executeQuery(sqlQuery); // 先查询同样的账号（比如手机号）是否存在  
            if(result.next()){ // 已存在  
                resCode = "201";  
                resMsg = "same situation";  
                userId = "";  
            } else { // 不存在  
                String sqlInsertPass = "insert into " + DBUtil.TABLE_PASSWORD + "(location,tag) values('"+location+"','"+tag+"')";  
                // 更新类操作返回一个int类型的值，代表该操作影响到的行数  
                int row1 = statement.executeUpdate(sqlInsertPass); // 插入帐号密码  
                if(row1 == 1){  
                    String sqlQueryId = "select userId from " + DBUtil.TABLE_PASSWORD + " where location='" + location + "'";  
                    ResultSet result2 = statement.executeQuery(sqlQueryId); // 查询新增记录的userId  
                    if(result2.next()){  
                        userId = result2.getInt("userId") + "";  
                    }  
                    String sqlInsertInfo = "insert into " + DBUtil.TABLE_USERINFO + "(userId) values('" + userId + "')";  
                    int row2 = statement.executeUpdate(sqlInsertInfo); // 在用户信息表中插入刚注册的Id  
                    if(row2 == 1){ // 两个表中都插入成功，从业务角度认定为注册成功  
                        resCode = "100";  
                        resMsg = "uploaded";  
                    }  
                } else {  
                    resCode = "202";  
                    resMsg = "error";  
                    userId = "";  
                }  
            }  
              
        } catch (SQLException e) {  
            e.printStackTrace();  
        }  
          
        HashMap<String, String> map = new HashMap<>();  
        map.put("resCode", resCode);  
        map.put("resMsg", resMsg);  
        map.put("userId", userId);  
          
        response.setContentType("text/html;charset=utf-8"); // 设置响应报文的编码格式  
        PrintWriter pw = response.getWriter(); // 获取 response 的输出流  
        pw.println(map.toString()); // 通过输出流把业务逻辑的结果输出  
        pw.flush();  
    }  
  
    /** 
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response) 
     */  
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  
        doGet(request, response);  
    }  
  
}