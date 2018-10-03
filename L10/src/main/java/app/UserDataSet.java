package app;

import my.orm.DataSet;
import my.orm.annotations.OrmTable;

@OrmTable(tableName = "user")
public final class UserDataSet extends DataSet
{
	private final String name;
	private final short age;
	
	public UserDataSet()
	{
		this("", (short)0);
	}

	public UserDataSet(String name, short age)
	{
		this(0, name, age);
	}

	public UserDataSet(long id, String name, short age)
	{
		super(id);
		
		this.name = name;
		this.age = age;
	}

	public String getName()
	{
		return name;
	}
	
	public short getAge()
	{
		return age;
	}
}
