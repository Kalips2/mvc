package lab2.mvc.migrated.database;

import static java.lang.Integer.parseInt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Element implements Serializable {
    public static final BigDecimal MAX_MONEY = new BigDecimal("10000000000000.00");

    private String value;
    private String column;

    public Element(String value, String column) {
        this.value = value;
        this.column = column;
    }

    @JsonIgnore
    public Integer getAsInteger() {
        formatValue();
        return value == null ? 0 : parseInt(value);
    }

    @JsonIgnore
    public Float getAsFloat() {
        formatValue();
        return value == null ? 0.0f : Float.parseFloat(value);
    }

    @JsonIgnore
    public char getAsCharacter() throws Exception {
        formatValue();
        if (value.length() != 1) throw new Exception("Invalid character value");
        return value.charAt(0);
    }

    @JsonIgnore
    public String getAsString() {
        formatValue();
        return value == null ? "" : value;
    }

    @JsonIgnore
    public BigDecimal getAsMoney() throws Exception {
        formatValue();
        value = value == null ? "0.0" : value;
        try {
            Double.parseDouble(Objects.requireNonNull(value));
        } catch(Exception e) {
            throw new Exception("Invalid number format");
        }

        BigDecimal money = new BigDecimal(value);

        if (money.compareTo(MAX_MONEY) > 0) {
            throw new Exception("Value exceeds maximum allowed amount of $10,000,000,000,000.00");
        }

        this.value = "$" + String.format("%,.2f", money);

        return money;
    }

    @JsonIgnore
    public List<BigDecimal> getAsMoneyInv() throws Exception {
        formatValue();
        value = value == null ? "0.0;0.0" : value;
        List<BigDecimal> moneyList = new ArrayList<>();

        String[] moneyValues = value.split(";");

        for (String moneyStr : moneyValues) {
            moneyStr = moneyStr.trim();
            moneyStr = moneyStr.replace("$", "");

            try {
                Double.parseDouble(moneyStr);
            } catch(Exception e) {
                throw new Exception("Invalid number format");
            }

            BigDecimal money = new BigDecimal(moneyStr);

            if (money.compareTo(MAX_MONEY) > 0) {
                throw new Exception("Value exceeds maximum allowed amount of $10,000,000,000,000.00");
            }

            moneyList.add(money);
        }

        if (moneyList.get(0).compareTo(moneyList.get(1)) > 0) {
            throw new Exception("Range is incorrect!");
        }

        this.value = moneyList.stream().map(m -> "$" + String.format("%,.2f", m)).collect(Collectors.joining(";"));

        return moneyList;
    }


    public String getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(String value, Column column) throws Exception {
        this.value = value;
        validate(column);
    }

    void validate(Table table) throws Exception {
        Column column = table.getColumn(this.column);

        try {
            switch (column.getType()) {
                case INT:
                    getAsInteger();
                    break;
                case FLOAT:
                    getAsFloat();
                    break;
                case CHAR:
                    getAsCharacter();
                    break;
                case STR:
                    getAsString();
                    break;
                case MONEY:
                    getAsMoney();
                    break;
                case MONEY_INV:
                    getAsMoneyInv();
                    break;
            }
        } catch (Exception e) {
            throw new Exception(String.format("Invalid element value '%s': %s", value, e.getMessage()));
        }
    }

    void validate(Column column) throws Exception {
        try {
            switch (column.getType()) {
                case INT:
                    getAsInteger();
                    break;
                case FLOAT:
                    getAsFloat();
                    break;
                case CHAR:
                    getAsCharacter();
                    break;
                case STR:
                    getAsString();
                    break;
                case MONEY:
                    getAsMoney();
                    break;
                case MONEY_INV:
                    getAsMoneyInv();
                    break;
            }
        } catch (Exception e) {
            throw new Exception(String.format("Invalid element value '%s': %s", value, e.getMessage()));
        }
    }

    public boolean equals(String other) {
        if (other == null) return value == null;
        if (value == null) return other.equals("null");
        return value.equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return Objects.equals(value, element.value) &&
                Objects.equals(column, element.column);
    }

    private void formatValue() {
        if (value != null) {
            this.value = value.replace("'", "");
        }
    }
}
