package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PostTest
 */
@WebServlet("/PostTest")
public class PostTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	 protected void doPost(HttpServletRequest request, HttpServletResponse response)  
	            throws ServletException, IOException {  
	        String location;  
	        String tag;  
	          
	        // 获取POST请求参数  
	        // 
	        location = request.getParameter("location");  
	        tag = request.getParameter("tag");   
	        System.out.println("account: " + location);  
	        System.out.println("tag: " + tag);  
	          
	        // 响应  
	        response.getWriter().append("Location: ").append(location).append("\nTag：").append(tag);  
	    }  
}
