package edu.brown.cs.student.recsys;

import edu.brown.cs.student.api.ApiAggregator;
import edu.brown.cs.student.bloomfilter.BloomFilterRecommender;
import edu.brown.cs.student.bloomfilter.recommender.Item;
import edu.brown.cs.student.entity.IdentityData;
import edu.brown.cs.student.entity.Interests;
import edu.brown.cs.student.entity.Negative;
import edu.brown.cs.student.entity.Positive;
import edu.brown.cs.student.entity.Skills;
import edu.brown.cs.student.entity.Student;
import edu.brown.cs.student.kdtree.KDTree;
import edu.brown.cs.student.main.command.Command;
import edu.brown.cs.student.orm.DataManager;
import edu.brown.cs.student.orm.Users;
import org.checkerframework.checker.units.qual.A;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecommendationSystem is a proxy class that is used by the main REPL to simplify interactions
 * with data. It contains all 4 of our components from Project 1: API, ORM, KD-Tree,
 * and BloomFilter.
 */
public class RecommendationSystem {
  private final ApiAggregator apiAggregator;
  private DataManager orm;
  private BloomFilterRecommender bloomFilterRecommender;
  private KDTree kdTree;
  // make RecommendationSystem singleton
  private static final RecommendationSystem recSys = new RecommendationSystem();
  private List<Student> students;

  /**
   * The constructor only instantiates the API, as we need user input to instantiate
   * the other three.
   */
  private RecommendationSystem() {
    this.apiAggregator = new ApiAggregator();
    //Instantiate these when given info from REPL.
    this.orm = null;
    this.bloomFilterRecommender = null;
    this.kdTree = null;
  }

  public static RecommendationSystem getInstance() {
    return recSys;
  }

  /**
   * Initialize the orm
   * @param databaseName database file
   * @throws SQLException
   * @throws ClassNotFoundException
   */
  public void initDataManager(String databaseName) throws SQLException, ClassNotFoundException {
    this.orm = new DataManager(databaseName);
  }

  /**
   * method to load student data into list of students
   * @return string saying how many students were loaded in
   * @throws Exception
   */
  public String loadData() throws Exception {
    // load data from sql database
    List<Object> skills = this.orm.select("", "", Skills.class);
    List<Object> negatives = this.orm.select("", "", Negative.class);
    List<Object> positives = this.orm.select("", "", Positive.class);
    List<Object> interests = this.orm.select("", "", Interests.class);
    this.students = new ArrayList<>();

    // hashmap of id to student
    Map<Integer, Student> idToStudent = new HashMap<>();

    // add skills, add student to hashmap
    for (Object skill : skills) {
      Student student = new Student();
      Skills studentSkills = (Skills) skill;

      student.setSkills(studentSkills);

      this.students.add(student);

      idToStudent.put(studentSkills.getId(), student);
    }

   // add negative traits to student
    for (Object negative : negatives) {
      Negative studentNegative = (Negative) negative;

      Student student = idToStudent.get(studentNegative.getId());
      student.addNegative(studentNegative.getTrait());
    }

    // add positive traits to student
    for (Object positive : positives) {
      Positive studentPositive = (Positive) positive;

      Student student = idToStudent.get(studentPositive.getId());
      student.addPositive(studentPositive.getTrait());
    }

    // add interests to student
    for (Object interest : interests) {
      Interests studentInterest = (Interests) interest;

      Student student = idToStudent.get(studentInterest.getId());
      student.addInterest(studentInterest.getInterest());
    }

    // load data from API

    ApiAggregator api = new ApiAggregator();
    List<Object> identityData = api.getData();

    // add identity fields to student
    for (Object value : identityData) {
      IdentityData identity = (IdentityData) value;
      Student student = idToStudent.get(identity.getId());
      student.setIdentityData(identity);
    }

    return "Loaded Recommender with " + this.students.size() + " students.";
  }

  /**
   * User Story 3: generate recommendations for a particular student’s team
   */
  public String genRecsForTeam(int numRecs, int studentId) {
    //get numeric recommendations
    //TODO: ask Alyssa how to get the student
    List<Object> students = null;
    try {
      students = orm.select("", "", Object.class);
    } catch (Exception e) {
      System.out.println("[Error: RecommendationSystem] genRecsForTeam(): Your select method "
          + "returned an Exception.");
    }
    Object student = students.get(0);
    List<Object> numericRecommendations = kdTree.kNearestNeighbors(student, numRecs);
    //get categorical recommendations

    List<Object> categoricalRecommendations =
        bloomFilterRecommender.getTopKRecommendations((Item) student, numRecs);
    List<Object> recs =
        combineRecommendations(numRecs, numericRecommendations, categoricalRecommendations);
    return serializeRecs(recs);
  }

