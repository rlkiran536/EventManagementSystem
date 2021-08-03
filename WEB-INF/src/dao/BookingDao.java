package dao;

import java.sql.*;
import java.util.ArrayList;
import dataModels.*;
import customconnectionpool.*;
import utilities.ExtraUtilities;

public class BookingDao {
	
	public BookingModel getBooking(int id) {
		BookingModel booking = new BookingModel();
		Connection connection = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            connection = DataSourcePool.getEmsConnection();
            if(connection == null){
                return null;
            }
            String sql = "SELECT * FROM bookings WHERE id = ?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            resultSet = pst.executeQuery();
            if(resultSet.next()) {
            	booking.setId(resultSet.getInt("id"));
				booking.setUserId(resultSet.getInt("userId"));
				booking.setRoom(resultSet.getInt("room"));
				booking.setRole(resultSet.getString("role"));
				booking.setStatus(resultSet.getString("status"));
				booking.setReason(resultSet.getString("reason"));
				booking.setLastUpdateTime(resultSet.getString("lastUpdated"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource(connection,pst,resultSet);
        }
		return booking;
	}

	public ArrayList<BookingModel> getAllBookings() {
		ArrayList<BookingModel> bookings = new ArrayList<BookingModel>();
		Connection connection = null;
		 Statement statement = null;
		 ResultSet resultSet = null;
		 try {
			 connection = DataSourcePool.getEmsConnection();
	            if(connection == null){
	                return null;
	            }
	            statement = connection.createStatement();
	            resultSet = statement.executeQuery("SELECT * FROM bookings");
	            while (resultSet.next()) {
	            	BookingModel booking = new BookingModel();
	                booking.setId(resultSet.getInt("id"));
					booking.setUserId(resultSet.getInt("userId"));
					booking.setRoom(resultSet.getInt("room"));
					booking.setRole(resultSet.getString("role"));
					booking.setStatus(resultSet.getString("status"));
					booking.setReason(resultSet.getString("reason"));
					booking.setLastUpdateTime(resultSet.getString("lastUpdated"));
                	bookings.add(booking);
	            }
	        } catch(SQLException e) {
	            e.printStackTrace();
	        } finally {
	            ConnectionCloseUtility.close(resultSet);
	            ConnectionCloseUtility.close(statement);
	            DataSourcePool.releaseEmsConnection(connection);
	        }
		return bookings;
	}

	public int addBooking(BookingModel booking,int room,UserModel user){
        int bookingId = 0;
        int count = 0;
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            connection = DataSourcePool.getEmsConnection();
            if(connection == null){
                return -1;
            }

            connection.setAutoCommit(false);
            pst = connection.prepareStatement(
                    "INSERT INTO bookings (room,userId,role,status,reason,lastUpdated) VALUES (?,?,?,?,?,NOW())",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, room);
            pst.setInt(2, user.getId());
            pst.setString(3, user.getRole());
            pst.setString(4, booking.getStatus());
            pst.setString(5, booking.getReason());
            count = pst.executeUpdate();
            if (count <= 0) {
            	return 0;
            } 
            resultSet = pst.getGeneratedKeys();
            if(resultSet.next()) {
                bookingId = resultSet.getInt(1);
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
        } finally {

            closeResource(connection,pst,resultSet);
        }
		return bookingId;
	}

	public Boolean removeBooking(int bookingId) {
		Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DataSourcePool.getEmsConnection();
            if(connection == null){
                return false;
            }
            connection.setAutoCommit(false);
            pst = connection.prepareStatement("DELETE FROM bookings WHERE id = ? ");
            pst.setInt(1, bookingId);
            pst.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
        } finally {
            closeResource(connection,pst);
        }
		return true;
	}

	public Boolean updateBooking(BookingModel booking) {
		Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DataSourcePool.getEmsConnection();
            if(connection == null){
                return false;
            }
            connection.setAutoCommit(false);
            pst = connection.prepareStatement("update bookings set status = ?, reason = ? where id = ? ");
            pst.setString(1, booking.getStatus());
            pst.setString(2, booking.getReason());  
            pst.setInt(3, booking.getId());                 
            int count = pst.executeUpdate();
            if (count < 1) {
            	return false;
            } 
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
        } finally {
            closeResource(connection,pst);
        }
		return true;
	}
    

    private void closeResource(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        ConnectionCloseUtility.close(resultSet);
        closeResource(connection, statement);
    }
 
    private void closeResource(Connection connection, PreparedStatement statement) {
        ConnectionCloseUtility.close(statement);
        DataSourcePool.releaseEmsConnection(connection);
    }
}