<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login Successful</title>
</head>
<body>
<h3>Congratulations... Your login was successful !</h3>

    <h3>Go to Login page...</h3>
      <input type="button" value="Login Again" onClick="openPage('login.jsp')" />

    <h3>Go to Home page...</h3>
      <input type="button" value="Home" onClick="openPage('index.html')" />
    <script type="text/javascript">
function openPage(pageURL) {
    window.location.href = pageURL;
}
    </script>
</body>
</html>