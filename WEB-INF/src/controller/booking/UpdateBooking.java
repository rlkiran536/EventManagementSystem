package controller.booking;


import java.io.*;  
import java.util.ArrayList;
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.servlet.annotation.*;
import dataModels.*;
import utilities.ExtraUtilities;
import dao.*;


@WebServlet("/updatebooking")
public class UpdateBooking extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session = request.getSession();
		UserModel user = (UserModel) session.getAttribute("user");
		ExtraUtilities eu = new ExtraUtilities();
		String roomId = request.getParameter("roomId");
		String reason = request.getParameter("reason");


		if(eu.checkString(reason)) {
			int roomNumber = Integer.parseInt(roomId);
			if(roomNumber > 0 && roomNumber < 11) {
				ArrayList<BookingModel> bookings = new BookingDao().getAllBookings();
				for(BookingModel booking:bookings) {
					if(booking.getRoom() == roomNumber) {
						if(booking.getUserId()==user.getId() ||
							(user.getRole().equals("ADMINISTRATOR") &&
							 booking.getStatus().equals("OCCUPIED"))
							) {
							booking.setReason(reason);
							Boolean bookingUpdateStatus = new BookingDao().updateBooking(booking);
							if(bookingUpdateStatus) {
								response.sendRedirect("/EventManagementSystem/");
								return;
							} 
						}
					}
				}
			} else {
				// invalid roomNumber
			}
		} else {
			//invalid reason
		}
		//show error popup
		response.sendRedirect("/EventManagementSystem/");
	}

}
