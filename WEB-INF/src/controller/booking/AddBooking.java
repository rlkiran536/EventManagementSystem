package controller.booking;


import java.io.*;
import java.util.ArrayList;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import javax.servlet.annotation.*;
import dataModels.*;
import dao.*;
import utilities.*;

@WebServlet("/addbooking")
public class AddBooking extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		PrintWriter out = response.getWriter();
		ExtraUtilities eu = new ExtraUtilities();
		HttpSession session = request.getSession();

		UserModel user = (UserModel) session.getAttribute("user");
		String roomId = (String) request.getParameter("roomId");
		String reason = (String) request.getParameter("reason");
		String status = "OCCUPIED";
		
		if(eu.checkString(roomId) && eu.checkString(reason)) {
			int roomNumber = Integer.parseInt(roomId);

			ArrayList<BookingModel> bookings = (ArrayList<BookingModel>) session.getAttribute("bookings");
			for(BookingModel userBooking:bookings) {
				if(userBooking.getRoom() == roomNumber) {
					if(userBooking.getUserId() == user.getId()) {
						response.sendRedirect("/EventManagementSystem/user");
						return;	
					}					
				}
			}
			
			RoomModel room = new RoomModel();
			room = new RoomDao().getRoom(roomNumber);
			if(room!=null && room.getStatus().equals("AVAILABLE")) {
				BookingModel booking = new BookingModel();
				if(user.getRole().equals("STUDENT"))
					status= "PENDING";
				booking.setStatus(status);
				booking.setReason(reason);	
				out.println(roomNumber);	
				out.println(user.toString());
				out.println(booking.toString());
				int bookingId = new BookingDao().addBooking(booking,roomNumber,user);				
				if(bookingId>0) {
					if(user.getRole().equals("FACULTY") || user.getRole().equals("ADMINISTRATOR")) {
						Boolean bookingStatus = new RoomDao().updateRoom(roomNumber,bookingId);
						if(bookingStatus) {
							response.sendRedirect("/EventManagementSystem/user");
							return;
						} else {
						//failed to update room
						}
					}
				} else {
					//failed to add booking
				}			
			} else {
				//room not available
			}
		} else {
			//invalid inputs
		}
		response.sendRedirect("/EventManagementSystem/user");
	}
}
