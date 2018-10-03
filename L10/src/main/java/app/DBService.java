package app;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import my.orm.DataSet;
import my.orm.IDBService;
import my.orm.RequestFactory;
import my.orm.SelectRequestHandler;
import my.orm.db.Executor;

public class DBService<T extends DataSet> implements IDBService<T>
{
	private final Class<?> clazz;
	
	private final Connection connection;

    public DBService(Connection connection, Class<?> clazz)
    {
    	this.clazz = clazz;
        this.connection = connection;
    }

    @Override
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
    }
    
    @Override
    public void prepareTables() throws SQLException
    {
		String query = null;
		try
		{
			query = RequestFactory.createCreateTableRequest(clazz);
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			return;
		}
		
    	Executor executor = new Executor(connection);
		executor.execUpdate(query);
    }

    @Override
    public void save(T user) throws SQLException
    {
		String query = null;
		try
		{
			query = RequestFactory.createInsertRequest(user);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
			return;
		}
		
    	Executor executor = new Executor(connection);
		executor.execUpdate(query);
    }

    @Override
	public T load(long id) throws SQLException
    {
		String query = RequestFactory.createSelectWhereRequest(clazz, "id", new Long(id).toString());
    	
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		
    	Executor executor = new Executor(connection);
		return executor.execQuery(query, new SelectRequestHandler<>(clazz, fields));
    }

    @Override
	public void deleteTable() throws SQLException
	{
		String query = RequestFactory.createDeleteTableRequest(clazz);
		
    	Executor executor = new Executor(connection);
		executor.execUpdate(query);
	}
	
}
