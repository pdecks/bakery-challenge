import java.net.MalformedURLException;
import java.net.URL;

public class Treat {
    private int id;
    private String name;
    private URL imageUrl;
    private float price;
    private BulkPricing bulkPricing;

    public Treat(int id, String name, String imageUrl, float price, BulkPricing bulkPricing) {
        this.id = id;
        this.name = name;
        try {
            this.imageUrl = new URL(imageUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.price = price;
        this.bulkPricing = bulkPricing;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public URL getImageUrl() { return imageUrl; } // for the future, if we are fetching images and displaying them

    public float getPrice() {
        return price;
    }

    public BulkPricing getBulkPricing() {
        return bulkPricing;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Treat id: ").append(this.id).append("\n");
        sb.append("Treat name: ").append(this.name).append("\n");
        return sb.toString();
    }
}
