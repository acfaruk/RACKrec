package ch.uzh.rackrec.model.provider;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import ch.uzh.rackrec.model.view.KAC;
import ch.uzh.rackrec.model.view.KKC;

public interface IDatabaseProvider {
    void prepareSchemas() throws SQLException;
    void closeConnection();
    List<String> getTokensForAPI(String api) throws SQLException;
    KAC getTopKAPIForToken(int k, String keyword) throws SQLException;
    boolean saveMinedContext(ModelEntry modelEntry) throws SQLException;
    boolean tableExists(String tableName) throws SQLException;
	double getKKCScore(Map.Entry<String, String> keywordPair) throws SQLException;
}
