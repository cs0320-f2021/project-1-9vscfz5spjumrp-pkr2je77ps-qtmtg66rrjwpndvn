package edu.brown.cs.student.main;

/**
 * Parker's Comments:
 *    N.B. this is much much better than what I implemented, and
 *    most of the things I'm about to say I didn't do myself.
 *
 * 1) Systems Tests: I like that you added more tests in system/stars. A few
 * additional tests to think about: All Stars (how many should it read),
 * Duplicate Star (how does your parser handle a star it's seen before? Error?),
 * Incorrect Number of Arguments (calling "naive_neighbors" with 4 arguments),
 * Star Does Not Exist (checking that error prints)
 *
 * 2) Unit Tests: You could probably have a bunch of argument checks for the
 * functions in Stars.java. I know that they're similar to the systems tests,
 * but whatever
 *
 * 3) Interesting choice to put Euclidian Distance function in MathBot. Didn't even think
 * about using the MathBot. Maybe make this part of the Stars class? Since Mathbot's "add"
 * and "subtract" are pretty separate from the Stars functionality. I agree that it's still
 * a math function though, so super subjective there
 *
 * 4) Is there a better way to refactor kNearest()? It seems like a super long function.
 * Not that I did much better, lol.
 *
 * 5) Could you use a private instance variable to store the star data? Once you load star data with
 * "star", you might want to call "naive_neighbors" multiple times with different arguments.
 * It would be a shame to reload that data every time.
 *
 * 6) LOVE all of the refactoring into a Stars class. I was way too lazy and did not do that.
 *
 */

/**
* Alyssa's Comments:
* 1. I think kNearest() could be made into two separate functions? You could overload the same method
* name, and since the two methods would take in a different number and types of parameters (one 
* would have parameters with k and coordinates, and the other would have k and a String), you could
* call the appropriate function based on the number of arguments the REPL took in.
*
* 2. I like how clean your REPL code is, it's really easy and straightforward to read which is nice
* (the same cannot be said for my REPL, so I really noticed it in yours!). 
*
* 3. I really like the Stars class!! Heck yeah single responsibility code!!
*
* 4. As Parker mentioned, I'm not sure MathBot is the right place to put the Euclidean distance
* calculation, just since the mathbot operations are quite separate from the stars operations,
* but again, that's also subjective.
*/

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

  private Main(String[] args) {
    this.args = args;
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
      List<List<String>> records = new ArrayList<>();
      String input;
      while ((input = br.readLine()) != null) {
        try {
          input = input.trim();
          String[] arguments = input.split(" ");
          MathBot math = new MathBot();
          Stars stars = new Stars();
          switch (arguments[0]) {
            case "add":
              double resAdd =
                  math.add(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]));
              System.out.println(resAdd);
              break;
            case "subtract":
              double resSub =
                  math.subtract(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]));
              System.out.println(resSub);
              break;
            case "stars":
              records = stars.parseCSV(arguments[1]);
              break;
            case "naive_neighbors":
              ArrayList<String> nearestStars = Stars.kNearest(arguments, records, math);
              for (String star : nearestStars) {
                System.out.println(star);
              }
              break;
            default:
              throw new Exception("Incorrect Command");
          }
        } catch (Exception e) {
          // e.printStackTrace();
          System.out.println("ERROR: We couldn't process your input");
        }
      }
    } catch (
        Exception e) {
      e.printStackTrace();
      System.out.println("ERROR: Invalid input for REPL");
    }

  }

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
