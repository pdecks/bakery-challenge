import java.time.DayOfWeek;
import java.time.LocalDate;

public class DayOfWeekPromotionalOffer extends PromotionalOffer {
    // this could also be expanded to allow for things like M-F; M/W/Sat, etc.
    private DayOfWeek dayOfWeek;

    public DayOfWeekPromotionalOffer(int amount, boolean requiresMultipleOfAmount, float percentDiscount, DayOfWeek dayOfWeek) {
        super(amount, requiresMultipleOfAmount, percentDiscount);
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() { return this.dayOfWeek; }

    public boolean isValidDayOfWeek(LocalDate ld) {
        return ld != null && ld.getDayOfWeek() == dayOfWeek;
    }

    public Float getDiscountedPrice(Treat treat, int quantity, LocalDate localDate) {
        if (!isValidDayOfWeek(localDate)) return null;
        return getDiscountedPrice(treat, quantity); // from PromotionalOffer
    }
}
