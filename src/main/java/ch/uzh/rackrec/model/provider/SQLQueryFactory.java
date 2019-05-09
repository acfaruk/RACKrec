package ch.uzh.rackrec.model.provider;

import java.util.List;
import java.util.stream.Collectors;

public class SQLQueryFactory {
    private final String SELECT_CONTEXT = "SELECT Context ";
    private final String FROM_TOKENREFERENCES = "FROM TokenReferences ";
    private final String SELECT_ID = "SELECT ID ";
    private final String FROM_TOKENS = "FROM Tokens ";
    private final String FROM_APIREFERENCES = "FROM APIReferences ";
    private final String FROM_APIS = "FROM apis ";
    private final String FROM_CONTEXTS = "FROM contexts ";
    private final String ID = "ID INTEGER PRIMARY KEY AUTOINCREMENT,";


    protected String getTopKCountedAPIsForKeyword() {
        return ""
            + "WITH ContextsWithKeyword AS ("
                + SELECT_CONTEXT
                + FROM_TOKENREFERENCES
                + "WHERE Token=("
                + SELECT_ID
                + FROM_TOKENS
                + "WHERE Token= ? )"
            + "),"
            + "APIReferencesWithKeyword AS ("
                + "SELECT API as ReferencedAPI, Count "
                + FROM_APIREFERENCES
                + "WHERE Context IN ContextsWithKeyword"
            + ")"
            + "SELECT API, SUM(COUNT) "
            + FROM_APIS
            + "JOIN APIReferencesWithKeyword "
            + "ON apis.ID=APIReferencesWithKeyword.ReferencedAPI "
            + "GROUP BY API "
            + "ORDER BY SUM(COUNT) DESC";
    }
    protected String getTokensForAPI() {
        return ""
            + "WITH ContextsWithApi AS ("
                + SELECT_CONTEXT
                + FROM_APIREFERENCES
                + "WHERE  API=("
                + SELECT_ID
                + FROM_APIS
                + "WHERE API= ? )"
            + "),"
            + "TokenReferencesWithApi AS ("
                + "SELECT Token "
                + FROM_TOKENREFERENCES
                + "WHERE Context IN ContextsWithApi"
            + ")"
            + "SELECT Token "
            + FROM_TOKENS
            + "WHERE ID IN TokenReferencesWithApi";
    }

    protected String getApisFromKeywordPairQuery()  {
        return ""
            + "WITH RelevantContexts AS("
                + SELECT_CONTEXT
                + FROM_TOKENREFERENCES
                + "Where Token IN ("
                    + SELECT_ID
                    + FROM_TOKENS
                    + "where token= ? OR "
                    + "token= ?"
                + ")"
            + "), "
            + "RelevantAPIS AS("
                + "SELECT API FROM APIReferences WHERE Context IN RelevantContexts"
            + ")"
            + "SELECT API From apis Where ID IN RelevantAPIS";
    }

    protected String getCountedNeighborTokens() {
        return ""
                + "SELECT token, SUM(count) "
                + "FROM tokenreferences "
                + "WHERE context IN("
                    + SELECT_CONTEXT
                    + "FROM tokenreferences "
                    + "WHERE token=("
                        + "SELECT id "
                        + FROM_TOKENS
                        + "WHERE token= ?"
                    + ")"
                + ") GROUP BY token";
    }

    protected String getAPICountForContext() {
        return ""
            + "SELECT Count "
            + FROM_APIREFERENCES
            + "WHERE Context=(SELECT ID FROM contexts WHERE Context= ?)"
            + "AND "
            + "API=("
                + SELECT_ID
                + FROM_APIS
                + "WHERE  API= ?"
            + ")";
    }

    protected String getTokenReferences () {
        return ""
            + "SELECT * "
            + FROM_TOKENREFERENCES
            + "WHERE Token=("
                + SELECT_ID
                + FROM_TOKENS
                + "WHERE Token= ?"
            + ")"
            + "AND Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context= ?"
            + ")";
    }

    protected String getAPIReferences() {
        return ""
            + "SELECT * "
            + FROM_APIREFERENCES
            + "WHERE   API=("
                + SELECT_ID
                + FROM_APIS
                + "WHERE API= ?"
            + ")"
            + "AND Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context= ?"
            + ")";
    }

