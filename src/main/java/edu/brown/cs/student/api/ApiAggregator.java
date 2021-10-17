package edu.brown.cs.student.api;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import edu.brown.cs.student.api.client.ApiClient;
import edu.brown.cs.student.api.client.ClientRequestGenerator;
import edu.brown.cs.student.entity.IdentityData;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class ApiAggregator {
  private final ApiClient client = new ApiClient();

  /**
   * @param dataType - String representing the type of data being considered (Example: rent, review, users)
   * @return - List of the appropriate objects
   * @throws Exception
   */
  public List<Object> getData(String dataType) throws Exception {
    Gson gson = new Gson();
    Type type = setType(dataType);
    String filename = "https://runwayapi.herokuapp.com/" + dataType + "-";
    String response1 =
        client.makeRequest(ClientRequestGenerator.getSecuredRequest(filename + "one?auth="));
    String response2 =
        client.makeRequest(ClientRequestGenerator.getSecuredRequest(filename + "three?auth="));
    response1 = generateExtras("one", filename, response1);
    response2 = generateExtras("two", filename, response2);
    String best_response = response1.length() > response2.length() ? response1 : response2;
    return gson.fromJson(best_response, type);
  }

  public List<Object> getData() throws Exception {
    Gson gson = new Gson();
    String response = client.makeRequest(ClientRequestGenerator.getSecuredPostRequest());
    return gson.fromJson(response, setType("identityData"));
  }

  /**
   * Creates request until the result is not an error message ( At most 5 times )
   *
   * @param server   - The number of the api that we want to call
   * @param filename - The name of the api we are referencing
   * @param response - The response generated on the first call
   * @return - The response as a string
   */
  private String generateExtras(String server, String filename, String response) {
    String error_message =
        "{\"message\": \"Your API call failed due to a malicious error by the course staff\"}";
    try {
      for (int i = 0; i < 5; i++) {
        if (response.equals(error_message)) {
          response = client.makeRequest(
              ClientRequestGenerator.getSecuredRequest(filename + server + "?auth="));
        } else {
          break;
        }
      }
    } catch (Exception ignored) {
    }
    return response;
  }

  /**
   * @param dataType - The type of data we are setting as a String
   * @return - A Type variable set to the desired type
   * @throws Exception
   */
  public Type setType(String dataType) throws Exception {
    Type type;
    if (dataType.equals("identityData")) {
      return new TypeToken<List<IdentityData>>(){}.getType();
    }
    return new TypeToken<List<Object>>() {
    }.getType();
  }

}
