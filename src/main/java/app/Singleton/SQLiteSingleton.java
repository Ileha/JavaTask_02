package app.Singleton;

import org.sqlite.JDBC;
import java.sql.*;
import java.io.*;
import app.Singleton.Exception.*;

public class SQLiteSingleton {
    private static final String CON_STR = "jdbc:sqlite:%s";
    private static SQLiteSingleton instance = null;

    public static void Init(String url) throws SQLException {
        if (instance == null) {
            instance = new SQLiteSingleton(url);
        }
    }

    public static SQLiteSingleton getInstance() throws SinletoneException {
        if (instance == null) {
            throw new NotInitialized("SQLiteSingleton");
        }
        return instance;
    }

    public Connection connection;
    private String DBRealPath;

    SQLiteSingleton(String url) throws SQLException {
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(String.format(CON_STR, url));
        DBRealPath = url;
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys=ON;");
        }
    }

    public String GetResoursePath() {
        return DBRealPath;
    }

//    public void getAll() { 
//        try (Statement statement = this.connection.createStatement()) {            
//            ResultSet resultSet = statement.executeQuery("SELECT id, name FROM main_table");
//            while (resultSet.next()) {
//                System.out.printf("id = %s, name = %s\n", resultSet.getString("id"), resultSet.getString("name"));
//            }
// 
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    
    public void getAllFromFolder(int folder_ID, PrintWriter write) { 
        try (Statement statement = this.connection.createStatement()) {            
            ResultSet resultSet = statement.executeQuery(String.format("SELECT id, name FROM main_table where ref=%s", folder_ID));
            while (resultSet.next()) {
                write.printf("id = %s, name = %s</br>", resultSet.getString("id"), resultSet.getString("name"));
                //System.out.printf("id = %s, name = %s\n", resultSet.getString("id"), resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//    public boolean IsLeaf(int node_ID) {
//        boolean res = false;
//        try(Statement statement = this.connection.createStatement()) {
//            ResultSet resultSet = statement.executeQuery(String.format("SELECT COUNT() FROM main_table WHERE ref=%s", node_ID));
//            if (resultSet.getInt(1) > 0) {
//                res = true;
//            }
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return res;
//    }
}
