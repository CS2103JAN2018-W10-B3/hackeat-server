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
        port(4569);

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
