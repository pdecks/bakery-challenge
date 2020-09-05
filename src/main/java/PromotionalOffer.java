// i.e. 2-for-1, save 30% on 5 or more, etc.
public class PromotionalOffer {
    protected int amount;
    protected boolean requiresMultipleOfAmount;
    protected float percentDiscount;

    public PromotionalOffer(int amount, boolean requiresMultipleOfAmount, float percentDiscount) {
        this.amount = amount; // min amount required
        this.requiresMultipleOfAmount = requiresMultipleOfAmount;
        this.percentDiscount = percentDiscount;
    }

    public boolean isEligibleQuantity(int quantity) {
        return quantity >= amount;
    }
    public Float getDiscountedPrice(Treat treat, int quantity) {
        if (!isEligibleQuantity(quantity)) return null;

        // i.e. buy 2-get-1 free -> 3 @ 30% off, 4th 100%
        if (requiresMultipleOfAmount) {
            float eligiblePrice = (float) (quantity / amount) * amount * treat.getPrice() * (1.0f - percentDiscount);
            float ineligiblePrice = (float) (quantity % amount) * treat.getPrice();
            return eligiblePrice + ineligiblePrice;
        }
        // i.e. 20% off when you buy 3 or more
        return (float) quantity * treat.getPrice() * (1.0f - percentDiscount);
    }
}
