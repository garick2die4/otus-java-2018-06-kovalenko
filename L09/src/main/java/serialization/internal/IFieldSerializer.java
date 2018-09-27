package serialization.internal;

import java.lang.reflect.Field;

import javax.json.JsonObjectBuilder;

public interface IFieldSerializer
{
	JsonObjectBuilder serialize(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException;
}
