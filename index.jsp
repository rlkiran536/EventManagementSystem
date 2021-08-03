<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>

	<div class="formContainer">
		<h1>Login</h1>
		<form action="/EventManagementSystem/login" method="POST">
			Enter UserName<br><br><input name="username" placeholder="Enter UserName"> <br><br>
			Enter Password<br><br><input name="password" type="password" placeholder="Enter Password"> <br><br>
			<button type="submit">login</button>
		</form>
		<h3>New user?<a href="/EventManagementSystem/register.jsp" >Register</a></h3>
		
	</div>
</body>
</html>