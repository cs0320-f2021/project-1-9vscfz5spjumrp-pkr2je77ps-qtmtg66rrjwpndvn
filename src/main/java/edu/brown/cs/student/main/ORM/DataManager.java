package edu.brown.cs.student.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
  // DataManager's connection
  private Connection conn;

  public DataManager(String databaseFile) throws SQLException, ClassNotFoundException {
    // create connection with database file path
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + databaseFile;
    this.conn = DriverManager.getConnection(urlToDB);
  }

  public void insert(Object obj) throws Exception {
    // get class name for objectClass (same as table name)
    String className = obj.getClass().getSimpleName();

    // get all the field information from the class
    List<FieldInfo> fieldInfos = ClassInfoUtil.populateFieldInfo(obj.getClass());

    // add classname into sql statement
    StringBuilder statement = new StringBuilder();
    statement.append("INSERT INTO " + className + " (");

    StringBuilder values = new StringBuilder();
    values.append(" VALUES(");

    // add field names to insert statement, add ?s for values
    for (int i = 0; i < fieldInfos.size() - 1; i++) {
      statement.append(fieldInfos.get(i).name + ", ");
      values.append("?, ");
    }

    // add the last field name and last ?
    statement.append(fieldInfos.get(fieldInfos.size() - 1).name + ")");
    values.append("?)");

    // combine statement + values to make sql query
    statement.append(values.toString());

    // prepare statement
    PreparedStatement prep = conn.prepareStatement(statement.toString());

    // check if field type is int or string, then use appropriate set function
    // use field's getter to get the value for the passed-in object, fill in the appropriate
    // ? position in string
    for (int i = 0; i < fieldInfos.size(); i++) {
      if (fieldInfos.get(i).dataType == int.class) {
        prep.setInt(i + 1, (Integer) fieldInfos.get(i).readMethod.invoke(obj));
      } else if (fieldInfos.get(i).dataType == String.class) {
        prep.setString(i + 1, (String) fieldInfos.get(i).readMethod.invoke(obj));
      }
    }

    // execute and close
    prep.executeUpdate();
    prep.close();
  }

  public List<Object> select(String where, String value, Class objectClass)
      throws Exception {
    // make list of objects to return
    List<Object> returnValues = new ArrayList();

    // get class name for objectClass (same as table name)
    String className = objectClass.getSimpleName();

    // get all the field information from the class
    List<FieldInfo> fieldInfos = ClassInfoUtil.populateFieldInfo(objectClass);

    // begin select statement
    PreparedStatement prep =
        conn.prepareStatement("SELECT * from " + className + " WHERE " + where);

    // fill in first question mark as value
    prep.setString(1, value);

    // get the data set from the query
    ResultSet rs = prep.executeQuery();
    ResultSetMetaData metaData = rs.getMetaData();

    while (rs.next()) {
      // construct an object
      Object obj = objectClass.getConstructor().newInstance();

      // use field's setters to set values for object
      for (int i = 1; i <= metaData.getColumnCount(); i++) {
        String columnName = metaData.getColumnName(i);
        FieldInfo fieldInfo = getFieldInfo(fieldInfos, columnName);
        if (fieldInfo.dataType == int.class) {
          fieldInfo.writeMethod.invoke(obj, rs.getInt(i));
        } else if (fieldInfo.dataType == String.class) {
          fieldInfo.writeMethod.invoke(obj, rs.getString(i));
        }
      }
      // add object to return list
      returnValues.add(obj);
    }
    // execute, close, return
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

    // begin DELETE statement
    statement.append("DELETE FROM " + className + " WHERE (");

    for (int i = 0; i < fieldInfos.size() - 1; i++) {
      statement.append(fieldInfos.get(i).name + "=? AND ");
    }

    statement.append(fieldInfos.get(fieldInfos.size() - 1).name + "=?)");

    PreparedStatement prep = conn.prepareStatement(statement.toString());

    // check if field type is int or string, then use appropriate set function
    // use field's getter to get the value for the passed-in object, fill in the appropriate
    // ? position in string
    for (int i = 0; i < fieldInfos.size(); i++) {
      if (fieldInfos.get(i).dataType == int.class) {
        prep.setInt(i + 1, (Integer) fieldInfos.get(i).readMethod.invoke(obj));
      } else if (fieldInfos.get(i).dataType == String.class) {
        prep.setString(i + 1, (String) fieldInfos.get(i).readMethod.invoke(obj));
      }
    }

    // execute and close
    prep.executeUpdate();
    prep.close();
  }

  public void update(Object obj, String field, String value) throws Exception {
    // get class name for objectClass (same as table name)
    String className = obj.getClass().getSimpleName();

    // get all the field information from the class
    List<FieldInfo> fieldInfos = ClassInfoUtil.populateFieldInfo(obj.getClass());

    StringBuilder statement = new StringBuilder();

    // begin update statement
    statement.append("UPDATE " + className + " SET " + field + " = \"" + value + "\"");

    statement.append(" WHERE (");

    for (int i = 0; i < fieldInfos.size() - 1; i++) {
      statement.append(fieldInfos.get(i).name + "=? AND ");
    }

    statement.append(fieldInfos.get(fieldInfos.size() - 1).name + "=?)");

    PreparedStatement prep = conn.prepareStatement(statement.toString());

    // check if field type is int or string, then use appropriate set function
    // use field's getter to get the value for the passed-in object, fill in the appropriate
    // ? position in string
    for (int i = 0; i < fieldInfos.size(); i++) {
      if (fieldInfos.get(i).dataType == int.class) {
        prep.setInt(i + 1, (Integer) fieldInfos.get(i).readMethod.invoke(obj));
      } else if (fieldInfos.get(i).dataType == String.class) {
        prep.setString(i + 1, (String) fieldInfos.get(i).readMethod.invoke(obj));
      }
    }

    // execute and close
    prep.executeUpdate();
    prep.close();
  }

  public ResultSet sql(String command) throws Exception {
    String[] commandSplit = command.split(" ");

    // check if it is a select statement (need to return something)
    if (commandSplit[0].equalsIgnoreCase("SELECT")) {
      PreparedStatement prep = conn.prepareStatement(command);
      ResultSet rs = prep.executeQuery();
      prep.close();
      // return ResultSet (no way of knowing type, cannot convert to object)
      return rs;
    } else {
      // if not SELECT, just execute statement
      PreparedStatement prep = conn.prepareStatement(command);
      prep.executeUpdate();
      prep.close();
      // return null
      return null;
    }
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
