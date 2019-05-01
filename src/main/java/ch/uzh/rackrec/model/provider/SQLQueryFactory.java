package ch.uzh.rackrec.model.provider;

import cc.kave.commons.model.naming.types.ITypeName;

import java.util.List;
import java.util.stream.Collectors;

public class SQLQueryFactory {
    protected String getTopKCountedAPIsForKeyword(String keyword) {
        String query = ""
            + "WITH ContextsWithKeyword AS ("
                + "SELECT Context "
                + "FROM TokenReferences "
                + "WHERE Token=("
                + "SELECT ID "
                + "FROM tokens "
                + "WHERE Token=\"" + keyword + "\")"
            + "),"
            + "APIReferencesWithKeyword AS ("
                + "SELECT API as ReferencedAPI, Count "
                + "FROM APIReferences "
                + "WHERE Context IN ContextsWithKeyword"
            + ")"
            + "SELECT API, SUM(COUNT) "
            + "FROM apis "
            + "JOIN APIReferencesWithKeyword "
            + "ON apis.ID=APIReferencesWithKeyword.ReferencedAPI "
            + "GROUP BY API "
            + "ORDER BY SUM(COUNT) DESC";

        return query;
    }
    protected String getTokensForAPI(String api) {
        String query = ""
            + "WITH ContextsWithApi AS ("
                + "SELECT Context "
                + "FROM APIReferences "
                + "WHERE API=("
                + "SELECT ID "
                + "FROM apis "
                + "WHERE API=\"" + api + "\")"
            + "),"
            + "TokenReferencesWithApi AS ("
                + "SELECT Token "
                + "FROM TokenReferences "
                + "WHERE Context IN ContextsWithApi"
            + ")"
            + "SELECT Token "
            + "FROM tokens "
            + "WHERE ID IN TokenReferencesWithApi";

        return query;
    }

    protected String getApisFromKeywordPairQuery(String keyword1, String keyword2)  {
        String getAPIQuery = ""
            + "WITH RelevantContexts AS("
                + "SELECT Context "
                + "FROM TokenReferences "
                + "Where Token IN ("
                    + "Select ID "
                    + "from tokens "
                    + "where token=\""+ keyword1 + "\" OR "
                    + "token=\"" + keyword2 + "\""
                + ")"
            + "), "
            + "RelevantAPIS AS("
                + "SELECT API FROM APIReferences WHERE Context IN RelevantContexts"
            + ")"
            + "SELECT API From apis Where ID IN RelevantAPIS";

        return getAPIQuery;
    }

    protected String getCountedNeighborTokens(String token) {
        String getTokensQuery = ""
                + "SELECT token, SUM(count) "
                + "FROM tokenreferences "
                + "WHERE context IN("
                    + "SELECT context "
                    + "FROM tokenreferences "
                    + "WHERE token=("
                        + "SELECT id "
                        + "FROM tokens "
                        + "WHERE token=\""+ token +"\""
                    + ")"
                + ") GROUP BY token";

        return getTokensQuery;
    }

    protected String getAPICountForContext(String context, String api) {
        String getCountedAPIsQuery = ""
            + "SELECT Count "
            + "FROM APIReferences "
            + "WHERE Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")"
            + "AND "
            + "API=("
                + "SELECT ID "
                + "FROM apis "
                + "WHERE API=\""+ api +"\""
            + ")";

