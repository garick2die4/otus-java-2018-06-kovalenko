package l16.dbserver.dbservice;

import l16.dbserver.datasets.UserDataSet;

public interface IDBService extends AutoCloseable
{
	String getMetaData();
	
	void prepare() throws DBServiceException;
	
	void save(UserDataSet user) throws DBServiceException;
	
	UserDataSet load(long id) throws DBServiceException;
	
	int usersCount() throws DBServiceException;
	
	void shutdown() throws DBServiceException;
}
