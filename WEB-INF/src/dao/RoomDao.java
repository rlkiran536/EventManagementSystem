package dao;

import java.sql.*;
import java.util.ArrayList;
import dataModels.RoomModel;
import customconnectionpool.*;

public class RoomDao {
	
	public RoomModel getRoom(int id) {
		RoomModel room = new RoomModel();
		Connection connection = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            connection = DataSourcePool.getEmsConnection();
            if(connection == null){
                return null;
            }
            String sql = "SELECT * FROM rooms WHERE id = ?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            resultSet = pst.executeQuery();
            if(resultSet.next()) {
				room.setId(resultSet.getInt("id"));
				room.setBookingId(resultSet.getInt("bookingId"));
				room.setStatus(resultSet.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource(connection,pst,resultSet);
        }
		return room;
	}

	public ArrayList<RoomModel> getAllRooms() {
		ArrayList<RoomModel> rooms = new ArrayList<RoomModel>();
		Connection connection = null;
		 Statement statement = null;
		 ResultSet resultSet = null;
		 try {
			 connection = DataSourcePool.getEmsConnection();
	            if(connection == null){
	                return null;
	            }
	            statement = connection.createStatement();
	            resultSet = statement.executeQuery("SELECT * FROM rooms");
	            while (resultSet.next()) {
	            	RoomModel room = new RoomModel();
	                room.setId(resultSet.getInt("id"));
					room.setBookingId(resultSet.getInt("bookingId"));
					room.setStatus(resultSet.getString("status"));
                	rooms.add(room);
	            }
	        } catch(SQLException e) {
	            e.printStackTrace();
	        } finally {
	            ConnectionCloseUtility.close(resultSet);
	            ConnectionCloseUtility.close(statement);
	            DataSourcePool.releaseEmsConnection(connection);
	        }

		return rooms;
	}

	public Boolean updateRoom(int roomId,int bookingId){
		Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DataSourcePool.getEmsConnection();
            if(connection == null){
                return false;
            }
            connection.setAutoCommit(false);
            pst = connection.prepareStatement("update rooms set status = ? , bookingId = ? where id = ? ");
            pst.setString(1, "OCCUPIED");
            pst.setInt(2, bookingId);
            pst.setInt(3, roomId);
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

	public Boolean removeRoom(int roomId) {
		Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DataSourcePool.getEmsConnection();
            if(connection == null){
                return false;
            }
            connection.setAutoCommit(false);
            pst = connection.prepareStatement("update rooms set status = ? , bookingId = null where id = ? ");
            pst.setString(1, "AVAILABLE");
            pst.setInt(2, roomId);
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