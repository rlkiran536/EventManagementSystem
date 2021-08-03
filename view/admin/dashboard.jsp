<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="java.util.ArrayList"%> 
    <%@ page import="dataModels.*,controller.authentication.UserController" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin Dashboard</title>
<link rel="stylesheet" type="text/css" href="../../styles.css">
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
      ArrayList<BookingModel> allBookings = (ArrayList<BookingModel>) session.getAttribute("allBookings");
      ArrayList<UserModel> allUsers = (ArrayList<UserModel>)  session.getAttribute("allUsers");
   %>
   <header>
      <h1>Welcome <% out.print(user.getName()); %></h1>
   </header>
   <div class="optionsList">
      <button class="optionButton" onclick="window.location='/EventManagementSystem/user'" >Refresh</button>
      <button class="optionButton" onclick="window.location='#addBooking'">Add New Booking</button>
      <button class="optionButton" onclick="window.location='#updateBooking'">Update Booking</button>
      <button class="optionButton" onclick="window.location='#removeBooking'">Remove Booking</button>
      <button class="optionButton" onclick="window.location='#manageBooking'">Manage Booking</button>
      <button class="optionButton" onclick="window.location='#addUser'">Add User</button>
      <button class="optionButton" onclick="window.location='#removeUser'">Remove User</button>
      <button class="optionButton" onclick="window.location='#updateUser'">Update User</button>
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
                     for(BookingModel booking:allBookings){
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
                     for(BookingModel booking:allBookings){
                        out.print("<option value="+booking.getRoom()+">"+booking.getRoom()+"</option>");
                     }
                  %>                  
               </select> <br> <br>
            <button >submit</button>
         </form>
      </div>
   </div>

   <div id="manageBooking" class="overlay">
      <div class="popup manageBookingPopup">
         <h2>Manage Booking</h2>
         <a class="close" href="#">&times;</a>
         <form action="/EventManagementSystem/managebooking" method="post" class="content">
            Select BookingId: <br> <br>
               <select name="bookingId" class="selectclass">
                  <option value="0">Select BookingId</option>
                  <%
                     for(BookingModel booking:allBookings){
                        if(booking.getStatus().equals("PENDING"))
                        out.print("<option value="+booking.getId()+">"+booking.getId()+"</option>");
                     }
                  %>                  
               </select> <br> <br>
               <div class="bookingsList">
                  <div class="bookingsRow bookingsRowHead">
                     <span>Booking Id</span>
                     <span>Room</span>
                     <span>UserId</span>
                     <span>Reason</span>
                     <span>Time</span>
                  </div>
                  <%
                     if(allBookings!=null && allBookings.size()>0) {
                        for(BookingModel booking:allBookings) {
                           if(booking.getStatus().equals("PENDING")) {
                              out.print("<div class='bookingsRow'>");
                              out.print("<span>"+booking.getId()+"</span>");
                              out.print("<span>"+booking.getRoom()+"</span>");
                              out.print("<span>"+booking.getUserId()+"</span>");
                              out.print("<span>"+booking.getReason()+"</span>");
                              out.print("<span>"+booking.getLastUpdateTime()+"</span>");
                              out.print("</div>");   
                           }                           
                        }
                        out.print("Select Choice: <br><br>");
                        out.print("<input type='radio' name='response' value='APPROVE'/>APPROVE");
                        out.print("<input type='radio' name='response' value='DENY'/>DENY");
                     } else out.print("<h1>NO BOOKING REUESTS</h1>");            
                  %>       
               </div>
            <br>  <br>  
            <button >submit</button>
         </form>
      </div>
   </div>

   <div id="addUser" class="overlay">
      <div class="popup">
         <h2>Add User</h2>
         <a class="close" href="#">&times;</a>
         <form action="/EventManagementSystem/adduser" method="post" class="content">
            Enter UserName <br><br><input name="username" placeholder="Enter UserName"> <br> <br>
            Enter Password <br><br><input name="password"  type="password" placeholder="Enter Password"> <br> <br>
            Re-Enter Password <br><br> <input name="rePassword"  type="password" placeholder="Re-Enter Password"> <br> <br>
            Select Role <br> <br>
            <input type="radio" name="role" value="STUDENT"/>STUDENT
            <input type="radio" name="role" VALUE="FACULTY"/>FACULTY
            <br><br>
               <button >submit</button>
         </form>
      </div>
   </div>
   
   <div id="removeUser" class="overlay">
      <div class="popup">
         <h2>Remove User</h2>
         <a class="close" href="#">&times;</a>
         <form action="/EventManagementSystem/removeuser" method="post" class="content">
            Enter UserId <br><br><input name="userId" placeholder="Enter UserId"> <br> <br>
            <button >submit</button>
         </form>
      </div>
   </div>

    <div id="updateUser" class="overlay">
      <div class="popup">
         <h2>Update User</h2>
         <a class="close" href="#">&times;</a>
         <form action="/EventManagementSystem/updateuser" method="post" class="content">
            Select User: <br> <br>
               <select name="userId" class="selectclass">
                  <option value="0">Select User</option>
                  <%
                     for(UserModel tempUser:allUsers){
                        out.print("<option value="+tempUser.getId()+">"+tempUser.getId()+"</option>");
                     }
                  %>                  
               </select> <br> <br>
            Enter UserName <br><br><input name="username" placeholder="Enter UserName"> <br> <br>
            Enter Password <br><br><input name="password"  type="password" placeholder="Enter Password"> <br> <br>
            Re-Enter Password <br><br> <input name="rePassword"  type="password" placeholder="Re-Enter Password"> <br> <br>
            Select Role <br> <br>
            <input type="radio" name="role" value="STUDENT"/>STUDENT
            <input type="radio" name="role" VALUE="FACULTY"/>FACULTY
            <br><br>
               <button >submit</button>
         </form>
      </div>
   </div>
</body>
</html>
