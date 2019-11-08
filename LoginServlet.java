package Servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

@SuppressWarnings("serial")
@WebServlet(
	name = "LoginServlet",
	urlPatterns = {"/LoginServlet"}
)

public class LoginServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
      		throws IOException, ServletException 
	{
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		Filter f = new Query.FilterPredicate("Email", FilterOperator.EQUAL, request.getParameter("email"));
		Query q = new Query("User").setFilter(f);
		PreparedQuery pq = ds.prepare(q);
		Entity result = pq.asSingleEntity();
		if(result==null)
			response.getWriter().print("Email "+request.getParameter("email")+" is Not Registered...!");
		else {
		    if(result.getProperty("Password").equals(request.getParameter("password")))
		    	response.sendRedirect("/LoginSuccess.jsp");
		    else
		    	response.getWriter().print("Password was Incorrect...!");
		}
	}
	
	@Override
    	public void doGet(HttpServletRequest request, HttpServletResponse response)
      		throws IOException 
	{
        	response.sendRedirect("/login.jsp");
    	}
}
