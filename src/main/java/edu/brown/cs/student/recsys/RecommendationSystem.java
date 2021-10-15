package edu.brown.cs.student.recsys;

import edu.brown.cs.student.api.ApiAggregator;
import edu.brown.cs.student.bloomfilter.BloomFilterRecommender;
import edu.brown.cs.student.bloomfilter.recommender.Item;
import edu.brown.cs.student.kdtree.KDTree;
import edu.brown.cs.student.orm.DataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

  /**
   * The constructor only instantiates the API, as we need user input to instantiate
   * the other three.
   */
  public RecommendationSystem() {
    this.apiAggregator = new ApiAggregator();
    //Instantiate these when given info from REPL.
    this.orm = null;
    this.bloomFilterRecommender = null;
    this.kdTree = null;
  }

  /**
   * User Story 2. This will be implemented by Alyssa and moved here later
   *
   * @param databaseFile
   */
  public String loadData(String databaseFile) {
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
  public List<Object> genRecsForTeam(int numRecs, int studentId) {
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

    return combineRecommendations(numRecs, numericRecommendations, categoricalRecommendations);
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

  /**
   * User Story 4: generate the set of best matched teams across the entire class
   */
  public List<Object> genBestMatchedTeams() {
    return null;
  }
}
