package edu.brown.cs.student.recsys;

import edu.brown.cs.student.api.ApiAggregator;
import edu.brown.cs.student.bloomfilter.BloomFilterRecommender;
import edu.brown.cs.student.kdtree.KDTree;
import edu.brown.cs.student.orm.DataManager;
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
  public List<Object> genRecsForTeam() {
    return null;
  }

  /**
   * User Story 4: generate the set of best matched teams across the entire class
   */
  public List<Object> genBestMatchedTeams() {
    return null;
  }
}
