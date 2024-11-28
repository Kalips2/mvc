package lab2.mvc.migrated.database;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Row implements Serializable {
    private Map<String, Element> elements;

    public Row(Collection<Element> elements) {
        this.elements = new HashMap<>();
        for (Element element : elements) {
            this.elements.put(element.getColumn(), element);
        }
    }

    public Element getElement(String columnName) {
        return elements.get(columnName);
    }

    public Element getElement(Column column) {
        return getElement(column.getName());
    }

    public Collection<Element> getElements() {
        return elements.values();
    }

    public Map<String, Element> getElementsAll() {
        return elements;
    }

    public void validate(Table table) throws Exception {
        for (Element element : getElements()) {
            element.validate(table);
        }
    }
}