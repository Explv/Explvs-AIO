package org.aio.activities.grand_exchange.item_guide;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ItemGuide {
    private static final Map<String, Integer> allGEItems = new HashMap<>();

    public static Map<String, Integer> getAllGEItems() {
        if (allGEItems.isEmpty()) {

            File summaryFile = Paths.get(System.getProperty("user.home"), "OSBot", "Data", "explv_aio_rsbuddy_summary.json").toFile();

            try {
                if (!summaryFile.exists()) {
                    System.out.println("Downloading summary JSON from RSBuddy");
                    URL website = new URL("https://storage.googleapis.com/osbuddy-exchange/summary.json");
                    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(summaryFile);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }

                try (FileReader fileReader = new FileReader(summaryFile)) {
                    JSONObject jsonObject = (JSONObject) (new JSONParser().parse(fileReader));
                    for (Object value : jsonObject.values()) {
                        JSONObject item = (JSONObject) value;
                        allGEItems.put((String) item.get("name"), new Long((long) item.get("id")).intValue());
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        return allGEItems;
    }
}
