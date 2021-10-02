package edu.brown.cs.student.main.ORM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
  // connection shared across all DataManager instances
  private static Connection conn;

  public DataManager(String databaseFile) throws SQLException, ClassNotFoundException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + databaseFile;
    conn = DriverManager.getConnection(urlToDB);
  }

  public void insert(Object obj) throws Exception {
    // get class name for objectClass (same as table name)
    String className = obj.getClass().getSimpleName();

    // get all the field information from the class
    List<FieldInfo> fieldInfos = ClassInfoUtil.populateFieldInfo(obj.getClass());

    StringBuilder statement = new StringBuilder();
    statement.append("INSERT INTO " + className + " (");

    StringBuilder values = new StringBuilder();
    values.append(" VALUES(");

    for (int i = 0; i < fieldInfos.size() - 1; i++) {
      statement.append(fieldInfos.get(i).name + ", ");
      values.append("?, ");
    }

    statement.append(fieldInfos.get(fieldInfos.size() - 1).name + ")");
    values.append("?)");

    statement.append(values.toString());

    PreparedStatement prep = conn.prepareStatement(statement.toString());

    for (int i = 0; i < fieldInfos.size(); i++) {
      if (fieldInfos.get(i).dataType == int.class) {
        prep.setInt(i + 1, (Integer) fieldInfos.get(i).readMethod.invoke(obj));
      } else if (fieldInfos.get(i).dataType == String.class) {
        prep.setString(i + 1, (String) fieldInfos.get(i).readMethod.invoke(obj));
      }
    }

    prep.executeUpdate();
    prep.close();
  }

  public List<Object> select(String where, String value, Class objectClass)
      throws Exception {
    List<Object> returnValues = new ArrayList();

    // get class name for objectClass (same as table name)
    String className = objectClass.getSimpleName();

    // get all the field information from the class
    List<FieldInfo> fieldInfos = ClassInfoUtil.populateFieldInfo(objectClass);

    PreparedStatement prep =
        conn.prepareStatement("SELECT * from " + className + " WHERE " + where);
    prep.setString(1, value);

    ResultSet rs = prep.executeQuery();
    ResultSetMetaData metaData = rs.getMetaData();

    while (rs.next()) {
      Object obj = objectClass.getConstructor().newInstance();

      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        String columnName = metaData.getColumnName(i);
        FieldInfo fieldInfo = getFieldInfo(fieldInfos, columnName);
        if (fieldInfo.dataType == int.class) {
          fieldInfo.writeMethod.invoke(obj, rs.getInt(i));
        } else if (fieldInfo.dataType == String.class) {
          fieldInfo.writeMethod.invoke(obj, rs.getString(i));
        }
      }
      returnValues.add(obj);
    }
    rs.close();
    prep.close();
    return returnValues;
  }

  public void delete(Object obj) throws Exception {
    // get class name for objectClass (same as table name)
    String className = obj.getClass().getSimpleName();

    // get all the field information from the class
    List<FieldInfo> fieldInfos = ClassInfoUtil.populateFieldInfo(obj.getClass());

    StringBuilder statement = new StringBuilder();

    statement.append("DELETE FROM " + className + " WHERE (");

    for (int i = 0; i < fieldInfos.size() - 1; i++) {
      statement.append(fieldInfos.get(i).name + "=? AND ");
    }

    statement.append(fieldInfos.get(fieldInfos.size() - 1).name + "=?)");

    PreparedStatement prep = conn.prepareStatement(statement.toString());

    for (int i = 0; i < fieldInfos.size(); i++) {
      if (fieldInfos.get(i).dataType == int.class) {
        prep.setInt(i + 1, (Integer) fieldInfos.get(i).readMethod.invoke(obj));
      } else if (fieldInfos.get(i).dataType == String.class) {
        prep.setString(i + 1, (String) fieldInfos.get(i).readMethod.invoke(obj));
      }
    }

    prep.executeUpdate();
    prep.close();
  }

  public void update(Object obj, String field, String value) throws Exception {
    // get class name for objectClass (same as table name)
    String className = obj.getClass().getSimpleName();

    // get all the field information from the class
    List<FieldInfo> fieldInfos = ClassInfoUtil.populateFieldInfo(obj.getClass());

    StringBuilder statement = new StringBuilder();

    statement.append("UPDATE " + className + " SET " + field + " = \"" + value + "\"");

    statement.append(" WHERE (");

    for (int i = 0; i < fieldInfos.size() - 1; i++) {
      statement.append(fieldInfos.get(i).name + "=? AND ");
    }

    statement.append(fieldInfos.get(fieldInfos.size() - 1).name + "=?)");

    PreparedStatement prep = conn.prepareStatement(statement.toString());

    for (int i = 0; i < fieldInfos.size(); i++) {
      if (fieldInfos.get(i).dataType == int.class) {
        prep.setInt(i + 1, (Integer) fieldInfos.get(i).readMethod.invoke(obj));
      } else if (fieldInfos.get(i).dataType == String.class) {
        prep.setString(i + 1, (String) fieldInfos.get(i).readMethod.invoke(obj));
      }
    }

    prep.executeUpdate();
    prep.close();
  }

  private FieldInfo getFieldInfo(List<FieldInfo> fieldInfos, String fieldName) {
    for (FieldInfo info : fieldInfos) {
      if (info.name.equals(fieldName)) {
        return info;
      }
    }

    return null;
  }
}
