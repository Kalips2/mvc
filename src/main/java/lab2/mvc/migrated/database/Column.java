package lab2.mvc.migrated.database;

public class Column {
    public enum Type {
        INT,
        FLOAT,
        CHAR,
        STR,
        MONEY,
        MONEY_INV,
    }

    private Type type;
    private String name;

    public Column(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
