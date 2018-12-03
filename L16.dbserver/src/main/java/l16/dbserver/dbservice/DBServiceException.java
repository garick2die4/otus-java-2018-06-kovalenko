package l16.dbserver.dbservice;

public class DBServiceException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DBServiceException(Throwable cause)
	{
		super(cause);
	}

	public DBServiceException(String message)
	{
		super(message);
	}
}
