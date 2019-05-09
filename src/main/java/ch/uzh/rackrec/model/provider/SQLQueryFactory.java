package ch.uzh.rackrec.model.provider;

import cc.kave.commons.model.naming.types.ITypeName;

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


    protected String getTopKCountedAPIsForKeyword(String keyword) {
        return ""
            + "WITH ContextsWithKeyword AS ("
                + SELECT_CONTEXT
                + FROM_TOKENREFERENCES
                + "WHERE Token=("
                + SELECT_ID
                + FROM_TOKENS
                + "WHERE Token=\"" + keyword + "\")"
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
    protected String getTokensForAPI(String api) {
        return ""
            + "WITH ContextsWithApi AS ("
                + SELECT_CONTEXT
                + FROM_APIREFERENCES
                + "WHERE  API=("
                + SELECT_ID
                + FROM_APIS
                + "WHERE API=\"" + api + "\")"
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

    protected String getApisFromKeywordPairQuery(String keyword1, String keyword2)  {
        return ""
            + "WITH RelevantContexts AS("
                + SELECT_CONTEXT
                + FROM_TOKENREFERENCES
                + "Where Token IN ("
                    + SELECT_ID
                    + FROM_TOKENS
                    + "where token=\""+ keyword1 + "\" OR "
                    + "token=\"" + keyword2 + "\""
                + ")"
            + "), "
            + "RelevantAPIS AS("
                + "SELECT API FROM APIReferences WHERE Context IN RelevantContexts"
            + ")"
            + "SELECT API From apis Where ID IN RelevantAPIS";
    }

    protected String getCountedNeighborTokens(String token) {
        return ""
                + "SELECT token, SUM(count) "
                + "FROM tokenreferences "
                + "WHERE context IN("
                    + SELECT_CONTEXT
                    + "FROM tokenreferences "
                    + "WHERE token=("
                        + "SELECT id "
                        + FROM_TOKENS
                        + "WHERE token=\""+ token +"\""
                    + ")"
                + ") GROUP BY token";
    }

    protected String getAPICountForContext(String context, String api) {
        return ""
            + "SELECT Count "
            + FROM_APIREFERENCES
            + "WHERE Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")"
            + "AND "
            + "API=("
                + SELECT_ID
                + FROM_APIS
                + "WHERE  API=\""+ api +"\""
            + ")";
    }

    protected String getTokenReferences (String token, ITypeName context) {
        return ""
            + "SELECT * "
            + FROM_TOKENREFERENCES
            + "WHERE Token=("
                + SELECT_ID
                + FROM_TOKENS
                + "WHERE Token=\""+ token +"\""
            + ")"
            + "AND Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context=\""+ context +"\""
            + ")";
    }

    protected String getAPIReferences(String api, ITypeName context) {
        return ""
            + "SELECT * "
            + FROM_APIREFERENCES
            + "WHERE   API=("
                + SELECT_ID
                + FROM_APIS
                + "WHERE   API=\""+ api +"\""
            + ")"
            + "AND Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context=\""+ context +"\""
            + ")";
    }

    protected String getApisForContext(String context) {
        return ""
            + "SELECT API "
            + FROM_APIREFERENCES
            + "WHERE Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context=\""+ context +"\""
            + ")";
    }

    protected String getTokensFromContext(String context) {
        return ""
            + "SELECT Token "
            + FROM_TOKENREFERENCES
            + "WHERE Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context=\""+ context +"\""
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

    protected String getSQLiteTable(String table) {
        return ""
            + "SELECT name "
            + "FROM sqlite_master "
            + "WHERE type='table' "
            + "AND name='" + table + "';";
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

    protected String storeContext(ITypeName name) {
        return ""
            + "INSERT OR IGNORE "
            + "INTO Contexts (Context) "
            +  "VALUES (\"" + name + "\")";
    }

    protected String storeTokens (List<String>tokens) {
        String values = tokens.stream()
                .map(token -> "(\"" + token + "\"),")
                .collect(Collectors.joining("\n"));

        values = terminateSqlStatement(values);
        return ""
            + "INSERT OR IGNORE INTO tokens (Token) VALUES "
            + values;
    }

    protected String storeAPIs(List<String> apis) {
        String values = apis.stream()
                            .map(token -> "(\"" + token + "\"),")
                            .collect(Collectors.joining("\n"));

        values = terminateSqlStatement(values);
        return ""
            + "INSERT OR IGNORE INTO apis (API) VALUES "
            + values;
    }

    protected String storeNewTokenContextReference(String token, ITypeName context) {
        return ""
            + "INSERT INTO TokenReferences (Token, Context, Count) VALUES ("
            + "(SELECT ID FROM tokens WHERE Token=\""+ token +"\"), "
            + "(SELECT ID FROM contexts WHERE Context=\""+ context +"\"),"
            + " 1)";
    }

    protected String storeNewAPIContextReference (String api, ITypeName context) {
        return ""
            + "INSERT INTO APIReferences "
            + "(API, Context, Count) VALUES ("
                + "(SELECT ID FROM apis WHERE API=\"" + api + "\"), "
                + "(SELECT ID FROM contexts WHERE CONTEXT=\"" + context + "\"),"
                + "1"
            + ")";
    }

    protected String incrementCounterOfAPIReference(String api, ITypeName context) {
        return ""
            + "UPDATE APIReferences "
            + "SET Count = Count + 1 "
            + "WHERE  API=("
                + SELECT_ID
                + FROM_APIS
                + "WHERE    API=\""+ api +"\""
            + ")"
            + "AND Context=("
                + SELECT_ID
                + FROM_CONTEXTS
                + "WHERE Context=\""+ context +"\""
            + ")";
    }
    protected String incrementCounterOfTokenRefernce(String token, ITypeName context) {
        return ""
            + "UPDATE TokenReferences "
            + "SET Count = Count + 1 "
            + "WHERE Token=(SELECT ID FROM tokens WHERE Token=\""+ token +"\")"
            + "AND Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")";
    }

    private String terminateSqlStatement(String statement) {
        return replaceLastChar(statement, ';');
    }

    private String replaceLastChar(String target, Character newChar) {
        return target.substring(0, target.length()-1) + newChar;
    }
}