        return getCountedAPIsQuery;
    }

    protected String getTokenReferences (String token, ITypeName context) {
        String query = ""
            + "SELECT * "
            + "FROM TokenReferences "
            + "WHERE Token=("
                + "SELECT ID "
                + "FROM tokens "
                + "WHERE Token=\""+ token +"\""
            + ")"
            + "AND Context=("
                + "SELECT ID "
                + "FROM contexts "
                + "WHERE Context=\""+ context +"\""
            + ")";

        return query;
    }

    protected String getAPIReferences(String api, ITypeName context) {
        String query = ""
            + "SELECT * "
            + "FROM APIReferences "
            + "WHERE API=("
                + "SELECT ID "
                + "FROM apis "
                + "WHERE API=\""+ api +"\""
            + ")"
            + "AND Context=("
                + "SELECT ID "
                + "FROM contexts "
                + "WHERE Context=\""+ context +"\""
            + ")";

        return query;
    }

    protected String getApisForContext(String context) {
        String getApisQuery = ""
            + "SELECT API "
            + "FROM APIReferences "
            + "WHERE Context=("
                + "SELECT ID "
                + "FROM contexts "
                + "WHERE Context=\""+ context +"\""
            + ")";

        return getApisQuery;
    }

    protected String getTokensFromContext(String context) {
        String getTokensQuery = ""
            + "SELECT Token "
            + "FROM TokenReferences "
            + "WHERE Context=("
                + "SELECT ID "
                + "FROM contexts "
                + "WHERE Context=\""+ context +"\""
            + ")";

        return getTokensQuery;
    }

    protected  String getAllTokens() {
        String getTokensQuery = ""
            + "SELECT Token from tokens";

        return getTokensQuery;
    }

    protected String getAllContexts() {
        String getContextsQuery = ""
            + "SELECT Context from Contexts";

        return getContextsQuery;
    }

    protected String getAllAPIs() {
        String getAPIsQuery = ""
            + "SELECT API from apis";

        return getAPIsQuery;
    }

    protected String getSQLiteTable(String table) {
        String tableExistsQuery = ""
            + "SELECT name "
            + "FROM sqlite_master "
            + "WHERE type='table' "
            + "AND name='" + table + "';";

        return tableExistsQuery;
    }

    protected String createContextTable() {
        String createContextSchema = ""
            + "CREATE TABLE contexts("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Context varchar(255) NOT NULL"
            + ")";

        return createContextSchema;
    }

    protected String createAPITable() {
        String createAPISchema = ""
            + "CREATE TABLE apis("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "API varchar(255) NOT NULL UNIQUE"
            + ")";

        return createAPISchema;
    }

    protected String createTokenTable() {
        String createTokenSchema = ""
            + "CREATE TABLE tokens("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Token varchar(255) NOT NULL UNIQUE"
            + ")";

        return createTokenSchema;
    }

    protected String createAPIReferenceTable() {
        String createAPIReferenceSchema =  ""
            + "CREATE TABLE APIReferences("
                + "API int,"
                + "Context int,"
                + "Count int,"
                + "FOREIGN KEY (API) REFERENCES apis(ID),"
                + "FOREIGN KEY (Context) REFERENCES contexts(ID)"
            + ")";

        return createAPIReferenceSchema;
    }

    protected String createTokenReferenceTable() {
        String createTokenReferenceSchema =  ""
            + "CREATE TABLE TokenReferences("
                + "Token int,"
                + "Context int,"
                + "Count int,"
                + "FOREIGN KEY (Token) REFERENCES tokens(ID),"
                + "FOREIGN KEY (Context) REFERENCES contexts(ID)"
            + ")";

        return createTokenReferenceSchema;
    }

    protected String storeContext(ITypeName name) {
        String insertContext = ""
            + "INSERT OR IGNORE "
            + "INTO Contexts (Context) "
            +  "VALUES (\"" + name + "\")";

        return insertContext;
    }

    protected String storeTokens (List<String>tokens) {
        String values = tokens.stream()
                .map(token -> "(\"" + token + "\"),")
                .collect(Collectors.joining("\n"));

        values = terminateSqlStatement(values);
        String insertTokens = ""
                + "INSERT OR IGNORE INTO tokens (Token) VALUES "
                + values;

        return insertTokens;
    }

    protected String storeAPIs(List<String> apis) {
        String values = apis.stream()
                .map(token -> "(\"" + token + "\"),")
                .collect(Collectors.joining("\n"));

        values = terminateSqlStatement(values);
        String insertAPIs = ""
                + "INSERT OR IGNORE INTO apis (API) VALUES "
                + values;

        return insertAPIs;
    }

    protected String storeNewTokenContextReference(String token, ITypeName context) {
        String storeReference = ""
                + "INSERT INTO TokenReferences (Token, Context, Count) VALUES ("
                + "(SELECT ID FROM tokens WHERE Token=\""+ token +"\"), "
                + "(SELECT ID FROM contexts WHERE Context=\""+ context +"\"),"
                + " 1)";

        return storeReference;
    }

    protected String storeNewAPIContextReference (String api, ITypeName context) {
        String storeReference = ""
            + "INSERT INTO APIReferences "
            + "(API, Context, Count) VALUES ("
                + "(SELECT ID FROM apis WHERE API=\"" + api + "\"), "
                + "(SELECT ID FROM contexts WHERE CONTEXT=\"" + context + "\"),"
                + "1"
            + ")";

        return storeReference;
    }

    protected String incrementCounterOfAPIReference(String api, ITypeName context) {
        String updateReference = ""
            + "UPDATE APIReferences "
            + "SET Count = Count + 1 "
            + "WHERE API=("
                + "SELECT ID "
                + "FROM apis "
                + "WHERE API=\""+ api +"\""
            + ")"
            + "AND Context=("
                + "SELECT ID "
                + "FROM contexts "
                + "WHERE Context=\""+ context +"\""
            + ")";

        return updateReference;
    }
    protected String incrementCounterOfTokenRefernce(String token, ITypeName context) {
        String updateReference = ""
            + "UPDATE TokenReferences "
            + "SET Count = Count + 1 "
            + "WHERE Token=(SELECT ID FROM tokens WHERE Token=\""+ token +"\")"
            + "AND Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")";

        return updateReference;
    }

    private String terminateSqlStatement(String statement) {
        return replaceLastChar(statement, ';');
    }

    private String replaceLastChar(String target, Character newChar) {
        return target.substring(0, target.length()-1) + newChar;
    }
}
