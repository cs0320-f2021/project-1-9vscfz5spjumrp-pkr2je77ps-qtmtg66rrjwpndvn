package edu.brown.cs.student.main;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * taken from https://github.com/dieselpoint/norm/tree/master/src/main/java/com/dieselpoint/norm
 */
public class ClassInfoUtil {
  public static List<FieldInfo> populateFieldInfo(Class<?> clazz)
      throws IntrospectionException {

    List<FieldInfo> fieldInfos = new ArrayList<>();

    BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
    PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
    for (PropertyDescriptor descriptor : descriptors) {

      Method readMethod = descriptor.getReadMethod();
      if (readMethod == null) {
        continue;
      }

      FieldInfo fieldInfo = new FieldInfo();
      fieldInfo.name = descriptor.getName();
      fieldInfo.readMethod = readMethod;
      fieldInfo.writeMethod = descriptor.getWriteMethod();
      fieldInfo.dataType = descriptor.getPropertyType();

      fieldInfos.add(fieldInfo);
    }

    return fieldInfos;
  }
}
