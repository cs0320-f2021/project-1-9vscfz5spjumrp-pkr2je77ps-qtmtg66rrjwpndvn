package edu.brown.cs.student.api.client;

import java.net.URI;
import java.net.http.HttpRequest;

/**
 * This class generates the HttpRequests that are then used to make requests from the ApiClient.
 */
public class ClientRequestGenerator {


  /**
   * Similar to the introductory GET request, but restricted to api key holders only. Try calling it without the API
   * Key configured and see what happens!
   *
   * @return an HttpRequest object for accessing the secured resource.
   */
  public static HttpRequest getSecuredRequest(String filepath) {
    ClientAuth clientAuth = new ClientAuth();
    String apiKey = clientAuth.getApiKey();
    String[] apiKeyArray = apiKey.split(" ");
    String user = apiKeyArray[0];
    String password = apiKeyArray[1];
    String reqUri = filepath + user + "&key=" + password;
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(reqUri))
              .build();
    return request;
  }


  /**
   * Similar to the secured GET request, but is a POST request instead.
   *
   * @return an HttpRequest object for accessing and posting to the secured resource.
   */
  public static HttpRequest getSecuredPostRequest() {
    String reqUri = "https://runwayapi.herokuapp.com/integration";
    String apiKey = ClientAuth.getApiKey();

    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(reqUri))
        .POST(HttpRequest.BodyPublishers.ofString("{\"auth\":\"" + "bkilimni" + "\"}"))
        .header("x-api-key", apiKey)
        .build();
    return request;
  }
}
