package my.orm;

public abstract class DataSet
{
	private long id;
	
	public DataSet(long id)
	{
		this.id = id;
	}
	
	public long getId()
	{
		return id;
	}
}
