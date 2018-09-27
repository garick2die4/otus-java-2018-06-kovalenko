package serialization.internal;

import java.lang.reflect.Field;

import javax.json.JsonObjectBuilder;

public interface ISerializeFunction
{
	void serialize(JsonObjectBuilder builder, Object obj, Field field) throws IllegalArgumentException, IllegalAccessException;
}
