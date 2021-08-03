package controller.user;

import java.io.*;
import java.util.ArrayList;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.servlet.annotation.*;
import dataModels.*;
import dao.*;
import utilities.*;

@WebServlet("/updateuser")
public class UpdateUser extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session =  request.getSession();
		ExtraUtilities eu = new ExtraUtilities();
		UserModel user = (UserModel) session.getAttribute("user");
		String userId = request.getParameter("userId");
		String username = request.getParameter("username");
		String role = request.getParameter("role");
		String password = request.getParameter("password");
		String rePassword = request.getParameter("rePassword");
		int userIdNumber = 0;
		if(!checkInfo(userId,password,rePassword)) {
			response.sendRedirect("/EventManagementSystem/user");
			return;
		}
		userIdNumber = Integer.parseInt(userId);				
		if(user.getRole().equals("ADMINISTRATOR")) {
			ArrayList<UserModel> allUsers = (ArrayList<UserModel>) session.getAttribute("allUsers");
			if(allUsers!=null && allUsers.size()>0){
				if(eu.checkString(userId)) {
					if(checkUserExists(allUsers,username)) {
						response.sendRedirect("/EventManagementSystem/user");
						return;
					}	
					for(UserModel tempUser:allUsers) {
						if(tempUser.getId() == userIdNumber) {
							tempUser.setId(userIdNumber);
							if(eu.checkString(password)) tempUser.setPassword(password);
							if(eu.checkString(username)) tempUser.setName(username);
							if(eu.checkString(role) && !role.equals("ADMINISTRATOR")) tempUser.setRole(role);
							Boolean updateStatus = new UserDao().updateUser(tempUser);
							if(updateStatus) {
								response.sendRedirect("/EventManagementSystem/user");
								return;
							}
						}
					}
				}	
			}			
		} else {
			//not enough privilages to update user
		}
		//error popup unable to update user
		response.sendRedirect("/EventManagementSystem/user");
	}

	Boolean checkInfo(String userId,String password,String rePassword) {
		ExtraUtilities eu = new ExtraUtilities();
		if( eu.checkString(password) && eu.checkString(rePassword) && password.equals(rePassword) ) {
			if(eu.checkString(userId)) {
				if(Integer.parseInt(userId)>0) return true;
			}			
		}
		return false;
	}

	Boolean checkUserExists(ArrayList<UserModel> allUsers, String username) {
		for(UserModel tempUser:allUsers) {
			if(tempUser.getName() == username) {
				return true;
			}
		}
		return false;
	}
}
