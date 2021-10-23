package edu.brown.cs.student.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The DataManager class establishes a connection to the sql database
 * via the ORM and contains methods to interact with it
 * (insert, select, delete, update, sql)
 */
public class DataManager {
  // connection shared across all DataManager instances
  private static Connection conn;

  /**
   * Takes the filename of the database file and establishes a
   * connection to it
   * @param databaseFile the database filename
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public DataManager(String databaseFile) throws SQLException, ClassNotFoundException {
    // create connection with database file path
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + databaseFile;
    conn = DriverManager.getConnection(urlToDB);
  }

  /**
   * Inserts an object into the database table with the same name
   * as the class of the object.
   * @param obj an object to insert into the database
   * @throws Exception
   */
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

  /**
   * Select rows from the sql database and return all matching rows in
   * object form.
   * @param where the condition to select rows by
   * @param value the value of the condition (column) to select by
   * @param objectClass the class of objects to return
   * @return a list of objects representing SQL rows
   * @throws Exception
   */
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

  /**
   * Delete an object (row) from the table
   * @param obj an object representing a sql row
   * @throws Exception
   */
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

  /**
   * Update the value of a field (column) of a specific row in the database
   * @param obj the object whose field needs updating
   * @param field the field to update
   * @param value the value to update the chosen field with
   * @throws Exception
   */
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

  /**
   * Execute a sql command on the database via the ORM
   * @param command a sql command to execute
   * @return a ResultSet response from the database
   * @throws Exception
   */
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

  /**
   * Get the value of a particular field in a list of FieldInfo
   * @param fieldInfos a list of fields
   * @param fieldName the name of the field we'd like to extract a value from
   * @return the FieldInfo of the field searched for
   */
  private FieldInfo getFieldInfo(List<FieldInfo> fieldInfos, String fieldName) {
    for (FieldInfo info : fieldInfos) {
      if (info.name.equals(fieldName)) {
        return info;
      }
    }
    return null;
  }
}
