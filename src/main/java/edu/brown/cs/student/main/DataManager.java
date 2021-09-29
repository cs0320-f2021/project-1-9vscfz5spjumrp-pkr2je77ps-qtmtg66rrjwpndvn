package edu.brown.cs.student.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DataManager {
  // TODO: create connection
  // private Connection connection;

  public DataManager(String databaseFile) {
    // TODO: set connection to database
    //  connection = new Connection(databaseFile);
  }

  public void insert(Object row) {
    // (Reflection API) get class name for the object row => "Users"
    // (Reflection API)get all the field information from the class "Users"
    // Construct SQL statement
    // String statement = "INSERT INTO " + className +( "
    // String values = "VALUES("
    // For (int i = 0; i < listField.length - 1; i ++)
    //     statement += field.getName + ","
    //     values += "?,"
    //  statement += field.getLastOne().getName() + ")";
    //  values += "?)";
    //  statement += values;
    //  Create a preparedStatement for the SQL
    //  For (int i = 0; i < listField.length; i ++)
    //      preparedStatement.setxxx(i + 1, field.getValue());  //setxxx based on field type
    //  connection.executeStatement(ps);
  }

  public List<Object> select(String where, String value, Class objectClass) {
    List<Object> returnValues = new ArrayList();
    // (Reflection API) get class name for objectClass "row" => "Users"
    // (Reflection API)get all the field information from the class "Users"
    // (SQL) SELECT * from Users WHERE" + where
    // (SQL) Create a PreparedStatement for the SQL
    // (SQL) statement.setString(1, value);
    // (SQL) ResultSet rs = connection.execute(statement)
    // (SQL) ResultSetMetaData meta = rs.getMetaData();
    // (ResultSet) Iterate through ResultSet
    //      For each row
    //          create a new User object
    //          for each column index
    //              Get the column name from meta data
    //              Find the field for the class, and set the field value on the user object
    //          add the user object to the returnValues

    return returnValues;
  }

  public void delete(Object row) {
    // (Reflection API) get class name for the object row => "Users"
    // (Reflection API)get all the field information from the class "Users"
    // Construct SQL statement
    // String statement = "DELETE FROM " + className + where ( "
    // For (int i = 0; i < listField.length - 1; i ++)
    //     statement += field.getName + "= ? AND"
    //  statement += field.getLastOne().getName() + "=?)";
    //  Create a preparedStatement for the SQL
    //  For (int i = 0; i < listField.length; i ++)
    //      preparedStatement.setxxx(i + 1, field.getValue());  //setxxx based on field type
    //  connection.executeStatement(ps);
  }

  public void update(Object row, String field, String value) {
    // (Reflection API) get class name for the object row => "Users"
    // (Reflection API)get all the field information from the class "Users"
    // Construct SQL statement
    // String statement = "UPDATE" + className + SET "
    // For (int i = 0; i < listField.length - 1; i ++)
    //     statement += field.getName + "= ? AND"
    //  statement += field.getLastOne().getName() + "=?)";
    //  statement += "WHERE" + field + "=?"
    //  Create a preparedStatement for the SQL
    //  For (int i = 0; i < listField.length; i ++)
    //      preparedStatement.setxxx(i + 1, field.getValue());  //setxxx based on field type
    //   preparedStatement.setxxx(fieldLength + 1, value)
    //  connection.executeStatement(ps);
  }
}
