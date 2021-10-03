package edu.brown.cs.student.main;

import edu.brown.cs.student.main.ORM.DataManager;
import edu.brown.cs.student.main.ORM.Users;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;

public class DataManagerTest {

  @Test
  public void testInsertUser() throws Exception {
    DataManager manager = new DataManager("data/project-1/emptyEditable.sqlite3");
    Users user = new Users();
    user.setAge("10");
    user.setUser_id("111");
    user.setBody_type("athletic");
    user.setHeight("short");
    user.setBust_size("blah");
    user.setWeight("blah");
    user.setHoroscope("gemini");
    manager.delete(user);
    manager.insert(user);

    Users db_user = (Users) manager.select("user_id = ?", "111", Users.class).get(0);
    assertEquals(user.getUser_id(), db_user.getUser_id());
    assertEquals(user.getAge(), db_user.getAge());
    assertEquals(user.getBody_type(), db_user.getBody_type());
    assertEquals(user.getHeight(), db_user.getHeight());
    assertEquals(user.getBust_size(), db_user.getBust_size());
    assertEquals(user.getWeight(), db_user.getWeight());
    assertEquals(user.getHoroscope(), db_user.getHoroscope());
  }
}