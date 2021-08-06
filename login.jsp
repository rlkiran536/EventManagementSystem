<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
<!DOCTYPE html>
<html>
<head>
	<title>Login</title>
	<style type="text/css">
		body {
			font-family: monospace;
		}
		/* AUTH FORM */
		.formContainer {
			text-align: center;
	  		min-width: 50%;
    		margin: 2% 25%;
		}
		
		.formContainer form {
			padding: 15% 10%;
  			border: 1px solid beige;
  			font-size: 1.1rem;
  			font-weight: bold;
		}

		.formContainer input {
			padding: 0.6rem;
    		border: 1px solid #252514;
    		border-radius: 10px;
		}

		.formContainer button {
	  		color: white;
    		background-color: #480505d4;
    		padding: 0.7rem 2rem;
    		border: 1px solid beige;
    		border-radius: 10px;
    		cursor: pointer;
		}
	</style>
</head>
<body>
	<%
		response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
		String username = (String) session.getAttribute("username");
		if(username!=null) response.sendRedirect("/");
	%>
	<div class="formContainer">
		<h1>Login</h1>
		<form action="j_security_check" method="POST">
			Enter UserName<br><br><input name="j_username" placeholder="Enter UserName"> <br><br>
			Enter Password<br><br><input name="j_password" type="password" placeholder="Enter Password"> <br><br>
			<button type="submit">login</button>
		</form>
		<h3>New user?<a href="/EventManagementSystem/register.jsp" >Register</a></h3>
		
	</div>
</body>
</html>