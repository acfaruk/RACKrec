package ch.uzh.rackrec.model.provider;

import cc.kave.commons.model.naming.types.ITypeName;

import java.util.List;
import java.util.stream.Collectors;

public class SQLQueryFactory {
    protected String getTopKCountedAPIsForKeyword(String keyword) {
        return ""
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
    }
    protected String getTokensForAPI(String api) {
        return ""
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
    }

    protected String getApisFromKeywordPairQuery(String keyword1, String keyword2)  {
        return ""
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
    }

    protected String getCountedNeighborTokens(String token) {
        return ""
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
    }

    protected String getAPICountForContext(String context, String api) {
        return ""
            + "SELECT Count "
            + "FROM APIReferences "
            + "WHERE Context=(SELECT ID FROM contexts WHERE Context=\""+ context +"\")"
            + "AND "
            + "API=("
                + "SELECT ID "
                + "FROM apis "
                + "WHERE API=\""+ api +"\""
            + ")";
    }

    protected String getTokenReferences (String token, ITypeName context) {
        return ""
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
    }

    protected String getAPIReferences(String api, ITypeName context) {
        return ""
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
    }

    protected String getApisForContext(String context) {
        return ""
            + "SELECT API "
            + "FROM APIReferences "
            + "WHERE Context=("
                + "SELECT ID "
                + "FROM contexts "
                + "WHERE Context=\""+ context +"\""
            + ")";
    }

    protected String getTokensFromContext(String context) {
        return ""
            + "SELECT Token "
            + "FROM TokenReferences "
            + "WHERE Context=("
                + "SELECT ID "
                + "FROM contexts "
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
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "Context varchar(255) NOT NULL"
            + ")";
    }

    protected String createAPITable() {
        return ""
            + "CREATE TABLE apis("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "API varchar(255) NOT NULL UNIQUE"
            + ")";
    }

    protected String createTokenTable() {
        return ""
            + "CREATE TABLE tokens("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
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
