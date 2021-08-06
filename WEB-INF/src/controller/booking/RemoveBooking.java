package controller.booking;


import java.io.*;  
import java.util.ArrayList;
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.servlet.annotation.*;
import dataModels.*;
import dao.*;


@WebServlet("/removebooking")
public class RemoveBooking extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session = request.getSession();
		UserModel user = (UserModel) session.getAttribute("user");
		int roomNumber = Integer.parseInt((String)request.getParameter("roomId"));
		ArrayList<BookingModel> bookings = new BookingDao().getAllBookings();
		if(roomNumber>0 && roomNumber<11) {
			for(BookingModel booking:bookings) {
				if( booking.getRoom() == roomNumber &&
				 	(	booking.getUserId() == user.getId() ||
				 		user.getRole().equals("ADMINISTRATOR"))
				 ) {
					Boolean bookingStatus = new BookingDao().removeBooking(booking.getId());
					if(bookingStatus) {
						if(user.getRole().equals("FACULTY") || user.getRole().equals("ADMINISTRATOR")) {
							Boolean roomStatus = new RoomDao().removeRoom(roomNumber);
							if(!roomStatus) { // error updating room
							}
						}
						response.sendRedirect("/EventManagementSystem/");
						return;
					}
				}
			}
		} else {
			//invalid room
		}
		//show error popup
		response.sendRedirect("/EventManagementSystem/");
	}
}
