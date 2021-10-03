package edu.brown.cs.student.main;

import edu.brown.cs.student.main.ORM.DataManager;
import edu.brown.cs.student.main.ORM.Users;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class DataManagerTest {

  @Test
  public void testInsertUser() throws Exception {
    DataManager manager = new DataManager("data/project-1/emptyEditable.sqlite3");
    Users user = new Users();
    user.setAge("10");
    user.setUser_id("1");
    user.setBody_type("athletic");
    user.setHeight("short");
    user.setBust_size("blah");
    user.setWeight("blah");
    user.setHoroscope("gemini");
    manager.delete(user);
    manager.insert(user);

    Users db_user = (Users) manager.select("user_id = ?", "1", Users.class).get(0);
    assertEquals(user.getUser_id(), db_user.getUser_id());
    assertEquals(user.getAge(), db_user.getAge());
    assertEquals(user.getBody_type(), db_user.getBody_type());
    assertEquals(user.getHeight(), db_user.getHeight());
    assertEquals(user.getBust_size(), db_user.getBust_size());
    assertEquals(user.getWeight(), db_user.getWeight());
    assertEquals(user.getHoroscope(), db_user.getHoroscope());
  }

  @Test
  public void testInsertMultiple() throws Exception {
    DataManager manager = new DataManager("data/project-1/emptyEditable.sqlite3");
    Users user = new Users();
    user.setAge("10");
    user.setUser_id("2");
    user.setBody_type("athletic");
    user.setHeight("short");
    user.setBust_size("blah");
    user.setWeight("blah");
    user.setHoroscope("gemini");
    manager.delete(user);
    manager.insert(user);

    Users user2 = new Users();
    user2.setAge("11");
    user2.setUser_id("3");
    user2.setBody_type("not athletic");
    user2.setHeight("tall");
    user2.setBust_size("blah");
    user2.setWeight("blah");
    user2.setHoroscope("capricorn");
    manager.delete(user2);
    manager.insert(user2);

    Users db_user = (Users) manager.select("user_id = ?", "2", Users.class).get(0);
    Users db_user2 = (Users) manager.select("user_id = ?", "3", Users.class).get(0);
    assertEquals(user.getUser_id(), db_user.getUser_id());
    assertEquals(user.getAge(), db_user.getAge());
    assertEquals(user.getBody_type(), db_user.getBody_type());
    assertEquals(user.getHeight(), db_user.getHeight());
    assertEquals(user.getBust_size(), db_user.getBust_size());
    assertEquals(user.getWeight(), db_user.getWeight());

    assertEquals(user2.getUser_id(), db_user2.getUser_id());
    assertEquals(user2.getAge(), db_user2.getAge());
    assertEquals(user2.getBody_type(), db_user2.getBody_type());
    assertEquals(user2.getHeight(), db_user2.getHeight());
    assertEquals(user2.getBust_size(), db_user2.getBust_size());
    assertEquals(user2.getWeight(), db_user2.getWeight());
    assertEquals(user2.getHoroscope(), db_user2.getHoroscope());
  }

  @Test
  public void testDelete() throws Exception {
    DataManager manager = new DataManager("data/project-1/emptyEditable.sqlite3");
    Users user = new Users();
    user.setAge("10");
    user.setUser_id("4");
    user.setBody_type("athletic");
    user.setHeight("short");
    user.setBust_size("blah");
    user.setWeight("blah");
    user.setHoroscope("gemini");
    manager.insert(user);
    manager.delete(user);

    List<Object> db_user = manager.select("user_id = ?", "4", Users.class);
    assertTrue(db_user.isEmpty());
  }

  @Test
  public void testUpdate() throws Exception {
    DataManager manager = new DataManager("data/project-1/emptyEditable.sqlite3");
    Users user = new Users();
    user.setAge("10");
    user.setUser_id("5");
    user.setBody_type("athletic");
    user.setHeight("short");
    user.setBust_size("blah");
    user.setWeight("blah");
    user.setHoroscope("gemini");
    manager.delete(user);
    manager.insert(user);
    manager.update(user, "horoscope", "aries");

    Users db_user = (Users) manager.select("user_id = ?", "5", Users.class).get(0);

    assertEquals("aries", db_user.getHoroscope());
  }
}