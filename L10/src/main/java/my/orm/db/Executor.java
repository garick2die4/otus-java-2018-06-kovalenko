package my.orm.db;

import java.sql.Connection;

public class Executor
{
	private final Connection connection;
	
	public Executor(Connection connection)
	{
		this.connection = connection;
	}
	
	public <T> void save(T user)
	{
		
	}

	public <T> T load(long id, Class<T> clazz)
	{
		return null;
	}
}
