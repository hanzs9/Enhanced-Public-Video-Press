package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
 

/**
 * Servlet implementation class UploadServlet
 */
//@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
     
    // route
    private static final String UPLOAD_DIRECTORY = "upload";
 
    // setting
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    
    String filePath;
    String uploadPath;
 
    /**
     * Upload
     */
    protected void doPost(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
        // media checking
        if (!ServletFileUpload.isMultipartContent(request)) {
        	System.out.println("it is not file");
            PrintWriter writer = response.getWriter();
            writer.println("Error:  enctype=multipart/form-data");
            writer.flush();
            return;
        }
        System.out.println("it is a file");
        // upload setting
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // menmory limitation
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // temp route
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
         
        // file size limitation
        upload.setFileSizeMax(MAX_FILE_SIZE);
         
        // request limitation
        upload.setSizeMax(MAX_REQUEST_SIZE);

        // chinese processing
        upload.setHeaderEncoding("UTF-8"); 

        // temp route
        String uploadPath = request.getServletContext().getRealPath("./") + UPLOAD_DIRECTORY;//+ File.separator 
       
         
        // create
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
 
        try {
            
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
 
            if (formItems != null && formItems.size() > 0) {
                // form data
                for (FileItem item : formItems) {
                    
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // output upload route
                        System.out.println(filePath);
                        // save file
                        item.write(storeFile);
                        request.setAttribute("message",
                            "successful");
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message",
                    "error1: " + ex.getMessage());
        }
        // turn to message.jsp
        request.getServletContext().getRequestDispatcher("/message.jsp").forward(
                request, response);
    }
    
 @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	super.doGet(req, resp);
	req.setCharacterEncoding("UTF-8");
	resp.setCharacterEncoding("UTF-8");
	//向服务器发送请求获取到参数
	String time=req.getParameter("time");
	String location=req.getParameter("location");
	String videoName = req.getParameter("videoName");
	System.out.println(time+"..."+location);
	
	
	String driver = "com.mysql.jdbc.Driver";  
    // ?useSSL=false 不验证身份
    String url = "jdbc:mysql://10.8.66.93:3306/databasetest?allowPublicKeyRetrieval=true";
    // MySQL配置时的用户名  
    String user = "root";   
    // MySQL配置时的密码  
    String password = "abc123456.";
    try {   
     // 加载驱动程序  
     Class.forName("com.mysql.cj.jdbc.Driver");  
     // 连续数据库  
     Connection conn = DriverManager.getConnection(url, user, password);  
     if(!conn.isClosed())   
      System.out.println("Succeeded connecting to the Database!");  
     if(time!=null) {
     // statement用来执行SQL语句  
     Statement statement = conn.createStatement();  
     // 要执行的SQL语句  
     String sql = "INSERT INTO location VALUES ('"+time+"', '"+location+"', '"+"/Users/sonny/Documents/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp1/wtpwebapps/ServletTest/upload/video"+videoName+"')";  
     // 结果集  
     System.out.println("aaaaaa");
     statement.executeUpdate(sql);

     System.out.println("—————–");  
     System.out.println("servlet have been executed:");  
     System.out.println("—————–"); 
     }
    } catch(Exception e) {  
    	  
        System.out.println("Sorry,can't find the Driver!");   
         e.printStackTrace();  
        }   
    }
}