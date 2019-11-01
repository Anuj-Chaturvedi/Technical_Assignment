<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="ISO-8859-1">
    <title>User Login</title>
  </head>
  
  <body>
    <h3>Login Form</h3>

    <form action="LoginServlet" method="post">  
      Email: <input type="text" name="email" required/><br/><br/>  
      Password: <input type="password" name="password" required/><br/><br/>  
      <input type="submit" value="login"/> 
    </form>  
      
    <h3>Go to Home page...</h3>
      <input type="button" value="Back" onClick="openPage('index.html')" />
      
    <script type="text/javascript">
function openPage(pageURL) {
    window.location.href = pageURL;
}
    </script>
  </body>
</html>