  private List<Object> combineRecommendations(int numRecs, List<Object> numericRecommendations,
                                              List<Object> categoricalRecommendations) {
    int n = 0;
    int c = 0;
    List<Object> recs = new ArrayList<>();
    for (int i = 0; i < numRecs; i++) {
      if (i % 2 == 0) {
        recs.add(numericRecommendations.get(n));
        n++;
      } else {
        recs.add(categoricalRecommendations.get(c));
        c++;
      }
    }
    return recs;
  }

  private String serializeRecs(List<Object> recs) {
    StringBuilder str = new StringBuilder();
    //TODO: create student class, and then append their ID
    for (Object rec : recs) {
      str.append(rec.toString());
    }
    return str.toString();
  }

  /**
   * User Story 4: generate the set of best matched teams across the entire class
   *
   * @param teamSize - size of individual teams
   * @return a String representation of the best matched teams for all students
   * in the dataset. Return value has the following structure
   * List<
   * List<[group_1_student_1, group_1_student_2, … group_1_student_k]>
   * List<[group_2_student_1, group_2_student_2, … group_2_student_k]>
   * ...
   * List<[group_n_student_1, group_n_student_2, … group_n_student_k]>
   * >
   */
  public String genBestMatchedTeams(int teamSize) {
    // hashmap of student to their recs
    Map<Object, List<Object>> studentRecs = new HashMap<>();
    // hashmap of student to their team members (duplicate students stored...)
    // Map<Object, List<Object>> AllTeams = new HashMap<>();

    // list to store all teams
    List<List<Student>> AllTeams = new ArrayList<>();

    try {
      //  if the total number of students is not divisible by teamSize
      //  return larger groups than desired in input teamSize
      //  Because smaller teams might be overwhelmed by the work
      if (this.students.size() % teamSize != 0) {
        teamSize++;
      }
      // generate recommendations for every student in the dataset
      for (Student student : this.students) {
        // get numeric recommendations
        List<Object> numericRecommendations =
            kdTree.kNearestNeighbors(student, this.students.size());
        // get categorical recommendations
        List<Object> categoricalRecommendations =
            bloomFilterRecommender.getTopKRecommendations((Item) student, this.students.size());
        // combine recommendations
        List<Object> AllRecsForStudent =
            combineRecommendations(teamSize, numericRecommendations, categoricalRecommendations);

        // add student (key) recs (value) to hashmap
        studentRecs.put(student, AllRecsForStudent);
      }

      // loop through map and assign highest recommended students to this students' team
      for (Map.Entry<Object, List<Object>> student : studentRecs.entrySet()) {
        List<Object> newTeam = new ArrayList();

        // check if this student is already in another team
        for (List<Student> team : AllTeams) {
          // if student doesn't already exist in another team, add it as first member of newTeam
          if (!team.contains(student.getKey())) {
            newTeam.add(student.getKey());
          }
        }

        int teamMembersAdded = teamSize;
        // search for highest recommended teammates for this student
        // looping over all recommendations for this student in the map<student : List<Students>>
        for (Object rec : student.getValue()) {
          // while team hasn't reached capacity
          while (teamMembersAdded != 0) {
            // check if this student is already in another team
            for (List<Student> team : AllTeams) {
              // if student isn't in another team, add it to newTeam
              if (!team.contains((Student) rec)) {
                newTeam.add((Student) rec);
                teamMembersAdded--;
              }
            }
          }
        }
      }
    } catch (Exception e) {
      System.out.println(
          "[Error: RecommendationSystem] genBestMatchedTeams(): Your select method "
              + "returned an Exception.");
    }

    System.out.println(AllTeams);

    // return serialized String version of results
    return serializeBestMatchedTeams(AllTeams);
  }

  private String serializeBestMatchedTeams(List<List<Student>> teams) {
    StringBuilder str = new StringBuilder();
    // TODO: append only ID?
    for (List<Student> team : teams) {
      for (Student student : team) {
        str.append(student.toString());
      }
      str.append("\n");
    }
    return str.toString();
  }

}
