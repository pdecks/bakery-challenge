import java.time.LocalDate;

// Specific day for promotion, i.e. every October 1
public class DayOfYearPromotionalOffer extends PromotionalOffer {
    private int month;
    private int dayOfMonth;

    public DayOfYearPromotionalOffer(int amount, boolean requiresMultipleOfAmount, float percentDiscount, int month, int dayOfMonth) {
        super(amount, requiresMultipleOfAmount, percentDiscount);
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public boolean isValidDayOfYear(LocalDate ld) {
        return ld != null && (ld.getDayOfMonth() == dayOfMonth && ld.getMonthValue() == month);
    }

    public Float getDiscountedPrice(Treat treat, int quantity, LocalDate localDate) {
        if (!isValidDayOfYear(localDate)) return null;
        return getDiscountedPrice(treat, quantity); // from PromotionalOffer
    }
}

