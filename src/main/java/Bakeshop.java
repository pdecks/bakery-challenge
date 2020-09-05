import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// Future extensibility -> I'd have Cashier and Register classes, order ids, etc.
// placing the orders, but keeping this a bit less complicated ...
public class Bakeshop {
    private static String ABS_FILE_PATH = System.getProperty("user.dir") + "/src/main/java/products-data.json";
    private Inventory inventory;
    public Map<String, PromotionalOffer> activePromotions;

    public Bakeshop() {
        this.inventory = new Inventory();
        this.activePromotions = null;
    }

    // future extensibility -> migrate to LocalDateTime to allow time-of-day promotions
    public Order startNewOrder(LocalDate localDate) {
        return new Order(activePromotions, localDate);
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void printInventory() {
        System.out.println(inventory.toString());
    }

    // TODO -> refactor the hard-coding here
    //  ideally would want to have activePromotions map item -> List<PromotionalOffer>
    public void setPromotionalOffers() {
        activePromotions = new HashMap<>();
        // every friday -> cookies
        DayOfWeekPromotionalOffer cookiePromo = new DayOfWeekPromotionalOffer(8, true, 0.40f, DayOfWeek.FRIDAY); // 8 for $6.00
        activePromotions.put("Cookie", cookiePromo);
        System.out.println("Promotion added: Cookies on Fridays.");

        // every Oct 1 -> cheesecakes
        DayOfYearPromotionalOffer cheesecakePromo = new DayOfYearPromotionalOffer(1, false, 0.25f, 10, 1);
        activePromotions.put("Key Lime Cheesecake", cheesecakePromo);
        System.out.println("Promotion added: Cheesecakes on 10/1.");

        // every Tuesday -> donuts 2-1
        DayOfWeekPromotionalOffer donutPromo = new DayOfWeekPromotionalOffer(2, true, 0.50f, DayOfWeek.TUESDAY);
        activePromotions.put("Mini Gingerbread Donut", donutPromo);
        System.out.println("Promotion added: Donuts on Tuesdays.");
    }

    // populate inventory from products-data.json
    public void generateInventoryFromJsonFile() throws IOException {
        FileReader fileReader = new FileReader(ABS_FILE_PATH);
        JsonStreamParser jsonStreamParser = new JsonStreamParser(fileReader);

        // definitely would optimize this in the future to use annotations (like Jackson)
        // wanted to try out a different library (GSON)
        while(jsonStreamParser.hasNext()) {
            JsonObject root = jsonStreamParser.next().getAsJsonObject();
            JsonArray jsonArray = root.getAsJsonArray("treats");
            // add treats to inventory
            for (JsonElement element : jsonArray) {
                BulkPricing bulkPricing = null;
                JsonObject treatObject = element.getAsJsonObject();
                int id = treatObject.get("id").getAsInt();
                String name = treatObject.get("name").getAsString();
                String imageURL = treatObject.get("imageURL").getAsString();
                float price = treatObject.get("price").getAsFloat();

                // handle nested inner object
                if (!treatObject.get("bulkPricing").isJsonNull()) {
                    JsonObject bulkPricingObject = treatObject.getAsJsonObject("bulkPricing");
                    int amount = bulkPricingObject.get("amount").getAsInt();
                    float totalPrice = bulkPricingObject.get("totalPrice").getAsFloat();
                    bulkPricing = new BulkPricing(amount, totalPrice);
                }

                // create treat with bulk pricing
                Treat treat = new Treat(id, name, imageURL, price, bulkPricing);
                inventory.addTreat(treat);
            }
        }

        fileReader.close();
    }

}
