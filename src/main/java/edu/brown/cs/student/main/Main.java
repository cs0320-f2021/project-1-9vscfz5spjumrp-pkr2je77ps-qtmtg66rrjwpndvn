package edu.brown.cs.student.main;

import edu.brown.cs.student.orm.DataManager;
import edu.brown.cs.student.orm.Users;

/**
 * Parker's Comments:
 *
 * (1) Code Problems:
 * Our REPL
 * (2) User Testing:
 */

public final class Main {
  public static void main(String[] args) throws Exception {
    DataManager manager = new DataManager("C:\\Users\\alyss\\CS32\\onboarding-9VsCfZ5sPjUMRp\\data\\project-1\\emptyEditable.sqlite3");
//    List<Object> users = manager.select("age = ?", "27", Users.class);
//    System.out.println(users);
//
//    List<Object> reviews = manager.select("id = ?", "2", Reviews.class);
//    System.out.println(reviews);

    Users user = new Users();

    user.setAge("10");
    user.setUser_id("5");
    user.setBody_type("athletic");
    user.setHeight("short");
    user.setBust_size("blah");
    user.setWeight("blah");
    user.setHoroscope("gemini");
//    manager.insert(user);

//    manager.delete(user);

    manager.update(user, "horoscope", "aries");
  }
}
