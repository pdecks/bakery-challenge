import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private Map<String, PromotionalOffer> activePromotions;
    private LocalDate orderDate;
    private Map<Treat, QuantityPrice> treatOrders; // treat -> (quantity, price)
    private float orderTotal;

    class QuantityPrice {
        private final int quantity;
        private final float calculatedPrice;

        public QuantityPrice(int quantity, float calculatedPrice) {
            this.quantity = quantity;
            this.calculatedPrice = calculatedPrice;
        }

        public int getQuantity() { return this.quantity; }

        public float getCalculatedPrice() { return this.calculatedPrice; };
    }

    public float getOrderTotal() {
        return orderTotal;
    }

    public Order(Map<String, PromotionalOffer> activePromotions, LocalDate dateOverride) {
        this.activePromotions = activePromotions;
        if (dateOverride != null) this.orderDate = dateOverride;
        else this.orderDate = LocalDate.now();
        this.treatOrders = new HashMap<>();
        this.orderTotal = 0.00f;
    }

    public void addTreat(Treat treat, int quantity) {
        // if treat exists, assume we are adding to the quantity for a duplicate entry
        QuantityPrice quantityPrice = calculateTotalPrice(treat, quantity);
        treatOrders.put(treat, quantityPrice);
        orderTotal += quantityPrice.getCalculatedPrice();
    }

    // future extensibility
    public void updateQuantity(Treat treat, int quantity) {
    }

    // we'll take the smallest of the resulting discounts, not allowing discounts to be combined
    public QuantityPrice calculateTotalPrice(Treat treat, int quantity) {
        Float promoPrice = null;
        Float bulkPrice = null;
        Float regularPrice;

        // check if treat has active promotion
        PromotionalOffer activePromo = activePromotions.get(treat.getName());
        if (activePromo != null) {
            if (activePromo instanceof DayOfYearPromotionalOffer) {
                promoPrice = ((DayOfYearPromotionalOffer) activePromo).getDiscountedPrice(treat, quantity, orderDate);
            }
            else if (activePromo instanceof DayOfWeekPromotionalOffer) {
                promoPrice = ((DayOfWeekPromotionalOffer) activePromo).getDiscountedPrice(treat, quantity, orderDate);
            }
            else {
                promoPrice = activePromo.getDiscountedPrice(treat, quantity);
            }
        }

        // check if bulk pricing applies
        BulkPricing bp = treat.getBulkPricing();
        if (bp != null && bp.isValidBulkQuantity(quantity)) {
            bulkPrice = treat.getBulkPricing().getBulkPriceForQuantity(quantity, treat.getPrice());
        }
        regularPrice = treat.getPrice() * quantity;

        // return minimum of potential prices
        float minPrice = regularPrice;
        if (promoPrice != null) {
            minPrice = Math.min(minPrice, promoPrice);
        }
        if (bulkPrice != null) {
            minPrice = Math.min(minPrice, bulkPrice);
        }

        return new QuantityPrice(quantity, minPrice);
    }
}
