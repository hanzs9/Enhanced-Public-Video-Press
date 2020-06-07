package servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;




//import org.apache.tomcat.jni.File;

@WebServlet("/UploadHandleServlet")
public class UploadHandleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// �ϴ��ļ��洢Ŀ¼
    private static final String UPLOAD_DIRECTORY = "upload";
 // �ϴ�����
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException{
		
		
		// ����Ƿ�Ϊ��ý���ϴ�
        if (!ServletFileUpload.isMultipartContent(request)) {
            // ���������ֹͣ
            PrintWriter writer = response.getWriter();
            writer.println("Error: ��������� enctype=multipart/form-data");
            writer.flush();
            return;
        }
        
        // �����ϴ�����
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // �����ڴ��ٽ�ֵ - �����󽫲�����ʱ�ļ����洢����ʱĿ¼��
        factory.setSizeThreshold(MEMORY_THRESHOLD);
        // ������ʱ�洢Ŀ¼
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        
        ServletFileUpload upload = new ServletFileUpload(factory);
        // ��������ļ��ϴ�ֵ
        upload.setFileSizeMax(MAX_FILE_SIZE); 
        // �����������ֵ (�����ļ��ͱ�����)
        upload.setSizeMax(MAX_REQUEST_SIZE);
        // ���Ĵ���
        upload.setHeaderEncoding("UTF-8"); 
        
        // ������ʱ·�����洢�ϴ����ļ�
        // ���·����Ե�ǰӦ�õ�Ŀ¼
        String uploadPath = request.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIRECTORY;

        // ���Ŀ¼�������򴴽�
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
 
        try {
            // ���������������ȡ�ļ�����
            @SuppressWarnings("unchecked")
            List<FileItem> formItems = upload.parseRequest(request);
 
            if (formItems != null && formItems.size() > 0) {
                // ����������
                for (FileItem item : formItems) {
                    // �����ڱ��е��ֶ�
                    if (!item.isFormField()) {
                        String fileName = new File(item.getName()).getName();
                        String filePath = uploadPath + File.separator + fileName;
                        File storeFile = new File(filePath);
                        // �ڿ���̨����ļ����ϴ�·��
                        System.out.println(filePath);
                        // �����ļ���Ӳ��
                        item.write(storeFile);
                        request.setAttribute("message",
                            "�ļ��ϴ��ɹ�!");
                    }
                }
            }
        } catch (Exception ex) {
            request.setAttribute("message",
                    "������Ϣ: " + ex.getMessage());
        }
        // ��ת�� message.jsp
        request.getServletContext().getRequestDispatcher("/message.jsp").forward(
                request, response);
        
        
        
        
        
//		DiskFileItemFactory f = new DiskFileItemFactory();
//		//route
//		f.setRepository(new File("d:/a"));
//		//size cache 8k
//		f.setSizeThreshold(1024*8);
//		
//		ServletFileUpload upload = new ServletFileUpload(f);
//		upload.setHeaderEncoding("UTF-8");
//		upload.setFileSizeMax(1024 * 1024 * 5);
//		upload.setSizeMax(1024 * 1024 * 10);
//		String path = this.getServletContext().getRealPath("/imgs");
//		
//		try {
//			List<FileItem> list = upload.parseRequest(request);// ����
//				for (FileItem ff : list) {
//					if (ff.isFormField()) {
//						String ds = ff.getString("UTF-8");//��������
//						//System.err.println("˵����:" + ds);
//					} 
//					else {
//						String ss = ff.getName();
//						ss = ss.substring(ss.lastIndexOf("\\") + 1);//�����ļ���
//						FileUtils.copyInputStreamToFile( //ֱ��ʹ��commons.io.FileUtils
//						ff.getInputStream(),
//						new File(path + "/" + ss));
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
        
	}	
}
	
	
	
	
	
	
//	public void doGet(HttpServletRequest request, HttpServletResponse response)
//			 								throws ServletException,IOException{
//		
//		//upload deriction
//		String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
//		String tempPath = this.getServletContext().getRealPath("/WEB-INF/temp");
//		File tmpFile = new File(tempPath);
//		
//		//crate new directory
//		if(!tmpFile.exists()) {
//			tmpFile.mkdir();
//		}
//		
//		String message = "";
//		try {
//			//type factory
//			DiskFileItemFactory factory = new DiskFileItemFactory();
//			//temp buffer
//			factory.setSizeThreshold(1024*100);
//			factory.setRepository(tmpFile);
//			
//			//
//			ServletFileUpload upload = new ServletFileUpload(factory);
//			upload.setProgressListener(new ProgressListener() {
//				public void update(long pBytesRead, long pContentLength, int arg2) {
//					System.out.println("file size: "+pContentLength + "; Now Process: " + pBytesRead);
//				}
//			});
//			
//			//utf-8 encode
//			upload.setHeaderEncoding("UTF-8");
//			
//			//check the table
//			if(!ServletFileUpload.isMultipartContent(request)) {
//				return;
//			}
//			
//			//max size;
//			upload.setFileSizeMax(1024*1024);
//			upload.setSizeMax(1024*1024*10);
//			List<FileItem> list = upload.parseRequest(request);
//			for (FileItem item : list) {
//				if(item.isFormField()) {
//					String name = item.getFieldName();
//					String value = item.getString("UTF-8");
//					System.out.println(name + "=" + value);
//				}
//				else {
//					String filename = item.getName();
//					System.out.println(filename);
//					if(filename==null || filename.trim().equals("")){
//						continue;
//					}
//					filename = filename.substring(filename.lastIndexOf("\\")+1);
//					String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
//					System.out.println("extension name��"+fileExtName);
//					InputStream in = item.getInputStream();
//					//String saveFilename = makeFileName(filename);
//					//String realSavePath = makePath(saveFilename, savePath);
//				}
//			}
//			
//			
//		
//		}catch{
//			
//		}
		
//	}


