package lab2.mvc.migrated.database;

public enum QueryRegex {
    LIST_TABLES("^\\s*(LIST\\s+TABLES)\\s*$"),
    CREATE_TABLE("^\\s*CREATE\\s+TABLE\\s+([^\\s]+)\\s+\\(([^\\)]+)\\)\\s*$"),
    DELETE_TABLE("^\\s*DELETE\\s+TABLE\\s+([^\\s]+)\\s*$"),
    SELECT_ROWS("^\\s*SELECT\\s+(.+)\\s+FROM\\s+([^\\s]+)(?:\\s+WHERE\\s+([^\\s]+))?\\s*$"),
    INSERT_ROW("^\\s*INSERT\\s+INTO\\s+([^\\s]+)\\s+\\(([^\\)]+)\\)\\s+VALUES\\s*\\(([^\\)]+)\\)s*$"),
    UPDATE_ROWS("^\\s*UPDATE\\s+([^\\s]+)\\s+SET\\s*([^\\s]+)(?:\\s+WHERE\\s+([^\\s]+))?\\s*$"),
    DELETE_ROWS("^\\s*DELETE\\s+FROM\\s+([^\\s]+)\\s+WHERE\\s+(([^\\s]+\\s*=\\s*[^,]+)(,\\s*[^\\s]+\\s*=\\s*[^,]+)*)\\s*$"),
    SORT_BY("^\\s*SORT\\s+FROM\\s+([^\\s]+)\\s+BY\\s+([^\\s]+)(?:\\s+(ASC|DESC))?\\s*$");

    private String regex;
    public final String SUFFIX = "/i";

    QueryRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex + SUFFIX;
    }
}
