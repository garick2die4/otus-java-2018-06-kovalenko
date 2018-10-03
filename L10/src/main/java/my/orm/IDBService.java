package my.orm;

import java.sql.SQLException;

public interface IDBService<T extends DataSet> extends AutoCloseable
{
	String getMetaData();
	
	void prepareTables() throws SQLException;
	
	void save(T user) throws SQLException;
	
	T load(long id) throws SQLException;
	
	void deleteTable() throws SQLException;
}
