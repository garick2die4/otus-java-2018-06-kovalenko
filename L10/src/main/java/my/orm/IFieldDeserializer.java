package my.orm;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface IFieldDeserializer
{
	public void deserialize(ResultSet result, Object obj, Field field) throws SQLException, IllegalArgumentException, IllegalAccessException;
}
