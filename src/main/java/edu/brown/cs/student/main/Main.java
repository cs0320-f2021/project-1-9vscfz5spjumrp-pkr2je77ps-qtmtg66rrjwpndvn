package edu.brown.cs.student.main;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

//TODO:
// TESTING:
// 1) call stars twice and make sure data is freshly loaded
// 2) make sure that the stars is actually in quotes
// 3) make sure star can't return itself

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  // use port 4567 by default when running server
  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;
  private Map<String, Star> stars;

  private Main(String[] args) {
    this.args = args;
    this.stars = new HashMap<>();
  }

  private void run() {
    // set up parsing of command line flags
    OptionParser parser = new OptionParser();

    // "./run --gui" will start a web server
    parser.accepts("gui");

    // use "--port <n>" to specify what port on which the server runs
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      while ((input = br.readLine()) != null) {
        try {
          input = input.trim();
          // Following 3 lines were taken from https://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
          List<String> list = new ArrayList<>();
          Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(input);
          while (m.find()) list.add(m.group(1)); // Add .replace("\"", "") to remove surrounding quotes.
          // resume my code
          String[] arguments = new String[list.size()];
          arguments = list.toArray(arguments);
          MathBot bot = new MathBot();
          switch(arguments[0]) {
            case "add":
              System.out.println(bot.add(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2])));
              break;
            case "subtract":
              System.out.println(bot.subtract(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2])));
              break;
            case "stars":
              if (arguments.length != 2) {
                System.out.println("ERROR: You must specify a file to read from");
                break;
              }
              int count = this.loadStarsInformation(arguments[1]);
              String message = String.format("Read %d stars from %s", count, arguments[1]);
              System.out.println(message);
              break;
            case "naive_neighbors":
              //TODO: handle exception where star name isn't given in quotes
              if (arguments.length == 5) {
                List<Star> nearestNeighbors = this.getNearestNeighbors(
                        Integer.parseInt(arguments[1]),
                        Double.parseDouble(arguments[2]),
                        Double.parseDouble(arguments[3]),
                        Double.parseDouble(arguments[4])
                );
                for (Star star : nearestNeighbors) System.out.println(star.getStarID());
              } else if (arguments.length == 3) {
                List<Star> nearestNeighbors = this.getNearestNeighbors(Integer.parseInt(arguments[1]), arguments[2]);
                for (Star star : nearestNeighbors) System.out.println(star.getStarID());
              } else {
                System.out.println("ERROR: invalid call to naive_neighbors.");
              }
              break;
            default:
              System.out.println("ERROR: Invalid command.");
          }
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("ERROR: We couldn't process your input");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("ERROR: Invalid input for REPL");
    }

  }

  private int loadStarsInformation(String filename) {
    this.stars.clear();
    int count = 0;

    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
      String line;
      br.readLine();
      String starName = "Star_";
      int starNum = 0;
      while ((line = br.readLine()) != null) {
        String[] data = line.split(",");
        if (data[1].equals("")) {
          data[1] = starName + starNum;
          starNum++;
        }
        Star star = new Star(
                Integer.parseInt(data[0]),
                data[1],
                Double.parseDouble(data[2]),
                Double.parseDouble(data[3]),
                Double.parseDouble(data[4])
        );
        count++;
        stars.put(star.getProperName(), star);
      }
    } catch (IOException e) {
      System.out.println("ERROR: Could not open filename");
    }
    return count;
  }

  private List<Star> getNearestNeighbors(int numNeighbors, double x, double y, double z) {
    if (numNeighbors == 0) return new LinkedList<>();

    List<Star> nearestNeighbors = new LinkedList<>();
    int count = 0;
    // Add all stars to the queue
    for (Star star : this.stars.values()) {
      // if this is the first star, we just need to add it
      if (count == 0) {
        nearestNeighbors.add(star);
        count++;
      } else if (count < numNeighbors) { //fill list until we have enough
        int pos = getStarPosition(x, y, z, nearestNeighbors, star);
        nearestNeighbors.add(pos, star);
        count++;
      } else { // at capacity
        if (nearestNeighbors.get(0).getEuclidianDistance(x, y, z) > star.getEuclidianDistance(x, y, z)) {
          nearestNeighbors.remove(0);
          int pos = getStarPosition(x, y, z, nearestNeighbors, star);
          nearestNeighbors.add(pos, star);
        }
      }
    }
    Collections.reverse(nearestNeighbors);
    return nearestNeighbors;
  }

  private List<Star> getNearestNeighbors(int numNeighbors, String name) {
    name = name.replaceAll("^\"|\"$", "");
    if (!this.stars.containsKey(name)) {
      System.out.println(String.format("ERROR: We do not have Star %s stored.", name));
      return new LinkedList<>();
    }
    Star givenStar = this.stars.get(name);
    double x = givenStar.getX();
    double y = givenStar.getY();
    double z = givenStar.getZ();

    if (numNeighbors == 0) return new LinkedList<>();

    List<Star> nearestNeighbors = new LinkedList<>();
    int count = 0;
    // Add all stars to the queue
    for (Star star : this.stars.values()) {
      // if this is the first star, we just need to add it
      if (star.getProperName().equals(name)) continue;
      if (count == 0) {
        nearestNeighbors.add(star);
        count++;
      } else if (count < numNeighbors) { //fill list until we have enough
        int pos = getStarPosition(x, y, z, nearestNeighbors, star);
        nearestNeighbors.add(pos, star);
        count++;
      } else { // at capacity
        if (nearestNeighbors.get(0).getEuclidianDistance(x, y, z) > star.getEuclidianDistance(x, y, z)) {
          nearestNeighbors.remove(0);
          int pos = getStarPosition(x, y, z, nearestNeighbors, star);
          nearestNeighbors.add(pos, star);
        }
      }
    }
    //reverse because we used head of list as furthest star
    Collections.reverse(nearestNeighbors);
    return nearestNeighbors;
  }

  private int getStarPosition(double x, double y, double z, List<Star> nearestNeighbors, Star star) {
    for (int i = 0; i < nearestNeighbors.size(); i++) {
      Star furthestStar = nearestNeighbors.get(i);
      if (furthestStar.getEuclidianDistance(x, y, z) < star.getEuclidianDistance(x, y, z)) {
        return i;
      }
    }
    return nearestNeighbors.size();
  };

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration(Configuration.VERSION_2_3_0);

    // this is the directory where FreeMarker templates are placed
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    // set port to run the server on
    Spark.port(port);

    // specify location of static resources (HTML, CSS, JS, images, etc.)
    Spark.externalStaticFileLocation("src/main/resources/static");

    // when there's a server error, use ExceptionPrinter to display error on GUI
    Spark.exception(Exception.class, new ExceptionPrinter());

    // initialize FreeMarker template engine (converts .ftl templates to HTML)
    FreeMarkerEngine freeMarker = createEngine();

    // setup Spark Routes
    Spark.get("/", new MainHandler(), freeMarker);
  }

  /**
   * Display an error page when an exception occurs in the server.
   */
  private static class ExceptionPrinter implements ExceptionHandler<Exception> {
    @Override
    public void handle(Exception e, Request req, Response res) {
      // status 500 generally means there was an internal server error
      res.status(500);

      // write stack trace to GUI
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

  /**
   * A handler to serve the site's main page.
   *
   * @return ModelAndView to render.
   * (main.ftl).
   */
  private static class MainHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {
      // this is a map of variables that are used in the FreeMarker template
      Map<String, Object> variables = ImmutableMap.of("title",
          "Go go GUI");

      return new ModelAndView(variables, "main.ftl");
    }
  }
}
