package controller.booking;


import java.io.*;  
import java.util.ArrayList;
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.servlet.annotation.*;
import dataModels.*;
import dao.*;
import utilities.ExtraUtilities;

@WebServlet("/managebooking")
public class ManageBooking extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session = request.getSession();
		ExtraUtilities eu = new ExtraUtilities();
		ArrayList<BookingModel> allBookings = (ArrayList<BookingModel>) session.getAttribute("allBookings");
		if(allBookings!=null && allBookings.size()>0) {
			String result = request.getParameter("response");
			String bookingId = request.getParameter("bookingId");	
			if(eu.checkString(result) && eu.checkString(bookingId)) {
				int bookingNumber = Integer.parseInt(bookingId);
				if(bookingNumber>0) {
					for(BookingModel booking:allBookings) {
						if(booking.getId() == bookingNumber){
							if(result.equals("DENY")) {
								booking.setStatus("REJECTED");
								Boolean bookingStatus = new BookingDao().updateBooking(booking);
								if(bookingStatus) {
									response.sendRedirect("/EventManagementSystem/");
									return;
								}
							}
							else {
								booking.setStatus("OCCUPIED");
								Boolean bookingStatus = new BookingDao().updateBooking(booking);
								if(bookingStatus) {
									Boolean roomStatus = new RoomDao().updateRoom(booking.getRoom(),booking.getId());
									if(roomStatus) {
										response.sendRedirect("/EventManagementSystem/");
										return;
									} else {
										//failed to update room
									}
								}
							}												
						}
					}																	
				}
			}
		}
		//error updating booking
		response.sendRedirect("/EventManagementSystem/");
	}
}
