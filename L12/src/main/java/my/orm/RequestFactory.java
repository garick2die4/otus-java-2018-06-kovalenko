package my.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Класс для генерации SQL-запросов по классу
 */
public final class RequestFactory
{
	private static final String INSERT_FORMAT = "INSERT INTO %s (%s) VALUES(%s)";
	
	private static final String SELECT_FORMAT = "SELECT %s FROM %s";
	private static final String WHERE_FORMAT = "WHERE %s = %s";

	private static final String CREATE_TABLE_FORMAT = "CREATE TABLE IF NOT EXISTS %s (%s)";

    private static final String DELETE_FORMAT = "DROP TABLE IF EXISTS %s";

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
		String columnNames = fields.stream()
				.filter(f -> {
					if (fieldTypes.containsKey(f.getType()))
						return true;
					OneToOne[] oos = f.getAnnotationsByType(OneToOne.class);
					return oos.length != 0;
				})
				.map(f -> f.getName())
				.collect(Collectors.joining(","));

		List<String> valuesArray = new ArrayList<>();
		for (Field f : fields)
		{
			f.setAccessible(true);
			if (f.getType().equals(String.class))
				valuesArray.add('\'' + f.get(obj).toString() + '\'');
			else
			{
				OneToOne[] oos = f.getAnnotationsByType(OneToOne.class);
				if (oos.length == 0)
				{
					if (!fieldTypes.containsKey(f.getType()))
						continue;
					valuesArray.add(f.get(obj).toString());
				}
				else
				{
					Object subObj = f.get(obj);
					Class<?> fieldClazz = f.getType(); 
					List<Field> subFields = new ArrayList<>(Arrays.asList(fieldClazz.getSuperclass().getDeclaredFields()));
					subFields.addAll(Arrays.asList(fieldClazz.getDeclaredFields()));
					for (Field sf : subFields)
					{
						if (!fieldTypes.containsKey(sf.getType()))
							continue;

						Id[] pk = sf.getAnnotationsByType(Id.class);
						if (pk.length == 0)
							continue;
						
						sf.setAccessible(true);
						valuesArray.add(sf.get(subObj).toString());
						break;
					}
				}
			}
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
		
		String columnNames = fields.stream()
				.filter(f -> {
					if (fieldTypes.containsKey(f.getType()))
						return true;
					OneToOne[] oos = f.getAnnotationsByType(OneToOne.class);
					return oos.length != 0;
				})
				.map(f -> f.getName()).collect(Collectors.joining(","));
		
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
			if (fieldTypes.containsKey(f.getType()))
			{
				fieldBuilder.append(" ");
				fieldBuilder.append(fieldTypes.get(f.getType()));
				Id[] pk = f.getAnnotationsByType(Id.class);
				if (pk.length != 0)
				{
					fieldBuilder.append(" ");
					fieldBuilder.append("AUTO_INCREMENT");				
					
					primaryKeys.add(f.getName());
				}
			}
			else
			{
				OneToOne[] oos = f.getAnnotationsByType(OneToOne.class);
				if (oos.length == 0)
					continue;
				Class<?> fieldClazz = f.getType(); 
				List<Field> subFields = new ArrayList<>(Arrays.asList(fieldClazz.getSuperclass().getDeclaredFields()));
				subFields.addAll(Arrays.asList(fieldClazz.getDeclaredFields()));
				for (Field sf : subFields)
				{
					if (!fieldTypes.containsKey(sf.getType()))
						continue;

					Id[] pk = sf.getAnnotationsByType(Id.class);
					if (pk.length == 0)
						continue;
					
					fieldBuilder.append(" ");
					fieldBuilder.append(fieldTypes.get(sf.getType()));
					break;
				}
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
	
	public static <T> String createDropTableRequest(Class<?> clazz)
	{
		String tableName = getTableName(clazz);
		return String.format(DELETE_FORMAT, tableName);
	}
	
	private static String getTableName(Class<?> clazz)
	{
		Entity[] entity = clazz.getAnnotationsByType(Entity.class);
		if (entity.length == 0)
			return "";
		
		Table[] tables = clazz.getAnnotationsByType(Table.class);
		if (tables.length == 0)
			return "";
		return tables[0].name();
	}

}
