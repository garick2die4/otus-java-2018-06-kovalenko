package my.orm.db;

import java.util.Map;

public class ResultObject<T>
{
	private final T object;
	private final Map<String, Object> oneToOneFields;
	
	public ResultObject(T object, Map<String, Object> oneToOneFields)
	{
		this.object = object;
		this.oneToOneFields = oneToOneFields;
	}
	
	public T getObject()
	{
		return object;
	}
	
	public Map<String, Object> getOneToOneFields()
	{
		return oneToOneFields;
	}
}
