package app;

import java.sql.Connection;
import java.sql.SQLException;

public class DBService implements AutoCloseable
{
	private final Connection connection;

    public DBService()
    {
        connection = ConnectionHelper.getConnection();
    }

    public String getMetaData()
    {
        try
        {
            return "Connected to: " + connection.getMetaData().getURL() + "\n" +
                    "DB name: " + connection.getMetaData().getDatabaseProductName() + "\n" +
                    "DB version: " + connection.getMetaData().getDatabaseProductVersion() + "\n" +
                    "Driver: " + connection.getMetaData().getDriverName();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return e.getMessage();
        }
    }
	
    @Override
    public void close() throws Exception
    {
        connection.close();
        System.out.println("Connection closed. Bye!");
    }
    
    void prepareTables() throws SQLException
    {
    	
    }

    void addUser(UserDataSet user) throws SQLException
    {
    	
    }

    UserDataSet getUser(long id) throws SQLException
    {
    	
    }

	 
}
