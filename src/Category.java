import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Category {
    private final String name;
    private final List<QuizCard> cards = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<QuizCard> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public void addCard(QuizCard card) {
        if (card != null) {
            cards.add(card);
        }
    }

    public int count() {
        return cards.size();
    }
}
