package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Random;

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

/**
 * Parker's Comments:
 *    N.B. I did not do most of the things that I'm about to say. So, a lot
 *    of these comments are also directed at me.
 *
 * 1) Unit Testing: fantastic job with StarHandlerTest.java
 *
 * 2) Systems Tests: I like that you added more tests in system/stars. A few
 *  * additional tests to think about: All Stars (how many should it read),
 *  * Duplicate Star (how does your parser handle a star it's seen before? Error?),
 *  * Incorrect Number of Arguments (calling "naive_neighbors" with 4 arguments),
 *  * Star Does Not Exist (checking that error prints)
 *
 *  3) Instead of using if/else if/else conditionals, try opting for a switch statement.
 *
 *  4) Nice use of TreeMap structure. Is it better to construct while parsing the CSV file or afterward? I'm
 *  actually not sure. Afterward makes sense if you want to get distance to a set of coordinates.
 *
 *  5) Lots of validation going on (new Star creation, headers, etc...) I like it.
 */

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
      // create new StarHandler
      StarHandler starHandler = new StarHandler();

      String input;
      while ((input = br.readLine()) != null) {
        try {
          input = input.trim();
          String[] arguments = input.split(" ");
          // declare new MathBot
          MathBot mathBot = new MathBot();

          if (arguments[0].equals("add")) {
            // if first argument is "add," use mathbot to add the next 2 arguments
            double sum =
                  mathBot.add(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]));

            System.out.println(sum);
          } else if (arguments[0].equals("subtract")) {
            // if first argument is "subtract," use mathbot to subtract the next 2 arguments
            double difference = mathBot.subtract(Double.parseDouble(arguments[1]),
                  Double.parseDouble(arguments[2]));

            System.out.println(difference);
          } else if (arguments[0].equals("stars")) {
            // if first argument is "stars," use loadStarInfo to read csv file into list
            starHandler.loadStarInfo(arguments[1]);
          } else if (arguments[0].equals("naive_neighbors")) {
            if (arguments.length == 5) {
              // if arguments length is 5, then use the naiveNeighbors with 4 arguments
              List<Star> list = starHandler.naiveNeighbors(Integer.parseInt(arguments[1]),
                  Float.parseFloat(arguments[2]),
                  Float.parseFloat(arguments[3]),
                  Float.parseFloat(arguments[4]));

              // print out star IDs
              for (Star star : list) {
                System.out.println(star.getStarID());
              }
            } else if (arguments.length == 3) {
              // if arguments length is 3, check that name has quotations and then get the
              // name out
              String name = arguments[2];

              if (name.charAt(0) == '"' && name.charAt(name.length() - 1) == '"') {
                // get the name from the quotations and call naiveNeighbors
                List<Star> list = starHandler.naiveNeighbors(Integer.parseInt(arguments[1]),
                    name.substring(1, name.length() - 1));

                // print out star IDs if list is null, otherwise star does not exist in database
                if (list != null) {
                  for (Star star : list) {
                    System.out.println(star.getStarID());
                  }
                } else {
                  System.out.println("ERROR: Star with name " + name + " does not exist in the database");
                }
              } else {
                System.out.println("ERROR: Name must have quotations around it");
              }
            }
          } else {
            // invalid input
            System.out.println("ERROR: We couldn't process your input");
          }
        } catch (Exception e) {
//          e.printStackTrace();
          System.out.println("ERROR: We couldn't process your input");
        }
      }
    } catch (Exception e) {
//      e.printStackTrace();
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
