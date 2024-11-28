package lab2.mvc.migrated.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;
import lab2.mvc.migrated.database.Column;

public class RowAddModel extends AbstractTableModel {
  public ArrayList<Column> getColumns() {
    return columns;
  }

  final ArrayList<Column> columns = new ArrayList<>();
  final ArrayList<String> values = new ArrayList<>();

  public RowAddModel(Collection<Column> columns) {
    this.columns.addAll(columns);
    columns.forEach(column -> this.values.add(""));
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
    return column == 0 ? "Column Name" : "Value";
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex) {
    return columnIndex == 0 ?
        String.format("%s (%s)", columns.get(rowIndex).getName(),
            columns.get(rowIndex).getType().toString()) :
        values.get(rowIndex);
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return columnIndex == 1;
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (columnIndex == 1) {
      values.set(rowIndex, aValue == null ? null : aValue.toString());
    }
    fireTableCellUpdated(rowIndex, columnIndex);
  }

  public List<String> getValues() {
    return values;
  }

  public String getColumnsAsString() {
    return columns.stream().map(Column::getName).collect(Collectors.joining(", "));
  }

  public String getValuesAsString() {
    return values.stream()
        .map(value -> value == null || value.isEmpty() ? "NULL" :
            "'" + value.replace("'", "''") + "'")
        .collect(Collectors.joining(", "));
  }
}
