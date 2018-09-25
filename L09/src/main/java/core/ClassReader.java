package core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import serialization.JsonProperty;
import serialization.JsonSerializable;

public class ClassReader
{
	public static String save(Object object) throws IllegalArgumentException, IllegalAccessException
	{	
		JsonObjectBuilder jsonBuilder = saveObject(object);
		if (jsonBuilder == null)
			return "";
		
		JsonObject model = jsonBuilder.build();
		return model.toString();
	}
	
	private static JsonObjectBuilder saveObject(Object object) throws IllegalArgumentException, IllegalAccessException
	{
		if (object.getClass().getAnnotation(JsonSerializable.class) == null)
			return null;
		
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		Class<?> objectClass = object.getClass();
		jsonBuilder.add("type", objectClass.getName());
		
		JsonArrayBuilder fieldsBuidler = Json.createArrayBuilder();
		for (Field field : objectClass.getDeclaredFields())
		{
			if (field.getAnnotation(JsonProperty.class) == null)
				continue;

			field.setAccessible(true);
			
			JsonObjectBuilder objectBuilder = field.getType().isPrimitive() ?
					savePrimitiveField(object, field) :
					saveField(field.get(object));	
			objectBuilder.add("name", field.getName());
			fieldsBuidler.add(objectBuilder.build());
		}
		jsonBuilder.add("fields", fieldsBuidler.build());
		return jsonBuilder;
	}
	
	private static JsonObjectBuilder savePrimitiveField(Object object, Field field) throws IllegalArgumentException, IllegalAccessException
	{
		if (field.getType().equals(int.class))
		{
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
			jsonBuilder.add("type", field.getType().getName());
			jsonBuilder.add("value", field.getInt(object));
			return jsonBuilder;	
		}
		return null;
	}
	
	private static JsonObjectBuilder saveField(Object object) throws IllegalArgumentException, IllegalAccessException
	{
		if (object.getClass().equals(String.class))
		{
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
			jsonBuilder.add("type", object.getClass().getName());
			jsonBuilder.add("value", (String)object);
			return jsonBuilder;
		}
		else if (object.getClass().equals(Integer.class))
		{
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
			jsonBuilder.add("type", object.getClass().getName());
			jsonBuilder.add("value", object.toString());
			return jsonBuilder;		
		}
		else if (object.getClass().equals(ArrayList.class))
		{
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
			ArrayList<?> list = (ArrayList<?>)object;
			JsonArrayBuilder arrysBuilder = Json.createArrayBuilder();
			for (Object o : list)
			{
				JsonObjectBuilder childJsonBuilder = saveField(o);
				arrysBuilder.add(childJsonBuilder.build());
			}
			jsonBuilder.add("type", object.getClass().getName());
			jsonBuilder.add("value", arrysBuilder.build());
			return jsonBuilder;
		}
		else if (object.getClass().equals(Map.class))
		{
			return null;
		}
		else
		{
			
			JsonObjectBuilder childBuilder = saveObject(object);
			if (childBuilder == null)
				return null;
		
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
			jsonBuilder.add("value", childBuilder.build());
			return jsonBuilder;
		}
	}
}
