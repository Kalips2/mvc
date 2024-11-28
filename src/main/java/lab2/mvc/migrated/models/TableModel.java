package lab2.mvc.migrated.models;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import lab2.mvc.migrated.database.Element;
import lab2.mvc.migrated.database.Result;
import lab2.mvc.migrated.database.Row;

public class TableModel extends AbstractTableModel {
  ArrayList<Row> rows = new ArrayList<>();
  public ArrayList<String> columns = new ArrayList<>();

  public void setResult(Result result) {
    rows.clear();
    columns.clear();
    if (result.getRows() != null && !result.getRows().isEmpty()) {
      rows.addAll(result.getRows());
      for (Element element : result.getRows().iterator().next().getElements()) {
        columns.add(element.getColumn());
      }
    }
    fireTableStructureChanged();
    fireTableDataChanged();
  }

  @Override
  public int getRowCount() {
    return rows.size();
  }

  @Override
  public int getColumnCount() {
    return columns.size();
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return rows.get(rowIndex).getElement(columns.get(columnIndex)).getValue();
  }

  @Override
  public String getColumnName(int column) {
    return columns.get(column);
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return true;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    Row row = rows.get(rowIndex);

    String columnName = columns.get(columnIndex);

    Element element = row.getElement(columnName);
    if (element != null) {
      element.setValue(aValue.toString());
    }

    fireTableCellUpdated(rowIndex, columnIndex);
  }
}
