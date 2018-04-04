import static spark.Spark.port;
import static spark.Spark.post;

import java.util.HashMap;

/**
 * A service which stores TwiML XML data or every order ID.
 */
public class Server {

    /**
     * Stores new order data for TwiML to fetch (via POST method)
     * @param args
     */
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
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

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
