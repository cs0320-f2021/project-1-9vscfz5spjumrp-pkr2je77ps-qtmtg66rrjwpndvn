package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import edu.brown.cs.student.api.ApiAggregator;
import edu.brown.cs.student.kdtree.KDTree;
import edu.brown.cs.student.main.command.Command;
import edu.brown.cs.student.main.command.RecSysGenGroupsCommand;
import edu.brown.cs.student.main.command.RecSysLoadCommand;
import edu.brown.cs.student.main.command.RecSysRecCommand;
import edu.brown.cs.student.orm.DataManager;
import edu.brown.cs.student.recsys.RecommendationSystem;
import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.checkerframework.checker.units.qual.A;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class REPLMain {

  // use port 4567 by default when running server
  private static final int DEFAULT_PORT = 4567;
  private Class Users;

  protected final Map<String, Command> commandMap = new HashMap<>();


  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) throws Exception {
    REPLMain repl = new REPLMain(args);
    repl.buildCommandMap();
    repl.run();
  }

  protected void buildCommandMap() {
    commandMap.put("recsys_load", new RecSysLoadCommand());
    commandMap.put("recsys_rec", new RecSysRecCommand());
    commandMap.put("recsys_gen_groups", new RecSysGenGroupsCommand());
  }

  private String[] args;

  private REPLMain(String[] args) {
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

    OptionSpec<String> databaseSpec =
        parser.accepts("database").withRequiredArg().ofType(String.class);

    DataManager manager = null;
    // support providing db file via cmd line argument --database=path/to/database
    // set default sql database
    String databaseName = "data/integration/integration.sqlite3";
    if (options.has("database")) {
      databaseName = options.valueOf(databaseSpec);
    }
    try {
      RecommendationSystem.getInstance().initDataManager(databaseName);
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }


    //Instantiate KDTree. Will create later
    KDTree kdTree = null;
    List<String> fields = new ArrayList<>();
    fields.add("weight");
    fields.add("height");
    fields.add("age");

    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

      //Using Proxy pattern to store components
      RecommendationSystem recSys = RecommendationSystem.getInstance();

      String input;
      while ((input = br.readLine()) != null) {
        try {
          input = input.trim();
          String[] arguments = input.split(" ");

          commandMap.get(arguments[0]).execute(arguments);

//          switch (arguments[0]) {
//            case "recsys_rec":
//              //make sure that the input is actually valid
//              if (arguments.length != 3) {
//                System.out.println(
//                    "Error: Too many arguments provided to the 'recsys_rec' command."
//                        + "Your REPL input should follow the following format:"
//                        + "recsys_rec <num_recs> <student_id>");
//              }
//              //parse rest of args
//              int numRecs = Integer.parseInt(arguments[1]);
//              int studentId = Integer.parseInt(arguments[2]);
//              //get recs
//              String message = recSys.genRecsForTeam(numRecs, studentId);
//              System.out.println(message);
//              break;
//            default:
//              System.out.println("ERROR: Invalid command.");
//            case "add":
//              // if first argument is "add," use mathbot to add the next 2 arguments
//              double sum =
//                  mathBot.add(Double.parseDouble(arguments[1]), Double.parseDouble(arguments[2]));
//
//              System.out.println(sum);
//              break;
//            case "subtract":
//              // if first argument is "subtract," use mathbot to subtract the next 2 arguments
//              double difference = mathBot.subtract(Double.parseDouble(arguments[1]),
//                  Double.parseDouble(arguments[2]));
//
//              System.out.println(difference);
//              break;
//            case "stars":
//              // if first argument is "stars," use loadStarInfo to read csv file into list
//              starHandler.loadStarInfo(arguments[1]);
//              break;
//            case "naive_neighbors":
//              if (arguments.length == 5) {
//                // if arguments length is 5, then use the naiveNeighbors with 4 arguments
//                List<Star> list = starHandler.naiveNeighbors(Integer.parseInt(arguments[1]),
//                    Float.parseFloat(arguments[2]),
//                    Float.parseFloat(arguments[3]),
//                    Float.parseFloat(arguments[4]));
//
//                // print out star IDs
//                for (Star star : list) {
//                  System.out.println(star.getStarID());
//                }
//              } else if (arguments.length == 3) {
//                // if arguments length is 3, check that name has quotations and then get the
//                // name out
//                String name = arguments[2];
//
//                if (name.charAt(0) == '"' && name.charAt(name.length() - 1) == '"') {
//                  // get the name from the quotations and call naiveNeighbors
//                  List<Star> list = starHandler.naiveNeighbors(Integer.parseInt(arguments[1]),
//                      name.substring(1, name.length() - 1));
//
//                  // print out star IDs if list is null, otherwise star does not exist in database
//                  if (list != null) {
//                    for (Star star : list) {
//                      System.out.println(star.getStarID());
//                    }
//                  } else {
//                    System.out.println(
//                        "ERROR: Star with name " + name + " does not exist in the database");
//                  }
//                }
//              }
//              break;
//            case "database":
//              try {
//                manager = new DataManager(arguments[1]);
//              } catch (SQLException e) {
//                System.err.println("SQLite error: " + e.getMessage());
//                System.exit(1);
//              }
//              break;
//            case "users":
//              //TODO: get these after loading into database
//              //create a KDTree if necessary
//              if (kdTree == null) {
//                assert manager != null;
//                List<Object> objects = manager.select("", "", Users);
//                kdTree = new KDTree(objects, 3, fields);
//              }
//              break;
//            case "similar":
//
//              if (arguments.length != 3 && arguments.length != 5) {
//                System.out.println(
//                    "ERROR: Invalid command. To run the 'similar' command, your input must be either: "
//                        + "(1) similar <k> <some_user_id> or (2) "
//                        + "similar <k> <weight in lbs> <height in inches> <age in years>");
//              }
//              int k = Integer.parseInt(arguments[1]);
//              UserKDObject target;
//              if (arguments.length == 5) {
//                int weight = Integer.parseInt(arguments[2]);
//                int height = Integer.parseInt(arguments[3]);
//                int age = Integer.parseInt(arguments[4]);
//                target = new UserKDObject(weight, height, age);
//              } else {
//                List<Object> list = manager.select("", "", Users);
//                Users user = new Users();
//                int weight = Integer.parseInt(user.getWeight());
//                int height = Integer.parseInt(user.getHeight());
//                int age = Integer.parseInt(user.getAge());
//                target = new UserKDObject(weight, height, age);
//              }
//              List<Object> kNearestNeighbors = kdTree.kNearestNeighbors(target, k);
//              break;
//            case "classify":
//              break;
//          }
        } catch (Exception e) {
//          e.printStackTrace();
          System.out.println("ERROR: We couldn't process your input");
        }
      }
    } catch (
        Exception e) {
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
