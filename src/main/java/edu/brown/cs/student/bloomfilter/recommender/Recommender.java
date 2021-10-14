package edu.brown.cs.student.bloomfilter.recommender;

import java.util.List;

public interface Recommender<T extends Item> {
  List<T> getTopKRecommendations(T item, int k);
}
