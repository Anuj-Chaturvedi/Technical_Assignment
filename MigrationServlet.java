package Servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Backend.LoadBigquery;

@SuppressWarnings("serial")
@WebServlet(
    name = "MigrationServlet",
    urlPatterns = {"/MigrationServlet"}
)

public class MigrationServlet extends HttpServlet {

  	@Override
  	public void doPost(HttpServletRequest request, HttpServletResponse response) 
      		throws IOException 
	{
	  	String message = LoadBigquery.start();
	  	response.getWriter().write(message);
  	}

  	@Override
  	public void doGet(HttpServletRequest request, HttpServletResponse response) 
      		throws IOException 
	{
	  	response.sendRedirect("/Migration.jsp");
  	}
}