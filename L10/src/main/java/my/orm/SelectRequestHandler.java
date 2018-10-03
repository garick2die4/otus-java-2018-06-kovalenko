package my.orm;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.orm.db.ResultHandler;

public class SelectRequestHandler<T> implements ResultHandler<T>
{
	private final Class<?> clazz;
	private final List<Field> fields;
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, IFieldDeserializer> deserializers = new HashMap<>();
	
	static 
	{
		deserializers.put(String.class, (result, obj, f) -> f.set(obj, result.getString(f.getName())));		
		deserializers.put(long.class, (result, obj, f) -> f.set(obj, result.getLong(f.getName())));
		deserializers.put(int.class, (result, obj, f) -> f.set(obj, result.getInt(f.getName())));
		deserializers.put(short.class, (result, obj, f) -> f.set(obj, result.getShort(f.getName())));
		deserializers.put(byte.class, (result, obj, f) -> f.set(obj, result.getByte(f.getName())));
		deserializers.put(boolean.class, (result, obj, f) -> f.set(obj, result.getBoolean(f.getName())));
		deserializers.put(float.class, (result, obj, f) -> f.set(obj, result.getFloat(f.getName())));
		deserializers.put(double.class, (result, obj, f) -> f.set(obj, result.getDouble(f.getName())));
	}
	
	public SelectRequestHandler(Class<?> clazz, List<Field> fields)
	{
		this.clazz = clazz;
		this.fields = fields;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T handle(ResultSet result) throws SQLException
	{
        result.next();

		T obj;
		try
		{
			obj = (T)clazz.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		}
        	
		try
		{
            for (Field f : fields)
            {
            	if (deserializers.containsKey(f.getType()))
            	{
                	f.setAccessible(true);
            		deserializers.get(f.getType()).deserialize(result, obj, f); 
            	}
            }
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
			return null;
		}
        return obj;
	}

}
