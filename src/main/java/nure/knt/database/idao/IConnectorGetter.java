package nure.knt.database.idao;

import java.sql.SQLException;


public interface IConnectorGetter {
    public java.sql.Statement getSqlStatement() throws SQLException;
    public java.sql.PreparedStatement getSqlPreparedStatement(String script) throws SQLException;
}
