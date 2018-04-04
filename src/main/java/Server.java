import static spark.Spark.post;
import static spark.Spark.port;

import java.util.HashMap;

/**
 * A service which stores TwiML XML data or every order ID.
 * Heroku setup thanks to:
 * https://junhopark.com/posts/2016/08/10/...
 * ...writing-and-deploying-a-simple-application-using-java-8-spark-gradle-heroku-intellij-idea
 */
public class Server {

    /**
     * Stores new order data for TwiML to fetch (via POST method)
     * @param args
     */
    public static void main(String[] args) {
        // In order for this to work on Heroku, we need to allow Heroku to set the port number
        final String portNumber = System.getenv("PORT");
        if (portNumber != null) {
            port(Integer.parseInt(portNumber));
        }

        HashMap<String, String> map = new HashMap<>();

        // Stores order data in memory
        post("/create/:orderId", (req, res) -> {
            String id = req.params(":orderId");
            map.put(id, req.body());
            return "SUCCESS";
        });

        // Releases data to TwiML
        post("/order/:orderId", (req, res) -> map.get(req.params(":orderId")));
    }
}
