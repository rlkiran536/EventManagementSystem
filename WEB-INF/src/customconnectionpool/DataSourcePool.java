
package customconnectionpool;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class DataSourcePool {
   
   private static final int COUNT = 10;
   
   private static final LinkedList<Connection> userConnections = new LinkedList<>();
   private static final LinkedList<Connection> emsConnections = new LinkedList<>();
   
   private static final ReentrantLock lock = new ReentrantLock();
   private static final Condition notEmpty = lock.newCondition();
   private static final Condition notFull = lock.newCondition();
   
   private static String USERS_URL;
   
   private static String EMS_URL;
   
   private static String USERNAME;
   
   private static String PASSWORD;
   
   private static String DRIVER_CLASS_NAME;
   
   static {
       try {
           USERS_URL = "jdbc:mysql://localhost:3306/usersDB";
           EMS_URL = "jdbc:mysql://localhost:3306/EMSDB";
           USERNAME = "root";
           PASSWORD = "superUser#1";
           DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

           Class.forName(DRIVER_CLASS_NAME);
           Connection userConnection = null;
           Connection emsConnection = null;
           for (int i = 0; i < COUNT; i++) {
        	   userConnection = DriverManager.getConnection(USERS_URL, USERNAME, PASSWORD);
        	   emsConnection = DriverManager.getConnection(EMS_URL, USERNAME, PASSWORD);
               userConnections.add(userConnection);
               emsConnections.add(emsConnection);
           }
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

   
   public static Connection getUserConnection() {
       final ReentrantLock reentrantLock = lock;
       reentrantLock.lock();
       try {
           if (userConnections.isEmpty()) {
               notEmpty.await();
           }
           Connection connection = userConnections.removeFirst();
           notFull.signalAll();
           return connection;
       } catch (InterruptedException e) {
           e.printStackTrace();
       } finally {
           reentrantLock.unlock();
       }
       return null;
   }
   
   public static Connection getEmsConnection() {
       final ReentrantLock reentrantLock = lock;
       reentrantLock.lock();
       try {
           if (emsConnections.isEmpty()) {
               notEmpty.await();
           }
           Connection connection = emsConnections.removeFirst();
           notFull.signalAll();
           return connection;
       } catch (InterruptedException e) {
           e.printStackTrace();
       } finally {
           reentrantLock.unlock();
       }
       return null;
   }

   public static void releaseUserConnection(Connection connection) {
       final ReentrantLock reentrantLock = lock;
       reentrantLock.lock();
       try {
           if (userConnections.size() == COUNT) {
               notFull.await();
           }
           if (connection == null || connection.isClosed()) {
        	   userConnections.add(DriverManager.getConnection(USERS_URL, USERNAME, PASSWORD));
               notEmpty.signalAll();
               return;
           }
           
           if (connection.getAutoCommit() == false) {
               connection.setAutoCommit(true);
           }
           userConnections.add(connection);
           notEmpty.signalAll();
       } catch (InterruptedException e) {
           e.printStackTrace();
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           reentrantLock.unlock();
       }
   }
   
   public static void releaseEmsConnection(Connection connection) {
       final ReentrantLock reentrantLock = lock;
       reentrantLock.lock();
       try {
           if (emsConnections.size() == COUNT) {
               notFull.await();
           }
           if (connection == null || connection.isClosed()) {
        	   emsConnections.add(DriverManager.getConnection(EMS_URL, USERNAME, PASSWORD));
               notEmpty.signalAll();
               return;
           }
           
           if (connection.getAutoCommit() == false) {
               connection.setAutoCommit(true);
           }
           emsConnections.add(connection);
           notEmpty.signalAll();
       } catch (InterruptedException e) {
           e.printStackTrace();
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           reentrantLock.unlock();
       }
   }

}
