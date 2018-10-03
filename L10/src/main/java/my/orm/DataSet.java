package my.orm;

import my.orm.annotations.OrmAutoIncrement;
import my.orm.annotations.OrmPrimaryKey;

public abstract class DataSet
{
	@OrmAutoIncrement
	@OrmPrimaryKey
	private long id;
	
	public DataSet()
	{
		id = 0;
	}
	
	public DataSet(long id)
	{
		this.id = id;
	}
	
	public long getId()
	{
		return id;
	}
}
