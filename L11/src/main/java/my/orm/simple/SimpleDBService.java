package my.orm.simple;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.Entity;
import javax.persistence.Table;

import app.DBServiceException;
import app.IDBService;
import my.orm.RequestFactory;
import my.orm.datasets.AddressDataSet;
import my.orm.datasets.UserDataSet;
import my.orm.db.Executor;

public class SimpleDBService implements IDBService
{
	private boolean prepared = false;
	
	private final Connection connection;

    public SimpleDBService(Connection connection)
    {
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
    public void prepare() throws DBServiceException
    {
    	checkIsEntityAndTable(UserDataSet.class);
    	checkIsEntityAndTable(AddressDataSet.class);
    	
    	dropTable(UserDataSet.class);
    	dropTable(AddressDataSet.class);
    	
    	createTable(UserDataSet.class);
    	createTable(AddressDataSet.class);
    	
		prepared = true;
    }

    @Override
    public void save(UserDataSet user) throws DBServiceException
    {
    	checkPrepared(prepared);
    	
    	SimpleUserDataSetDAO dao = new SimpleUserDataSetDAO(connection);
    	dao.save(user);
    }

    @Override
	public UserDataSet load(long id) throws DBServiceException
    {
    	checkPrepared(prepared);

    	SimpleUserDataSetDAO dao = new SimpleUserDataSetDAO(connection);
    	return dao.load(id);
    }

    @Override
	public void shutdown() throws DBServiceException
	{
    	checkPrepared(prepared);
    	
    	dropTable(UserDataSet.class);
    	dropTable(AddressDataSet.class);
	}
	
    private void createTable(Class<?> clazz) throws DBServiceException
    {
		String query = null;
		try
		{
			query = RequestFactory.createCreateTableRequest(clazz);
		}
		catch (IllegalArgumentException e)
		{
			throw new DBServiceException(e);
		}
		
    	Executor executor = new Executor(connection);
		try
		{
			executor.execUpdate(query);
		}
		catch (SQLException e)
		{
			throw new DBServiceException(e);
		}
    }
    
    private void dropTable(Class<?> clazz) throws DBServiceException
    {
		String query = RequestFactory.createDropTableRequest(clazz);
		
    	Executor executor = new Executor(connection);
		try
		{
			executor.execUpdate(query);
		}
		catch (SQLException e)
		{
			throw new DBServiceException(e);
		}    	
    }
    
    private static void checkIsEntityAndTable(Class<?> clazz) throws DBServiceException
    {
		Entity[] entity = clazz.getAnnotationsByType(Entity.class);
		if (entity.length == 0)
			throw new DBServiceException("No Entity annotation");
		
		Table[] tables = clazz.getAnnotationsByType(Table.class);
		if (tables.length == 0)
			throw new DBServiceException("No Table annotation");
    }
    
    private static void checkPrepared(boolean prepared) throws DBServiceException
    {
    	if (!prepared)
    	{
    		throw new DBServiceException("Not prepared");
    	}    	
    }
}
