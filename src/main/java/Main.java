import static spark.Spark.port;

public class Main {
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

        Server server = new Server();
    }
}
