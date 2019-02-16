package activities.grand_exchange.price_guide;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class RSBuddyPriceGuide {
    public static Optional<Integer> getBuyPrice(final int itemID) {
        return getPrice(itemID, "buying");
    }

    public static Optional<Integer> getSellPrice(final int itemID) {
        return getPrice(itemID, "selling");
    }

    private static Optional<Integer> getPrice(final int itemID, final String type) {
        try {
            URL url = new URL("http://api.rsbuddy.com/grandExchange?a=guidePrice&i=" + itemID);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

            try (InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                JSONParser jsonParser = new JSONParser();
                JSONObject itemJSON = (JSONObject) jsonParser.parse(bufferedReader);

                int price = new Long((long) itemJSON.get(type)).intValue();

                System.out.println("Got RSBuddy price");
                return Optional.of(price);
            }
        } catch (Exception e) {
            System.out.println("Failed to get RSBuddy price");
        }
        return Optional.empty();
    }
}