    protected String getApisForContext() {
        return ""
            + "SELECT API "
            + FROM_APIREFERENCES
            + "WHERE Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context= ?"
            + ")";
    }

    protected String getTokensFromContext() {
        return ""
            + "SELECT Token "
            + FROM_TOKENREFERENCES
            + "WHERE Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context= ?"
            + ")";
    }

    protected  String getAllTokens() {
        return ""
            + "SELECT Token from tokens";
    }

    protected String getAllContexts() {
        return ""
            + "SELECT Context from Contexts";
    }

    protected String getAllAPIs() {
        return ""
            + "SELECT API from apis";
    }

    protected String getSQLiteTable() {
        return ""
            + "SELECT name "
            + "FROM sqlite_master "
            + "WHERE type='table' "
            + "AND name= ?";
    }

    protected String createContextTable() {
        return ""
            + "CREATE TABLE contexts("
                + ID
                + "Context varchar(255) NOT NULL"
            + ")";
    }

    protected String createAPITable() {
        return ""
            + "CREATE TABLE apis("
                + ID
                + "API varchar(255) NOT NULL UNIQUE"
            + ")";
    }

    protected String createTokenTable() {
        return ""
            + "CREATE TABLE tokens("
                + ID
                + "Token varchar(255) NOT NULL UNIQUE"
            + ")";
    }

    protected String createAPIReferenceTable() {
        return  ""
            + "CREATE TABLE APIReferences("
                + "API int,"
                + "Context int,"
                + "Count int,"
                + "FOREIGN KEY (API) REFERENCES apis(ID),"
                + "FOREIGN KEY (Context) REFERENCES contexts(ID)"
            + ")";
    }

    protected String createTokenReferenceTable() {
        return  ""
            + "CREATE TABLE TokenReferences("
                + "Token int,"
                + "Context int,"
                + "Count int,"
                + "FOREIGN KEY (Token) REFERENCES tokens(ID),"
                + "FOREIGN KEY (Context) REFERENCES contexts(ID)"
            + ")";
    }

    protected String storeContext() {
        return ""
            + "INSERT OR IGNORE "
            + "INTO Contexts (Context) "
            + "VALUES (?)";
    }

    // TODO port to batch
    protected String storeTokens () {
        return ""
            + "INSERT OR IGNORE "
            + "INTO tokens (Token) "
            + "VALUES (?)";
    }

    // TODO port to batch
    protected String storeAPIs() {
        return ""
            + "INSERT OR IGNORE "
            + "INTO apis (API) "
            + "VALUES (?)";
    }

    protected String storeNewTokenContextReference() {
        return ""
            + "INSERT INTO TokenReferences (Token, Context, Count) VALUES ("
            + "(SELECT ID FROM tokens WHERE Token= ?), "
            + "(SELECT ID FROM contexts WHERE Context= ?),"
            + " 1)";
    }

    protected String storeNewAPIContextReference() {
        return ""
            + "INSERT INTO APIReferences "
            + "(API, Context, Count) VALUES ("
                + "(SELECT ID FROM apis WHERE API= ?), "
                + "(SELECT ID FROM contexts WHERE CONTEXT= ?),"
                + "1"
            + ")";
    }

    protected String incrementCounterOfAPIReference() {
        return ""
            + "UPDATE APIReferences "
            + "SET Count = Count + 1 "
            + "WHERE  API=("
                + SELECT_ID
                + FROM_APIS
                + "WHERE    API= ?"
            + ")"
            + "AND Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context= ?"
            + ")";
    }
    protected String incrementCounterOfTokenRefernce() {
        return ""
            + "UPDATE TokenReferences "
            + "SET Count = Count + 1 "
            + "WHERE Token=(SELECT ID FROM tokens WHERE Token= ?)"
            + "AND Context=(SELECT ID FROM contexts WHERE Context= ?)";
    }

    private String terminateSqlStatement(String statement) {
        return replaceLastChar(statement, ';');
    }

    private String replaceLastChar(String target, Character newChar) {
        return target.substring(0, target.length()-1) + newChar;
    }
}
