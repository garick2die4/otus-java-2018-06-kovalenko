package my.orm.db;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultHandler<T>
{
	ResultObject<T> handle(ResultSet result) throws SQLException;
}
