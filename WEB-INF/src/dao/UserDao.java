package dao;

import java.sql.*;
import java.util.ArrayList;
import dataModels.UserModel;
import customconnectionpool.*;

public class UserDao {
	
    public UserModel userLogin(String name,String password) {
        UserModel user = new UserModel();   
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            connection = DataSourcePool.getUserConnection();
            if(connection == null){
                return null;
            }
            String sql = "SELECT * FROM users WHERE name = ? AND password = md5(?)";
            pst = connection.prepareStatement(sql);
            pst.setString(1, name);
            pst.setString(2, password);
            resultSet = pst.executeQuery();
            if(resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setRole(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource(connection,pst,resultSet);
        }
        return user;
    }
    
	public UserModel getUser(int id) {
		UserModel user = new UserModel();	
		Connection connection = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            connection = DataSourcePool.getUserConnection();
            if(connection == null){
                return null;
            }
            String sql = "SELECT * FROM users WHERE id = ?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, id);
            resultSet = pst.executeQuery();
            if(resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setRole(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource(connection,pst,resultSet);
        }
		return user;
	}

	public UserModel getUser(String name) {
		UserModel user = new UserModel();	
		Connection connection = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            connection = DataSourcePool.getUserConnection();
            if(connection == null){
                return null;
            }
            String sql = "SELECT * FROM users WHERE name = ?";
            pst = connection.prepareStatement(sql);
            pst.setString(1, name);
            resultSet = pst.executeQuery();
            if(resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setRole(resultSet.getString("role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource(connection,pst,resultSet);
        }
		return user;
	}

	public ArrayList<UserModel> getAllUsers() {
		ArrayList<UserModel> users = new ArrayList<UserModel>();
		Connection connection = null;
		 Statement statement = null;
		 ResultSet resultSet = null;
		 try {
			 connection = DataSourcePool.getUserConnection();
	            if(connection == null){
	                return null;
	            }
	            statement = connection.createStatement();
	            resultSet = statement.executeQuery("SELECT * FROM users");
	            while (resultSet.next()) {
	            	UserModel user = new UserModel();
	                user.setId(resultSet.getInt("id"));
                	user.setName(resultSet.getString("name"));
                	user.setRole(resultSet.getString("role"));
                	users.add(user);
	            }
	        } catch(SQLException e) {
	            e.printStackTrace();
	        } finally {
	            ConnectionCloseUtility.close(resultSet);
	            ConnectionCloseUtility.close(statement);
	            DataSourcePool.releaseUserConnection(connection);
	        }
		return users;
	}

	public UserModel addUser(UserModel user){
		Connection connection = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            connection = DataSourcePool.getUserConnection();
            if(connection == null){
                return null;
            }

            connection.setAutoCommit(false);
            pst = connection.prepareStatement(
                    "INSERT INTO users (name,password,role,lastUpdated) VALUES (?,md5(?),?,now())",
                    Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, user.getName());
            pst.setString(2, user.getPassword());
            pst.setString(3, user.getRole());
            int count = pst.executeUpdate();
            if (count > 0) {
                resultSet = pst.getGeneratedKeys();
            	if (resultSet.next()) {
                	user.setId(resultSet.getInt(1));
            	}
            	connection.commit();
            } 
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ee) {
                ee.printStackTrace();
            }
        } finally {
            closeResource(connection,pst,resultSet);
        }
		return user;
	}

	public Boolean removeUser(int userId) {
		Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DataSourcePool.getUserConnection();
            if(connection == null){
                return false;
            }
            connection.setAutoCommit(false);
            pst = connection.prepareStatement("DELETE FROM users WHERE id = ? ");
            pst.setInt(1, userId);
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

	public Boolean updateUser(UserModel user) {
		Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = DataSourcePool.getUserConnection();
            if(connection == null){
                return false;
            }
            connection.setAutoCommit(false);
            if(user.getPassword()!=null) {
                pst = connection.prepareStatement("update users set name = ?, password = md5(?), role = ? where id =? ");    
                pst.setString(1, user.getName());
                pst.setString(2, user.getPassword());
                pst.setString(3, user.getRole());
                pst.setInt(4, user.getId());                         
            } else {
                pst = connection.prepareStatement("update users set name = ?, role = ? where id =? ");
                pst.setString(1, user.getName());
                pst.setString(2, user.getRole());
                pst.setInt(3, user.getId());                         
            }            
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
        DataSourcePool.releaseUserConnection(connection);
    }
}