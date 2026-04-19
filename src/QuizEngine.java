import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizEngine {
    private final List<QuizCard> cards;
    private final List<Integer> order;
    private final int[] userAnswers; // -1 si non répondu
    private int position = 0;

    public QuizEngine(List<QuizCard> cards, boolean shuffle) {
        this.cards = new ArrayList<>(cards);
        this.order = new ArrayList<>();
        for (int i = 0; i < this.cards.size(); i++) order.add(i);
        if (shuffle) Collections.shuffle(order);
        this.userAnswers = new int[cards.size()];
        for (int i = 0; i < userAnswers.length; i++) userAnswers[i] = -1;
    }

    public int size() { return cards.size(); }

    public int getPosition() { return position; }

    public void setAnswer(int answerIndex) {
        if (answerIndex < -1 || answerIndex > 3) return;
        userAnswers[order.get(position)] = answerIndex;
    }

    public Integer getCurrentAnswer() {
        return userAnswers[order.get(position)];
    }

    public QuizCard getCurrent() {
        return cards.get(order.get(position));
    }

    public boolean hasNext() { return position < size() - 1; }

    public boolean hasPrevious() { return position > 0; }

    public void next() {
        if (hasNext()) position++;
    }

    public void previous() {
        if (hasPrevious()) position--;
    }

    public int computeScore() {
        int score = 0;
        for (int i = 0; i < cards.size(); i++) {
            if (userAnswers[i] == cards.get(i).getCorrectIndex()) score++;
        }
        return score;
    }

    public int[] getUserAnswers() {
        return userAnswers.clone();
    }

    public List<QuizCard> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
