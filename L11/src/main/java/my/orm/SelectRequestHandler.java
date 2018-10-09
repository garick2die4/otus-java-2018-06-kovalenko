package my.orm;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.OneToOne;

import my.orm.db.ResultHandler;
import my.orm.db.ResultObject;

public class SelectRequestHandler<T> implements ResultHandler<T>
{
	private final Class<?> clazz;
	private T object;
	private final Map<String, Field> fields;
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, IFieldDeserializer> deserializers = new HashMap<>();
	
	static 
	{
		deserializers.put(String.class, (result, obj, f, fn) -> f.set(obj, result.getString(fn)));		
		deserializers.put(long.class, (result, obj, f, fn) -> f.set(obj, result.getLong(fn)));
		deserializers.put(int.class, (result, obj, f, fn) -> f.set(obj, result.getInt(fn)));
		deserializers.put(short.class, (result, obj, f, fn) -> f.set(obj, result.getShort(fn)));
		deserializers.put(byte.class, (result, obj, f, fn) -> f.set(obj, result.getByte(fn)));
		deserializers.put(boolean.class, (result, obj, f, fn) -> f.set(obj, result.getBoolean(fn)));
		deserializers.put(float.class, (result, obj, f, fn) -> f.set(obj, result.getFloat(fn)));
		deserializers.put(double.class, (result, obj, f, fn) -> f.set(obj, result.getDouble(fn)));
	}
	
	public SelectRequestHandler(T object, Map<String, Field> fields)
	{
		this.object = object;
		this.clazz = object.getClass();
		this.fields = fields;
	}

	public SelectRequestHandler(Class<?> clazz, Map<String, Field> fields)
	{
		this.object = null;
		this.clazz = clazz;
		this.fields = fields;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ResultObject<T> handle(ResultSet result) throws SQLException
	{
        result.next();

        if (object == null)
        {
			try
			{
				object = (T)clazz.newInstance();
			}
			catch (InstantiationException | IllegalAccessException e)
			{
				e.printStackTrace();
				return null;
			}
        }	
        Map<String, Object> oneToOneFields = new HashMap<>();
		try
		{
            for (Map.Entry<String,Field> entry : fields.entrySet())
            {
            	String nm = entry.getKey();
            	Field f = entry.getValue();
            	f.setAccessible(true);
            	if (deserializers.containsKey(f.getType()))
            	{
            		deserializers.get(f.getType()).deserialize(result, object, f, nm); 
            	}
            	else
            	{
            		OneToOne[] oos = f.getAnnotationsByType(OneToOne.class);
            		if (oos.length != 0)
            			oneToOneFields.put(f.getName(), result.getObject(f.getName()));
            	}
            }
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		}
        return new ResultObject<T>(object, oneToOneFields);
	}

}
