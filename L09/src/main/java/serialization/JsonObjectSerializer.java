package serialization;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import serialization.internal.IFieldSerializer;
import serialization.internal.PrimitiveTypeFieldSerializer;
import serialization.internal.SerializationErrorException;

public class JsonObjectSerializer
{
	private static final Map<Integer, IFieldSerializer> serializers = new HashMap<>();
	
	static
	{
		serializers.put(new Integer(int.class.hashCode()),
				new PrimitiveTypeFieldSerializer((b, o, f) -> b.add("value", f.getInt(o))));
		
		serializers.put(new Integer(long.class.hashCode()),
				new PrimitiveTypeFieldSerializer((b, o, f) -> b.add("value", f.getLong(o))));
		
		serializers.put(new Integer(float.class.hashCode()),
				new PrimitiveTypeFieldSerializer((b, o, f) -> b.add("value", f.getFloat(o))));

		serializers.put(new Integer(double.class.hashCode()),
				new PrimitiveTypeFieldSerializer((b, o, f) -> b.add("value", f.getDouble(o))));
		
		serializers.put(new Integer(boolean.class.hashCode()),
				new PrimitiveTypeFieldSerializer((b, o, f) -> b.add("value", f.getBoolean(o))));

		serializers.put(new Integer(short.class.hashCode()),
				new PrimitiveTypeFieldSerializer((b, o, f) -> b.add("value", f.getShort(o))));
		
		serializers.put(new Integer(byte.class.hashCode()),
				new PrimitiveTypeFieldSerializer((b, o, f) -> b.add("value", f.getByte(o))));

		serializers.put(new Integer(char.class.hashCode()),
				new PrimitiveTypeFieldSerializer((b, o, f) -> b.add("value", f.getChar(o))));
		
		IFieldSerializer simpleSerializer = (object, f) -> {
			Class<?> clazz = object.getClass();
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
			jsonBuilder.add("type", clazz.getName());
			jsonBuilder.add("value", object.toString());
			return jsonBuilder;		
		};
		serializers.put(new Integer(String.class.hashCode()), simpleSerializer);
		serializers.put(new Integer(Integer.class.hashCode()), simpleSerializer);
		serializers.put(new Integer(Number.class.hashCode()), simpleSerializer);
		serializers.put(new Integer(Double.class.hashCode()), simpleSerializer);
		serializers.put(new Integer(Boolean.class.hashCode()), simpleSerializer);
		
		IFieldSerializer arraySerializer = (object, f) -> {
			Class<?> clazz = object.getClass();
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
			ArrayList<?> list = (ArrayList<?>)object;
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (Object o : list)
			{
				JsonObjectBuilder childJsonBuilder = saveField(o);
				arrayBuilder.add(childJsonBuilder.build());
			}
			jsonBuilder.add("type", clazz.getName());
			jsonBuilder.add("value", arrayBuilder.build());
			return jsonBuilder;
		};
		serializers.put(new Integer(ArrayList.class.hashCode()), arraySerializer);
		
		IFieldSerializer hashMapSerializer = (object, f) ->
		{
			Class<?> clazz = object.getClass();
			JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
			HashMap<?,?> m = (HashMap<?,?>)object;
			JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
			for (HashMap.Entry<?,?> entry : m.entrySet())
			{
				JsonObjectBuilder pairBuilder = Json.createObjectBuilder();
				
				JsonObjectBuilder keyJsonBuilder = saveField(entry.getKey());
				pairBuilder.add("key", keyJsonBuilder.build());
				
				JsonObjectBuilder valueBuilder = saveField(entry.getValue());
				pairBuilder.add("value", valueBuilder.build());
				
				arrayBuilder.add(pairBuilder.build());
			}
			jsonBuilder.add("type", clazz.getName());
			jsonBuilder.add("value", arrayBuilder.build());
			return jsonBuilder;
		};
		serializers.put(new Integer(HashMap.class.hashCode()), hashMapSerializer);
	}
	
	public static String save(Object object) throws SerializationErrorException
	{	
		JsonObjectBuilder jsonBuilder = null;
		try
		{
			jsonBuilder = saveObject(object);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			throw new SerializationErrorException(); 
		}
		if (jsonBuilder == null)
			return "";
		
		return jsonBuilder.build().toString();
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
		if (!serializers.containsKey(field.getType().hashCode()))
			return null;
		
		IFieldSerializer s = serializers.get(field.getType().hashCode());
		return s.serialize(object, field);
	}
	
	private static JsonObjectBuilder saveField(Object object) throws IllegalArgumentException, IllegalAccessException
	{
		Class<?> clazz = object.getClass();
		
		if (!serializers.containsKey(clazz.hashCode()))
			return saveObject(object);
		
		IFieldSerializer s = serializers.get(clazz.hashCode());
		return s.serialize(object, null);
	}
}
