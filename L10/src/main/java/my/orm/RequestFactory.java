package my.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import my.orm.annotations.OrmAutoIncrement;
import my.orm.annotations.OrmPrimaryKey;
import my.orm.annotations.OrmTable;

/**
 * Класс для генерации SQL-запросов по классу
 */
public final class RequestFactory
{
	private static final String INSERT_FORMAT = "INSERT INTO %s (%s) VALUES(%s)";
	
	private static final String SELECT_FORMAT = "SELECT %s FROM %s";
	private static final String WHERE_FORMAT = "WHERE %s = %s";

	private static final String CREATE_TABLE_FORMAT = "CREATE TABLE IF NOT EXISTS %s (%s)";

    private static final String DELETE_FORMAT = "DROP TABLE %s";

    @SuppressWarnings("rawtypes")
	private static final Map<Class, String> fieldTypes = new HashMap<>();
    
    private RequestFactory()
    {
    	
    }
    
    static
    {
    	fieldTypes.put(String.class, "VARCHAR(256)");		
    	fieldTypes.put(long.class, "BIGINT");
    	fieldTypes.put(int.class, "INT");
		fieldTypes.put(short.class, "SMALLINT");
		fieldTypes.put(byte.class, "TINYINT");
		fieldTypes.put(boolean.class, "TINYINT(1)");
		fieldTypes.put(float.class, "FLOAT");
		fieldTypes.put(double.class, "DOUBLE");
    }
    
	public static <T> String createInsertRequest(T obj) throws IllegalArgumentException, IllegalAccessException
	{
		String tableName = getTableName(obj.getClass());
		if (tableName.isEmpty())
			return "";
		
		List<Field> fields = Arrays.asList(obj.getClass().getDeclaredFields());
		String columnNames = fields.stream().map(f -> f.getName()).collect(Collectors.joining(","));

		List<String> valuesArray = new ArrayList<>();
		for (Field f : fields)
		{
			f.setAccessible(true);
			if (f.getType().equals(String.class))
				valuesArray.add('\'' + f.get(obj).toString() + '\'');
			else
				valuesArray.add(f.get(obj).toString());
		}
		String values =  valuesArray.stream().collect(Collectors.joining(","));		
		
		return String.format(INSERT_FORMAT, tableName, columnNames, values);
	}
	
	public static <T> String createSelectRequest(Class<?> clazz)
	{
		String tableName = getTableName(clazz);
		if (tableName.isEmpty())
			return "";
		
		List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		
		String columnNames = fields.stream().map(f -> f.getName()).collect(Collectors.joining(","));
		
		return String.format(SELECT_FORMAT, columnNames, tableName);		
	}
	
	public static <T> String createSelectWhereRequest(Class<?> clazz, String field, String value)
	{
		String query = createSelectRequest(clazz) + " " + String.format(WHERE_FORMAT, field, value);
		return query;		
	}
	
	public static <T> String createCreateTableRequest(Class<?> clazz)
	{
		String tableName = getTableName(clazz);
		if (tableName.isEmpty())
			return "";
		
		List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getSuperclass().getDeclaredFields()));
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		
		List<String> primaryKeys = new ArrayList<>();
		List<String> fieldsArray = new ArrayList<>();
		for (Field f : fields)
		{
			StringBuilder fieldBuilder = new StringBuilder();
			fieldBuilder.append(f.getName());
			if (!fieldTypes.containsKey(f.getType()))
				return "";
			fieldBuilder.append(" ");
			fieldBuilder.append(fieldTypes.get(f.getType()));
			OrmAutoIncrement[] ai = f.getAnnotationsByType(OrmAutoIncrement.class);
			if (ai.length != 0)
			{
				fieldBuilder.append(" ");
				fieldBuilder.append("AUTO_INCREMENT");				
			}
			OrmPrimaryKey[] pk = f.getAnnotationsByType(OrmPrimaryKey.class);
			if (pk.length != 0)
			{
				primaryKeys.add(f.getName());
			}
			fieldsArray.add(fieldBuilder.toString());
		}
		
		String primaryStr = primaryKeys.stream().collect(Collectors.joining(","));
		if (!primaryStr.isEmpty())
		{
			StringBuilder fieldBuilder = new StringBuilder();
			fieldBuilder.append("PRIMARY KEY(");
			fieldBuilder.append(primaryStr);
			fieldBuilder.append(")");
			fieldsArray.add(fieldBuilder.toString());
		}
		String fieldsStr = fieldsArray.stream().collect(Collectors.joining(","));
		return String.format(CREATE_TABLE_FORMAT, tableName, fieldsStr);
	}
	
	public static <T> String createDeleteTableRequest(Class<?> clazz)
	{
		String tableName = getTableName(clazz);
		return String.format(DELETE_FORMAT, tableName);
	}
	
	private static String getTableName(Class<?> clazz)
	{
		OrmTable[] tables = clazz.getAnnotationsByType(OrmTable.class);
		if (tables.length == 0)
			return "";
		return tables[0].tableName();
	}

}
