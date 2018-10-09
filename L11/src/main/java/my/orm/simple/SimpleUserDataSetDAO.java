package my.orm.simple;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import app.DBServiceException;
import my.orm.RequestFactory;
import my.orm.SelectRequestHandler;
import my.orm.datasets.AddressDataSet;
import my.orm.datasets.UserDataSet;
import my.orm.db.Executor;
import my.orm.db.ResultObject;

class SimpleUserDataSetDAO
{
	private final Connection connection;
	
	SimpleUserDataSetDAO(Connection connection)
	{
		this.connection = connection;		
	}
	
    public void save(UserDataSet user) throws DBServiceException
    {
    	doInsert(user.getAddress());
    	doInsert(user);
    }

    public UserDataSet load(long id) throws DBServiceException
    {
    	ResultObject<UserDataSet> result = doLoad(UserDataSet.class, id);
    	UserDataSet user = result.getObject();
    	for (Map.Entry<String, Object> entry : result.getOneToOneFields().entrySet())
    	{
    		if (entry.getKey().equals("address"))
    		{
    			Long val = (Long)entry.getValue(); 
    			ResultObject<AddressDataSet> addrResult = doLoad(AddressDataSet.class, val.longValue());
    			user.setAddress(addrResult.getObject());
    		}
    	}
		return user;
    }
    
    private <T> void doInsert(T object) throws DBServiceException
    {
		String query = null;
		try
		{
			query = RequestFactory.createInsertRequest(object);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new DBServiceException(e);
		}

		Map<String, Field> fields = new HashMap<>();
		try
		{
			Field f = UserDataSet.class.getSuperclass().getDeclaredField("id");
			fields.put("GENERATED_KEY", f);
		}
		catch (NoSuchFieldException | SecurityException e)
		{
			throw new DBServiceException(e);
		}
		
		try
		{
	    	Executor executor = new Executor(connection);
			executor.execInsert(query, new SelectRequestHandler<>(object, fields));
		}
		catch (SQLException e)
		{
			throw new DBServiceException(e);
		}    	
    }
    
    private <T> ResultObject<T> doLoad(Class<?> clazz, long id) throws DBServiceException
    {
		String query = RequestFactory.createSelectWhereRequest(clazz, "id", new Long(id).toString());
    	
        Map<String, Field> fields = new HashMap<>();
        for (Field f : clazz.getSuperclass().getDeclaredFields())
        {
        	fields.put(f.getName(), f);        	
        }

        for (Field f :clazz.getDeclaredFields())
        {
    		fields.put(f.getName(), f);        	
        }
		
		try
		{
	    	Executor executor = new Executor(connection);
	    	return executor.execQuery(query, new SelectRequestHandler<>(clazz, fields));
		}
		catch (SQLException e)
		{
			throw new DBServiceException(e);
		}    	
    }
}
