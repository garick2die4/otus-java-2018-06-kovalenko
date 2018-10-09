package app;

import my.orm.datasets.UserDataSet;

public interface IDBService extends AutoCloseable
{
	String getMetaData();
	
	void prepare() throws DBServiceException;
	
	void save(UserDataSet user) throws DBServiceException;
	
	UserDataSet load(long id) throws DBServiceException;
	
	void shutdown() throws DBServiceException;
}
