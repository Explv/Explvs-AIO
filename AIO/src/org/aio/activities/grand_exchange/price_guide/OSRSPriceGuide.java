package org.aio.activities.grand_exchange.price_guide;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OSRSPriceGuide {
    private static Map<Character, Integer> suffixMultipliers = new HashMap<>();
    static {
        suffixMultipliers.put('k', 1000);
        suffixMultipliers.put('m', 1_000_000);
        suffixMultipliers.put('b', 1_000_000_000);
    }

    public static Optional<Integer> getPrice(final int itemID) {
        try {
            URL url = new URL("http://services.runescape.com/m=itemdb_oldschool/api/catalogue/detail.json?item=" + itemID);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");

            try (InputStreamReader inputStreamReader = new InputStreamReader(con.getInputStream());
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                JSONParser jsonParser = new JSONParser();
                JSONObject json = (JSONObject) jsonParser.parse(bufferedReader);

                JSONObject itemJSON = (JSONObject) json.get("item");
                JSONObject currentPriceData = (JSONObject) itemJSON.get("current");

                Object currentPriceObj = currentPriceData.get("price");

                int currentPrice;

                if (currentPriceObj instanceof String) {
                    currentPrice = parsePriceStr((String) currentPriceData.get("price"));
                } else if (currentPriceObj instanceof Long){
                    currentPrice = ((Long) currentPriceObj).intValue();
                } else {
                    currentPrice = (int) currentPriceObj;
                }

                return Optional.of(currentPrice);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to get price from RuneScape api");
        }
        return Optional.empty();
    }

    private static int parsePriceStr(String priceStr) {
        priceStr = priceStr.trim();
        priceStr = priceStr.replaceAll(",", "");

        char suffix = priceStr.charAt(priceStr.length() - 1);

        if (!suffixMultipliers.containsKey(suffix)) {
            return Integer.parseInt(priceStr);
        }

        String priceNoSuffix = priceStr.substring(0, priceStr.length() - 1);

        double priceDouble = Double.parseDouble(priceNoSuffix);

        priceDouble = priceDouble * suffixMultipliers.get(suffix);

        return (int) priceDouble;
    }
}
