package controller.user;


import java.io.*;
import java.util.ArrayList;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.servlet.annotation.*;
import dataModels.*;
import dao.*;
import utilities.*;

@WebServlet("/adduser")
public class AddUser extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session =  request.getSession();
		ExtraUtilities eu = new ExtraUtilities();
		UserModel user = (UserModel) session.getAttribute("user");
		if(user.getRole().equals("ADMINISTRATOR")) {
			UserModel newUser = new UserModel();
			String username = request.getParameter("username");
			String role = request.getParameter("role");
			String password = request.getParameter("password");
			String rePassword = request.getParameter("rePassword");
			if(eu.checkString(username) && eu.checkString(password) && eu.checkString(rePassword) && eu.checkString(role)) {
				if(password.equals(rePassword)) {
					if(!role.equals("ADMINISTRATOR")){
						newUser.setName(username);
						newUser.setRole(role);
						newUser.setPassword(password);
						newUser = new UserDao().addUser(newUser);
						if(newUser!=null) {
							response.sendRedirect("/EventManagementSystem/user");
							return;				
						}
					} else {
						//unable to add admin
					}
				} else {
					//passwords didn't match
				}
			} else {
				//invalid fields
			}			
		} else {
			//not enough privilages to add user
		}
		//error popup unable to add user
		response.sendRedirect("/EventManagementSystem/user");
	}
}
