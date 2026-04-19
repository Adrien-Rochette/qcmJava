public class QuizCard {
    private final String question;
    private final String[] choices; // taille 4
    private final int correctIndex; // 0..3

    public QuizCard(String question, String rep1, String rep2, String rep3, String rep4, int correctIndex) {
        if (question == null || question.trim().isEmpty())
            throw new IllegalArgumentException("La question est vide.");
        this.question = question.trim();

        choices = new String[4];
        choices[0] = validate(rep1, "Réponse 1");
        choices[1] = validate(rep2, "Réponse 2");
        choices[2] = validate(rep3, "Réponse 3");
        choices[3] = validate(rep4, "Réponse 4");

        if (correctIndex < 0 || correctIndex > 3)
            throw new IllegalArgumentException("Index de la bonne réponse hors limites.");
        this.correctIndex = correctIndex;
    }

    private String validate(String s, String label) {
        if (s == null || s.trim().isEmpty())
            throw new IllegalArgumentException(label + " est vide.");
        return s.trim();
    }

    public String getQuestion() {
        return question;
    }

    public String[] getChoices() {
        return choices.clone();
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
}
