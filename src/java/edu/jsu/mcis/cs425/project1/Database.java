package edu.jsu.mcis.cs425.project1;

import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Database {
    
    private Connection getConnection() {
        
        Connection conn = null;
        
        try {
            
            Context envContext = new InitialContext();
            Context initContext  = (Context)envContext.lookup("java:/comp/env");
            DataSource ds = (DataSource)initContext.lookup("jdbc/db_pool");
            conn = ds.getConnection();
            
        }        
        catch (SQLException | NamingException e) { e.printStackTrace(); }
        
        return conn;

    }
    
    public String getResultSetTable(ResultSet resultset) throws ServletException, IOException {
        
        ResultSetMetaData metadata = null;
        
        String table = "";
        String tableheading;
        String tablerow;
        
        String key;
        String value;
        
        try {
            
            System.out.println("*** Getting Query Results ... ");

            metadata = resultset.getMetaData();

            int numberOfColumns = metadata.getColumnCount();
            
            table += "<table border=\"1\">";
            tableheading = "<tr>";
            
            System.out.println("*** Number of Columns: " + numberOfColumns);
            
            for (int i = 1; i <= numberOfColumns; i++) {
            
                key = metadata.getColumnLabel(i);
                
                tableheading += "<th>" + key + "</th>";
            
            }
            
            tableheading += "</tr>";
            
            table += tableheading;
                        
            while(resultset.next()) {
                
                tablerow = "<tr>";
                
                for (int i = 1; i <= numberOfColumns; i++) {

                    value = resultset.getString(i);

                    if (resultset.wasNull()) {
                        tablerow += "<td></td>";
                    }

                    else {
                        tablerow += "<td>" + value + "</td>";
                    }
                    
                }
                
                tablerow += "</tr>";
                
                table += tablerow;
                
            }
            
            table += "</table><br />";

        }
        
        catch (SQLException e) {}
        
        return table;
        
    } // End getResultSetTable()
    
    
    private ResultSet getResultSet(String query) throws SQLException{
        
        Connection connection = getConnection();
        PreparedStatement pStatement = connection.prepareStatement(query);
        pStatement.execute();
        
        ResultSet results = pStatement.getResultSet();
        

        return results;
    }
    
    public String getSession(String sessionID) throws SQLException, ServletException, IOException{
        
        String query = "SELECT * FROM registration_db.registrations " + "WHERE sessionid = " + sessionID;
        
        return getResultSetTable(getResultSet(query));
    }
    
    
    public JSONObject addAttendee(String firstname, String lastname, String displayname, int sessionid) throws SQLException{
        
        
        int primaryKey = 0;
        
        String query = "INSERT INTO registration_db.registrations (firstname,lastname,displayname,sessionid) VALUES (?,?,?,?)" ;
    
        Connection connection = getConnection();
        PreparedStatement pStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        
        pStatement.setString(1,firstname);
        pStatement.setString(2,lastname);
        pStatement.setString(3,displayname);
        pStatement.setInt(4, sessionid);
        
        int result = pStatement.executeUpdate();
        ResultSet keys;
        
        if(result == 1){
             keys = pStatement.getGeneratedKeys();
            if(keys.next()){
                primaryKey = keys.getInt(1);
            }
        }
        
        connection.close();
        pStatement.close();

        String registrationID = String.format("%06d", primaryKey);
        
        JSONObject json = new JSONObject();
        json.put("code", "R" + registrationID);
        json.put("displayname",displayname);
        
        return json;
    }
}
