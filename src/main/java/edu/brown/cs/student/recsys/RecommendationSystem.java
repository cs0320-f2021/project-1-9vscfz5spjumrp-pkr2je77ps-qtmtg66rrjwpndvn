package edu.brown.cs.student.recsys;

import edu.brown.cs.student.api.ApiAggregator;
import edu.brown.cs.student.bloomfilter.BloomFilterRecommender;
import edu.brown.cs.student.bloomfilter.recommender.Item;
import edu.brown.cs.student.kdtree.KDTree;
import edu.brown.cs.student.main.command.Command;
import edu.brown.cs.student.orm.DataManager;
import edu.brown.cs.student.orm.Users;

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
  private String databaseName;

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

  public void setDataBaseName(String databaseName) {
    this.databaseName = databaseName;
  }

  /**
   * User Story 2. This will be implemented by Alyssa and moved here later
   */
  public String loadData() {
    //TODO: get how many students there are after loading data into ORM, KD-Tree,
    // and BloomFilter. Must instantiate all 3 objects
    int k = 0;
    String message = "";
    //TODO: have error messages related to loading data
    boolean error = false;
    if (error) {
      message = "Error: Fill out rest of error message";
    } else {
      message = "Loaded Recommender with " + k + " students.";
    }
    return message;
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
   *
   * <p>
   * >
   */
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
    // list to store all students in dataset (returned from orm)
    List<Object> AllStudents = null;

    // hashmap of student to their recs
    Map<Object, List<Object>> studentRecs = new HashMap<>();
    // hashmap of student to their team members (duplicate students stored...)
    // Map<Object, List<Object>> AllTeams = new HashMap<>();

    // list to store all teams
    List<List<Object>> AllTeams = null;

    try {
      // check if this selects all students, or if need to call custom query .sql("SELECT ...")
      AllStudents = orm.select("", "", Object.class);

      //  if the total number of students is not divisible by teamSize
      //  return larger groups than desired in input teamSize
      //  Why? Smaller teams might be overwhelmed by work
      if (AllStudents.size() % teamSize != 0) {
        teamSize++;
      }
      // generate recommendations for every student in the database
      for (Object student : AllStudents) {
        // get numeric recommendations
        List<Object> numericRecommendations = kdTree.kNearestNeighbors(student, AllStudents.size());
        // get categorical recommendations
        List<Object> categoricalRecommendations =
            bloomFilterRecommender.getTopKRecommendations((Item) student, AllStudents.size());
        // combine recommendations
        List<Object> AllRecsForStudent =
            combineRecommendations(teamSize, numericRecommendations, categoricalRecommendations);

        // add student (key) recs (value) to hashmap
        studentRecs.put(student, AllRecsForStudent);
      }

      // loop through map and assign highest recommended students to this students team
      for (Map.Entry<Object, List<Object>> student : studentRecs.entrySet()) {
        int numTeamMates = teamSize;
        List<Object> newTeam = new ArrayList();
        for (List<Object> team : AllTeams) {
          // if student already exists in another students team then move on to next rec
          if (team.contains(student.getValue())) {

          } else {
            newTeam.add()
            numTeamMates--;
          }
        }

      }


    } catch (Exception e) {
      System.out.println("[Error: RecommendationSystem] genBestMatchedTeams(): Your select method "
          + "returned an Exception.");
    }

    // check if student already exists in another team, if so, increment teamSize for next call to
    // get more recommended students. Eventually will have all of them
    // student A optimal for both students B and C
    // prioritize students compatible with highest # of other students?
    // what if total # of students not divisible by team size => return larger groups than desired.
    // print results to REPL interface?

    // handle missing columns?

    // return serialized String version of results
    return null;
  }
}
