import static spark.Spark.post;

import java.net.URI;
import java.util.HashMap;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;

/**
 * A service which stores TwiML XML data or every order ID.
 * Heroku setup thanks to:
 * https://junhopark.com/posts/2016/08/10/...
 * ...writing-and-deploying-a-simple-application-using-java-8-spark-gradle-heroku-intellij-idea
 */
public class Server {

    HashMap<String, String> map;

    // Find your Account Sid, Token and phone number used at twilio.com/console
    public static final String ACCOUNT_SID = "AC08ed603e3a4de8c0055e27ed8f5e8a3e";
    public static final String AUTH_TOKEN = "97fbd0228fa8419cb931583626039e00";
    public static final String OUTGOING_PHONE = "+16123245532";

    // Country code specific to Singapore at the moment
    public static final String LOCAL_COUNTRY_CODE = "+65";
    public static final String ORDER_PATH = "order/";
    public static final String REMOTE_SERVER = "https://mysterious-temple-83678.herokuapp.com/";


    public Server() {
        map = new HashMap<>();
        handleCreateOrder();
        handleReleaseOrder();
    }


    private void handleCreateOrder() {
        // Stores order data in memory and initiate call
        post("/create/:orderId", (req, res) -> {

            String id = req.params(":orderId");

            // Decode message
            String data = req.body();
            String[] elements = data.split("//");
            String toPhone = elements[0];
            String message = elements[1];

            createOrder(id, message);
            order(id, toPhone);
            return "SUCCESS";
        });
    }


    private void handleReleaseOrder() {
        // Releases data to TwiML
        post("/order/:orderId", (req, res) -> map.get(req.params(":orderId")));
    }

    /**
     * Uses Twilio API to begin call and order {@code Food}
     */
    public void order(String orderId, String phoneNumber) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            String to = LOCAL_COUNTRY_CODE + phoneNumber;
            String from = OUTGOING_PHONE;

            Call.creator(new PhoneNumber(to), new PhoneNumber(from),
                    new URI(REMOTE_SERVER + ORDER_PATH + orderId)).create();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *  Use TwiML to generate speech
     *  Say Hello. Wait for response. Say order. Wait for response. Say Thank you.
     */
    private void createOrder(String id, String speech) {

        Say say  = new Say.Builder(
                speech)
                .build();
        VoiceResponse voiceResponse = new VoiceResponse.Builder()
                .say(say)
                .build();

        map.put(id, voiceResponse.toXml());
    }
}
