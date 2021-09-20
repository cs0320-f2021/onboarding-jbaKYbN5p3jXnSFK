package edu.brown.cs.student.main;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

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

  public List<Star> starData = new ArrayList<>(); //create empty arrayList of Stars

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

    // TODO: Add your REPL here!
    try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
      String input;
      while ((input = br.readLine()) != null) {
        try {
          input = input.trim();
          String[] arguments = input.split(" ");
          //System.out.println(arguments[0]);
          // TODO: complete your REPL by adding commands for addition "add" and subtraction
          //  "subtract"
          MathBot math = new MathBot();
          try {
            if (arguments[0].equals("add")) {
              double number1 = Double.parseDouble(arguments[1]); //too repetitive?
              double number2 = Double.parseDouble(arguments[2]);
              System.out.println(math.add(number1, number2));
            }
            else if (arguments[0].equals("subtract")) {
              double number1 = Double.parseDouble(arguments[1]);
              double number2 = Double.parseDouble(arguments[2]);
              System.out.println(math.subtract(number1, number2));
            }
            else if (arguments[0].equals("stars")) {
              try { //catch invalid pathway
                BufferedReader csvReader = new BufferedReader(new FileReader(arguments[1])); //read in from file
                BufferedWriter csvWriter = new BufferedWriter(new FileWriter(arguments[1], false));
                String row; //define row
                while ((row = csvReader.readLine()) != null) { //read line in while loop
                  String[] data = row.split(","); //split line into array of data
                  int index = Integer.parseInt(data[0]);
                  String name = data[1];
                  double x = Double.parseDouble(data[2]);
                  double y = Double.parseDouble(data[3]);
                  double z = Double.parseDouble(data[4]);
                  Star newStar = new Star(index, name, x, y, z, 10000); //create new star
                  starData.add(newStar); //add array to existing array
                  csvWriter.write(row); //write the row that was just read
                }
                csvReader.close();
                csvWriter.flush(); //write everything stored in bufferedWriter
                csvWriter.close();
              }
              catch (Exception e) {
                System.out.println("Error: Invalid arguments");
              }
            }
            else if (arguments[0].equals("naive_neighbors")) {
              if (arguments[1].indexOf('"') == 0) { //first character is quotation
                for(Star starDatum : starData) {
                  if(arguments[2].equals(starDatum.starName)) {
                    for(Star starDatum2 : starData) {
                      starDatum2.distanceGivenCoor(starDatum.xCoor, starDatum.yCoor, starDatum.zCoor);
                    }
                  }
                }
              } else {
                double x = Double.parseDouble(arguments[2]); //string to double, store coordinates
                double y = Double.parseDouble(arguments[3]);
                double z = Double.parseDouble(arguments[4]);
                for (Star starDatum : starData) { //iterate through the array list made during star command
                  starDatum.distanceGivenCoor(x, y, z); //for each iteration, store their distance from the given coordinates
                }//end for loop because each starDatum is populated
              }
              //create a sorted list
              List<Star> sorted = starData.stream().sorted(Comparator.comparingDouble(Star::getDistance)).collect(Collectors.toList());
              //print out elements 0 through index k
              System.out.println(sorted.subList(0, Integer.parseInt(arguments[1])));
            }
            else {
              System.out.println("Error: Command not recognized");
            }
          } catch (Exception e) {
            System.out.println("Error: Inputs are not 2 numbers");
          }
        } catch (Exception e) {
          // e.printStackTrace();
          System.out.println("ERROR: We couldn't process your input");
        }
      }
    } catch (Exception e) {
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
