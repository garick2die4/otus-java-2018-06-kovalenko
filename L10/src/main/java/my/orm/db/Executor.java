package my.orm.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Executor
{
	private final Connection connection;
	
	public Executor(Connection connection)
	{
		this.connection = connection;
	}

    public int execUpdate(String update) throws SQLException
    {
        try (Statement stmt = connection.createStatement())
        {
            stmt.execute(update);
            return stmt.getUpdateCount();
        }
    }
	
    public <T> T execQuery(String query, ResultHandler<T> handler) throws SQLException
    {
        try(Statement stmt = connection.createStatement())
        {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        }
    }
}
