package ch.uzh.rackrec.model.provider;

import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import ch.uzh.rackrec.model.view.KAC;

public interface IDatabaseProvider {
    void prepareSchemas() throws SQLException;
    void closeConnection();
    List<String> getTokensForAPI(String api) throws SQLException;
    KAC getTopKAPIForToken(int k, String keyword) throws SQLException;
    boolean saveMinedContext(ModelEntry modelEntry) throws SQLException;
    boolean tableExists(String tableName) throws SQLException;
}
