package serialization.internal;

import java.lang.reflect.Field;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

public class PrimitiveTypeFieldSerializer implements IFieldSerializer
{
	private final ISerializeFunction func;
	
	public PrimitiveTypeFieldSerializer(ISerializeFunction func)
	{
		this.func = func;
	}
	
	public JsonObjectBuilder serialize(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException
	{
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		jsonBuilder.add("type", field.getType().getName());
		func.serialize(jsonBuilder, obj, field);
		return jsonBuilder;
	}
}
