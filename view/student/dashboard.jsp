 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.ArrayList"%> 
    <%@ page import="dataModels.*,controller.authentication.UserController" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Student Dashboard</title>
<style type="text/css">
   body, .optionButton  {
   font-family: monospace;
}
      /* AUTH FORM */
.formContainer {
      text-align: center;
     min-width: 50%;
    margin: 2% 25%;
}

.formContainer form {
   padding: 15% 10%;
  border: 1px solid beige;
  font-size: 1.1rem;
  font-weight: bold;
}

.formContainer input {
      padding: 0.6rem;
    border: 1px solid #252514;
    border-radius: 10px;
}

.formContainer button {
     color: white;
    background-color: #480505d4;
    padding: 0.7rem 2rem;
    border: 1px solid beige;
    border-radius: 10px;
    cursor: pointer;
}

header {
   text-align: center;
}

.optionsList {
   width: 50%;
   height: 9vh;
   margin: 0 auto;
   display: flex;
   justify-content: space-around;
}

.optionButton {
   padding: 1%;
   margin: 1%;
   background-color: green;
   color: white;
   border-radius: 5px;
   transition: all .3s ease-out;
}

.optionButton:hover {
   padding: 1.5%;
   margin: 0.5%;
   font-size: 1rem;
}

.bookingsList {
   width: 70%;
   margin: 2% auto;
}

.bookingsRow {
   border: 1px solid black;
   display: flex;
   justify-content: space-between;
}

.bookingsRowHead {
   font-size: 1rem;
   font-style: italic;
  font-weight: bolder;
}

.bookingsRow span {
   padding: 2%;
   min-width: 15%;
}

   /* POPUP */
.overlay {
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.7);
  transition: opacity 500ms;
  visibility: hidden;
  opacity: 0;
}
.overlay:target {
  visibility: visible;
  opacity: 1;
}

.popup {
  margin: 5% auto;
  padding: 1rem;
  background: #fff;
  border-radius: 5px;
  width: 30%;
  min-width: 30%;
  position: relative;
  text-decoration: none;
  text-align: center;
}

.popup .close {
  position: absolute;
  top: 20px;
  right: 30px;
  transition: all 200ms;
  font-size: 30px;
  font-weight: bold;
  text-decoration: none;
  color: #333;
}

.popup .close:hover {
  color: #06D85F;
}

.selectclass {
     width: 45%;
    text-align: center;
    padding: 1% 10%;
}

.popup input {
   padding: 0.3rem;
   text-align: center;
}

.manageBookingPopup {
   width: 80%;
}
</style>
</head>
<body>
   <% 
      response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); 
      if(session.getAttribute("user")==null) {
            response.sendRedirect("/EventManagementSystem/user");
      }
      UserModel user = (UserModel)session.getAttribute("user");
      ArrayList<RoomModel> rooms = (ArrayList<RoomModel>) session.getAttribute("rooms");
      ArrayList<BookingModel> bookings = (ArrayList<BookingModel>) session.getAttribute("bookings");
   %>
	<header>
      <h1>Welcome <% out.print(user.getName()); %></h1>
   </header>
   <div class="optionsList">
      <button class="optionButton" onclick="window.location='/EventManagementSystem/'" >Refresh</button>
      <button class="optionButton" onclick="window.location='#addBooking'">Add New Booking</button>
      <button class="optionButton" onclick="window.location='#updateBooking'">Update Booking</button>
      <button class="optionButton" onclick="window.location='#removeBooking'">Remove Booking</button>
      <button class="optionButton" onclick="window.location='/EventManagementSystem/Logout'" >Logout</button>
   </div>
   
   <div class="bookingsList">
      <div class="bookingsRow bookingsRowHead">
         <span>S.no</span>
         <span>Room</span>
         <span>Status</span>
         <span>Reason</span>
         <span>Time</span>
      </div>
      <%
         int i = 1;
         if(bookings!=null && bookings.size()>0) {
            for(BookingModel booking:bookings) {
               out.print("<div class='bookingsRow'>");
               out.print("<span>"+i+"</span>");
               out.print("<span>"+booking.getRoom()+"</span>");
               out.print("<span>"+booking.getStatus()+"</span>");
               out.print("<span>"+booking.getReason()+"</span>");
               out.print("<span>"+booking.getLastUpdateTime()+"</span>");
               out.print("</div>");
               i++;
            }   
         } else out.print("<h1>NO BOOKINGS</h1>");            
      %>       
   </div>

   <div id="addBooking" class="overlay">
      <div class="popup">
         <h2>Add Booking</h2>
         <a class="close" href="#">&times;</a>
         <form action="/EventManagementSystem/addbooking" method="post" class="content">
            Select Room: <br> <br>
               <select name="roomId" class="selectclass">
                  <option value="0">Select Room</option>
                  <%
                     for(RoomModel room:rooms){
                        if(room.getStatus().equals("AVAILABLE"))
                        out.print("<option value="+room.getId()+">"+room.getId()+"</option>");
                     }
                  %>                  
               </select> <br> <br>
            Enter Reason: <br> <br>
               <input type="text" name="reason" placeholder="Enter Reason"> <br> <br>
            <button >submit</button>
         </form>
      </div>
   </div>
   
   <div id="updateBooking" class="overlay">
      <div class="popup">
         <h2>Update Booking</h2>
         <a class="close" href="#">&times;</a>
         <form action="/EventManagementSystem/updatebooking" method="post" class="content">
           Select Room: <br> <br>
               <select name="roomId" class="selectclass">
                  <option value="0">Select Room</option>
                  <%
                     for(BookingModel booking:bookings){
                        if(booking.getUserId() == user.getId())
                        out.print("<option value="+booking.getRoom()+">"+booking.getRoom()+"</option>");
                     }
                  %>                  
               </select> <br> <br>
            Enter Reason: <br> <br>
               <input type="text" name="reason" placeholder="Enter Reason"> <br> <br>
            <button >submit</button>
         </form>
      </div>
   </div>

   <div id="removeBooking" class="overlay">
      <div class="popup">
         <h2>Remove Booking</h2>
         <a class="close" href="#">&times;</a>
         <form action="/EventManagementSystem/removebooking" method="post" class="content">
            Select Room: <br> <br>
               <select name="roomId" class="selectclass">
                  <option value="0">Select Room</option>
                  <%
                     for(BookingModel booking:bookings){
                        if(booking.getUserId() == user.getId())
                        out.print("<option value="+booking.getRoom()+">"+booking.getRoom()+"</option>");
                     }
                  %>                  
               </select> <br> <br>
            <button >submit</button>
         </form>
      </div>
   </div>

</body>
</html>