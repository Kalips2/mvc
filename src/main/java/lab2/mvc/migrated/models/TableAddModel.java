package lab2.mvc.migrated.models;

import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;
import lab2.mvc.migrated.database.Column;

public class TableAddModel extends AbstractTableModel {
  private final ArrayList<Column> columns;

  public TableAddModel() {
    columns = new ArrayList<>(5);
  }

  public void add(Column column) {
    columns.add(column);
    fireTableRowsInserted(columns.size() - 1, columns.size() - 1);
  }

  void remove(Column column) {
    if (columns.contains(column)) {
      int index = columns.indexOf(column);
      remove(index);
    }
  }

  public void remove(int index) {
    columns.remove(index);
    fireTableRowsDeleted(index, index);
  }

  @Override
  public int getRowCount() {
    return columns.size();
  }

  @Override
  public int getColumnCount() {
    return 2;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  @Override
  public String getColumnName(int column) {
    return column == 0 ? "Type" : "Name";
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    Column column = columns.get(rowIndex);
    return columnIndex == 0 ? column.getType().toString() : column.getName();
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return true;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    Column column = columns.get(rowIndex);
    if (columnIndex == 0) {
      column.setType(aValue == null ? null : Column.Type.valueOf(aValue.toString()));
    } else {
      column.setName(aValue == null ? null : aValue.toString());
    }
    fireTableCellUpdated(rowIndex, columnIndex);
  }

  public String getColumnsAsString() {
    return columns.stream()
        .map(column -> column.getType().toString() + " " + column.getName())
        .collect(Collectors.joining(", "));
  }
}
