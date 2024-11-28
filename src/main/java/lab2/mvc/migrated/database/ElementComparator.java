package lab2.mvc.migrated.database;

import java.util.Arrays;
import java.util.Comparator;

public class ElementComparator implements Comparator<Element> {
  private final Column columnToSort;

  public ElementComparator(Column columnToSort) {
    this.columnToSort = columnToSort;
  }

  @Override
  public int compare(Element e1, Element e2) {
    try {
      switch (columnToSort.getType()) {
        case INT:
          return Integer.compare(e1.getAsInteger(), e2.getAsInteger());
        case FLOAT:
          return Float.compare(e1.getAsFloat(), e2.getAsFloat());
        case CHAR:
          return Character.compare(e1.getAsCharacter(), e2.getAsCharacter());
        case STR:
          return e1.getAsString().compareTo(e2.getAsString());
        case MONEY:
          String s = e1.getAsString().replace("$", "").replace(",", "");
          double value1 = Double.parseDouble(s);
          String s1 = e2.getAsString().replace("$", "").replace(",", "");
          System.out.println("Comparing " + s + " with " + s1);
          double value2 = Double.parseDouble(s1);
          return Double.compare(value1, value2);
        case MONEY_INV:
          double money1 = Double.parseDouble(Arrays.stream(e1.getAsString().split(";")).toList().get(0).replace("$", "").replace(",", ""));
          double money2 = Double.parseDouble(Arrays.stream(e2.getAsString().split(";")).toList().get(0).replace("$", "").replace(",", ""));
          return Double.compare(money1, money2);
        default:
          throw new IllegalArgumentException("Unsupported type: " + columnToSort.getType());
      }
    } catch (Exception ex) {
      throw new RuntimeException("Error during comparison: " + ex.getMessage(), ex);
    }
  }
}