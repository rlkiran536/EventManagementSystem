package controller.authentication;

import javax.servlet.annotation.*;
import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;  
import javax.servlet.http.*;

import dao.*;
import dataModels.*;
import utilities.*;

@WebServlet("/login")
public class LoginController extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		ExtraUtilities eu = new ExtraUtilities();
		String username = (String) request.getParameter("username");
		String password = (String) request.getParameter("password");

		if(eu.checkString(username) && eu.checkString(username)) {
			UserModel user = new UserDao().userLogin(username,password);
			if(user!=null) {
				session.setAttribute("user",user);			
				response.sendRedirect("/EventManagementSystem/user");
			} else {
				//user doesnot exists 
			}
		} else {
			//input fields empty
		}
	}
}
