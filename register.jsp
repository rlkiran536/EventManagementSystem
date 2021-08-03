<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Register</title>
<link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
	<div class="formContainer">
		<h1>Register</h1>
		<form action="/EventManagementSystem/register" method="post">
			Enter UserName <br><br><input name="username" placeholder="Enter UserName"> <br> <br>
			Enter Password <br><br><input name="password"  type="password" placeholder="Enter Password"> <br> <br>
			Re-Enter Password <br><br> <input name="rePassword"  type="password" placeholder="Re-Enter Password"> <br> <br>
			Select Role <br> <br>
			<input type="radio" name="role" value="STUDENT"/>STUDENT
			<input type="radio" name="role" VALUE="FACULTY"/>FACULTY
			<br><br>
			<button type="submit">register</button>
		</form>
		<h3>Already a user?<a href="/EventManagementSystem/index.jsp" >Login</a></h3>		
	</div>
</body>
</html>