public class BulkPricing {
    private float totalPrice;
    private int amount;

    public BulkPricing(int amount, float totalPrice) {
        this.amount = amount;
        this.totalPrice = totalPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isValidBulkQuantity(int quantity) {
        return quantity / amount != 0;
    }

    public float getBulkPriceForQuantity(int quantity, float regularPrice) {
        float bulkDiscountedPrice = (float) (quantity / amount) * totalPrice;
        float undiscountedPrice = (float) (quantity % amount) * regularPrice;
        return bulkDiscountedPrice + undiscountedPrice;
    }
}
