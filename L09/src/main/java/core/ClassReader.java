package core;

import java.lang.reflect.Field;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import serialization.JsonProperty;
import serialization.JsonSerializable;

public class ClassReader
{
	public static void save(StringBuilder stringBuilder, Object object) throws IllegalArgumentException, IllegalAccessException
	{
		Class<?> objectClass = object.getClass();
		if (objectClass.getAnnotation(JsonSerializable.class) == null)
			return;
		
		JsonObjectBuilder builder = Json.createObjectBuilder()
				.add("type", objectClass.getName());

		JsonArrayBuilder fieldsBuidler = Json.createArrayBuilder();
		for (Field field : objectClass.getDeclaredFields())
		{
			if (field.getAnnotation(JsonProperty.class) == null)
				continue;

			Class<?> fieldClass = field.getType();
			if (fieldClass.equals(String.class))
			{
				field.setAccessible(true);
				fieldsBuidler.add(Json.createObjectBuilder()
					.add("type", fieldClass.getName())
					.add("name", field.getName())
					.add("value", (String)field.get(object))
					.build());
				
			}
			else
			{
				save(stringBuilder, field.get(object));
			}
		}
		builder.add("fields", fieldsBuidler.build());
		JsonObject model = builder.build();
		stringBuilder.append(model.toString());
	}
}
