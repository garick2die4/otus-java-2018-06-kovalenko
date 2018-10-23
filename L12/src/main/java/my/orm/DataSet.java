package my.orm;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class DataSet
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
