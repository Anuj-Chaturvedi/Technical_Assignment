package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import Backend.UploadToDatastore;

@SuppressWarnings("serial")
@WebServlet(
    	name = "UploadHandlerServlet",
    	urlPatterns = {"/UploadHandlerServlet"}
)

public class UploadHandlerServlet extends HttpServlet {

    	@Override
    	public void doPost(HttpServletRequest request, HttpServletResponse response) 
      		throws IOException, ServletException 
	{
    		boolean status = true;
    		FileItemFactory factory = new DiskFileItemFactory();
    		ServletFileUpload upload = new ServletFileUpload(factory);
    		try {
			List<FileItem> items = upload.parseRequest(request);
			status = UploadToDatastore.start(items);
		} 
		catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = false;
		}
        	if(status)        	
        		response.sendRedirect("/login.jsp");
        	else
        		response.getWriter().print("Some Exception occurred...");
    	}

    	@Override
    	public void doGet(HttpServletRequest request, HttpServletResponse response)
    		throws IOException 
	{
    	    	response.sendRedirect("/index.html");
    	}
}
