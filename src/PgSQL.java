
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.*;
import java.sql.*;
import java.lang.*;


public class PgSQL 
{
    public Connection conn;
    public PreparedStatement pst;
    
	public PgSQL() 
	{
		try 
		{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection ("jdbc:postgresql://localhost:5432/eAlps", "postgres","123456");
            System.out.println("conn built...");
        } 
		catch (SQLException e) {e.printStackTrace();} 
		catch (ClassNotFoundException e) {e.printStackTrace();}
	}
	
	public ResultSet retriveData(String SQL) throws SQLException 
	{
        pst = conn.prepareStatement (SQL,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        System.out.println("data retrival...");
        return pst.executeQuery();  
    }
	

	public void insertData(String SQL, String inputData) throws SQLException 
	{
		pst = conn.prepareStatement (SQL);
		pst.setObject(1, inputData);
		pst.executeUpdate();
		System.out.println("data inserted...");
	}
	
	public void deletData(String SQL) throws SQLException 
	{
		 System.out.println("entered deleted...");
		 pst = conn.prepareStatement (SQL);
        pst.executeUpdate();
        System.out.println("table deleted...");
    }
	
    @Override
    protected void finalize() throws Throwable 
    {
    	if (conn != null || !conn.isClosed()) {conn.close();}
    }
}
