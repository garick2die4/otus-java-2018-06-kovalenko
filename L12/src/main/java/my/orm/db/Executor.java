package my.orm.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public <T> ResultObject<T> execInsert(String update, ResultHandler<T> handler) throws SQLException
    {
        try (PreparedStatement stmt = connection.prepareStatement(update, java.sql.Statement.RETURN_GENERATED_KEYS))
        {
            stmt.execute();
            ResultSet result = stmt.getGeneratedKeys();
            return handler.handle(result);
        }
    }

    public int execUpdate(String update) throws SQLException
    {
        try (PreparedStatement stmt = connection.prepareStatement(update))
        {
            stmt.execute();
            return stmt.getUpdateCount();
        }
    }
	
    public <T> ResultObject<T> execQuery(String query, ResultHandler<T> handler) throws SQLException
    {
        try(PreparedStatement stmt = connection.prepareStatement(query))
        {
            stmt.execute();
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        }
    }
}
