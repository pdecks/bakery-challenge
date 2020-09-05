import java.util.HashMap;
import java.util.Map;

public class Inventory {
    // no need to completely over-optimize right now, but for larger inventory tradeoffs w/ordered structure relevant
    public Map<Integer, Treat> treats;

    public Inventory() {
        this.treats = new HashMap<>();
    }

    public Treat getTreatById(int id) {
        return treats.get(id);
    }

    public void addTreat(Treat treat) {
        if (treat != null) {
            treats.put(treat.getId(), treat);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inventory: ").append(treats.size());
        if (treats.size() == 0) {
            sb.append(" treats.");
            return sb.toString();
        }
        if (treats.size() == 1) sb.append(" treat\n");
        else sb.append(" treats\n");
        for(Map.Entry<Integer, Treat> entry : treats.entrySet()) {
            sb.append(entry.getValue().toString());
        }
        sb.append("\n");
        return sb.toString();
    }
}
