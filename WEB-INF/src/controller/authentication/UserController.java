package controller.authentication;

import javax.servlet.annotation.*;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.ArrayList;
import dataModels.*;
import dao.*;

@WebServlet("/user")
public class UserController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		HttpSession session = request.getSession();
		UserModel user = (UserModel) session.getAttribute("user");
		if(user!=null) {
			ArrayList<RoomModel> rooms = new RoomDao().getAllRooms();
			ArrayList<BookingModel> allBookings = new BookingDao().getAllBookings();
			ArrayList<BookingModel> bookings = new ArrayList<BookingModel>();
			for(BookingModel booking: allBookings) {
				if(booking.getRole().equals(user.getRole()))
					bookings.add(booking);
			}
			session.setAttribute("rooms",rooms);
			session.setAttribute("bookings",bookings);
			if(user.getRole().equals("STUDENT"))
				response.sendRedirect("/EventManagementSystem/view/student/dashboard.jsp");
			else if(user.getRole().equals("FACULTY")) {
				session.setAttribute("allBookings",allBookings);
				response.sendRedirect("/EventManagementSystem/view/faculty/dashboard.jsp");
			}
			else if(user.getRole().equals("ADMINISTRATOR")) {
				session.setAttribute("allBookings",allBookings);
				ArrayList<UserModel> allUsers = new UserDao().getAllUsers();
				session.setAttribute("allUsers",allUsers);
				response.sendRedirect("/EventManagementSystem/view/admin/dashboard.jsp");
			}
			else response.sendRedirect("/EventManagementSystem/index.jsp");
		}
	}
}