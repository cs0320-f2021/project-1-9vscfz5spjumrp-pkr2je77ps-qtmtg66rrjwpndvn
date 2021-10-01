package edu.brown.cs.student.main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldInfo {
  public String name;
  public Method readMethod;
  public Method writeMethod;
  public Field field;
  public Class<?> dataType;
}
