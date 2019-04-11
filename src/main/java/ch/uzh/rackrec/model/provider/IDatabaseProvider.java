package ch.uzh.rackrec.model.provider;

import java.sql.SQLException;
import java.util.List;

public interface IDatabaseProvider {
    void prepareSchemas() throws SQLException;
    void closeConnection();
    List<String> getTokensForAPI(String api) throws SQLException;
    List<String> getTopKAPIForToken(int k, String keyword);
    boolean saveMinedContext(ModelEntry modelEntry) throws SQLException;
    boolean tableExists(String tableName) throws SQLException;
}
