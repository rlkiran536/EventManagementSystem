package controller.authentication;

import javax.servlet.annotation.*;
import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;  
import javax.servlet.http.*;

import dao.*;
import dataModels.*;
import utilities.*;

@WebServlet("/register")
public class RegistrationController extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Boolean status = false;
		PrintWriter out = response.getWriter();
		ExtraUtilities eu = new ExtraUtilities();
		HttpSession session = request.getSession();
		UserModel user = new UserModel();
		String username = (String) request.getParameter("username");
		String password = (String) request.getParameter("password");
		String rePassword = (String) request.getParameter("rePassword");
		String role = (String) request.getParameter("role");
		if(eu.checkString(username) && eu.checkString(password) && 
			eu.checkString(rePassword) && eu.checkString(role) ) {
			if(password.equals(rePassword)) {
				user.setName(username);
				user.setPassword(password);
				user.setRole(role);
				user = new UserDao().addUser(user);
				if(user!=null) {
					session.setAttribute("user",user);
					status = true;					
				} else {
					//failed to add user
				}
			} else {
				//passwords didn't matched
			}
		} else {
			//empty fields
		}
		if(status) response.sendRedirect("/EventManagementSystem/user");
		else response.sendRedirect("/EventManagementSystem/register.jsp");
	}
}