package controller.user;


import java.io.*;
import java.util.ArrayList;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.servlet.annotation.*;
import dataModels.*;
import dao.*;
import utilities.*;

@WebServlet("/removeuser")
public class RemoveUser extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session =  request.getSession();
		ExtraUtilities eu = new ExtraUtilities();
		String userId = request.getParameter("userId");
		if(eu.checkString(userId)) {
			int userIdNumber = Integer.parseInt(userId);
			Boolean userStatus = new UserDao().removeUser(userIdNumber);
			if(userStatus) {
				ArrayList<BookingModel> allBookings = new BookingDao().getAllBookings();
				for(BookingModel booking:allBookings) {
					if(booking.getUserId() == userIdNumber) {
						Boolean bookingStatus = new BookingDao().removeBooking(booking.getId());
						if(!bookingStatus) {
							Boolean roomStatus = new RoomDao().removeRoom(booking.getRoom());
							if(!roomStatus) {
								//error unable to remove room
							}
						} else {
							//error unable to remove booking
						}
					}
				}
				response.sendRedirect("/EventManagementSystem/user");
				return;		
			}
		}
		//error popup unable to remove user
		response.sendRedirect("/EventManagementSystem/user");
	}
}
