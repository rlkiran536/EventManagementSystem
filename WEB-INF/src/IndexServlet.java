import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import dao.*;
import dataModels.*;
import utilities.*;

@WebServlet("/")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getRemoteUser();
		HttpSession session = request.getSession();
		if(username==null) {
			session.setAttribute("user",null);
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		} else {
			UserModel user = getUserData(username);
			if(user!=null) {
				session.setAttribute("user",user);
				session.setAttribute("rooms",getRooms());
				session.setAttribute("bookings",getBookings(user.getId()));
				session.setAttribute("allBookings",null);
				session.setAttribute("allUsers",null);
				if(request.isUserInRole("ADMINISTRATOR")) {
					session.setAttribute("allBookings",getAllBookings());
					session.setAttribute("allUsers",getAllUsers());
					response.sendRedirect(request.getContextPath()+"/view/admin/dashboard.jsp");
				}
				else if(request.isUserInRole("FACULTY")) {
					session.setAttribute("allBookings",getAllBookings());
					response.sendRedirect(request.getContextPath()+"/view/faculty/dashboard.jsp");
				}
				else if(request.isUserInRole("STUDENT"))
					response.sendRedirect(request.getContextPath()+"/view/student/dashboard.jsp");	
			}			
		}
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// PrintWriter out = response.getWriter();
		// out.print("INVALID CREDENTIALS");
		String username = request.getRemoteUser();
		HttpSession session = request.getSession();
		if(username==null) {
			session.setAttribute("user",null);
			response.sendRedirect(request.getContextPath()+"/login.jsp");
		} else {
			UserModel user = getUserData(username);
			if(user!=null) {
				session.setAttribute("user",user);
				session.setAttribute("rooms",getRooms());
				session.setAttribute("bookings",getBookings(user.getId()));
				session.setAttribute("allBookings",null);
				if(request.isUserInRole("ADMINISTRATOR")) {
					session.setAttribute("allBookings",getAllBookings());
					response.sendRedirect(request.getContextPath()+"/view/admin/dashboard.jsp");
				}
				else if(request.isUserInRole("FACULTY")) {
					session.setAttribute("allBookings",getAllBookings());
					response.sendRedirect(request.getContextPath()+"/view/faculty/dashboard.jsp");
				}
				else if(request.isUserInRole("STUDENT"))
					response.sendRedirect(request.getContextPath()+"/view/student/dashboard.jsp");	
			}			
		}	
	}


	UserModel getUserData(String username) {	
		UserModel user = new UserDao().getUser(username);
		if(user!=null) {
			return user;
		} else return null;
	}

	ArrayList<RoomModel> getRooms() {
		ArrayList<RoomModel> rooms = new RoomDao().getAllRooms();
		if(rooms!=null && rooms.size() > 0)
			return rooms;
		else return null;
	}

	ArrayList<BookingModel> getBookings(int userId) {
		ArrayList<BookingModel> allBookings = new BookingDao().getAllBookings();
		ArrayList<BookingModel> bookings = new ArrayList<BookingModel>();
		for(BookingModel booking:allBookings) {
			if(booking.getUserId()==userId) bookings.add(booking);
		}
		if(bookings!=null) return bookings;
		else return null;
	}

	ArrayList<BookingModel> getAllBookings() {
		ArrayList<BookingModel> allBookings = new BookingDao().getAllBookings();
		if(allBookings!=null) return allBookings;	
		else return null;		
	}

	ArrayList<UserModel> getAllUsers() {
		ArrayList<UserModel> allUsers = new UserDao().getAllUsers();
		if(allUsers != null && allUsers.size() > 0) return allUsers;
		else return null;
	}
}
