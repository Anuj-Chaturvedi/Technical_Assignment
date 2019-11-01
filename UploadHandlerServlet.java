package assignment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.*;

import java.io.*;
import java.util.*;

import jxl.*;
import jxl.read.biff.BiffException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
@WebServlet(
    name = "UploadHandlerServlet",
    urlPatterns = {"/UploadHandlerServlet"}
)

public class UploadHandlerServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) 
      throws IOException, ServletException {
	
	  FileItemFactory factory = new DiskFileItemFactory();
	  ServletFileUpload upload = new ServletFileUpload(factory);
	  
	  List<FileItem> items;
	  boolean status = true;
	  
	  try {
		    String saveFile=System.getProperty("java.io.tmpdir");
		    items = upload.parseRequest(request);
		    Iterator<FileItem> itr = items.iterator();
		    FileItem item = (FileItem)itr.next();
		    String itemname = item.getName();
		    String filename = FilenameUtils.getName(itemname);
		    File f = new File(saveFile+"/"+filename);
		    item.write(f);
		    status=readExcel(f);
		    f.delete();
	    } 
	    catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		    status = false;
	    }
        if(status)        	
        	response.sendRedirect("/login.jsp");
        else
        	response.getWriter().print("Some Exception occurred...");
    }

    public boolean readExcel(File f) {

    	try {
    		
    		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Workbook workBook = Workbook.getWorkbook(f);
			String [] sheetNames = workBook.getSheetNames();
			Sheet sheet=null;
			
			for (int sheetNumber =0; sheetNumber < sheetNames.length; sheetNumber++) {
				
				int key=1;
				sheet=workBook.getSheet(sheetNames[sheetNumber]);
				
				for (int rows=1;rows < sheet.getRows();rows++) {
					
					if(!sheet.getCell(0, rows).getContents().equals("")) {
					    Entity e = new Entity("User",key++);
					    
				        for (int columns=0;columns < sheet.getColumns();columns++)
					        e.setProperty(sheet.getCell(columns, 0).getContents(),
							            sheet.getCell(columns, rows).getContents());
				        ds.put(e);
				    }
		        }
			}
		} 
    	catch (BiffException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    	throws IOException {
    	    response.sendRedirect("/index.html");
    }
}