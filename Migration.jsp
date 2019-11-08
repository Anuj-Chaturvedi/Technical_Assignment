<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="ISO-8859-1">
    <title>Migration Page</title>
  </head>
  <body>
    <h3>This will trigger Migration process for copying data from Datastore table to BigQuery table...</h3>


    Are you sure you want to start migration?<br /><br />
    <form action="MigrationServlet" method="post">
      <input type="submit" value="Yes" /> 
      <input type="button" value="No" onClick="openPage('index.html')" />
    </form>    
    <script type="text/javascript">
function openPage(pageURL) {
    window.location.href = pageURL;
}
    </script>
  </body>
</html>