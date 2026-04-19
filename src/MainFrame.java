import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel center = new JPanel(cardLayout);

    private final MainMenuPanel mainMenuPanel;
    private final CategoryListPanel categoryListPanel;
    private final CategoryDetailPanel categoryDetailPanel;
    private final AddCardPanel addCardPanel;
    private final QuizPanel quizPanel;
    private final ResultsPanel resultsPanel;

    private final AppState appState = new AppState();

    public MainFrame() {
        super("QCM interactif");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 720));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JButton menuButton = UIStyle.mediumButton("Menu");
        menuButton.addActionListener(e -> showHome());

        JPanel left = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.insets = new Insets(8,8,8,8);
        left.add(menuButton, gc);
        left.setPreferredSize(new Dimension(110, 100));
        add(left, BorderLayout.WEST);

        mainMenuPanel = new MainMenuPanel(this);
        categoryListPanel = new CategoryListPanel(this);
        categoryDetailPanel = new CategoryDetailPanel(this);
        addCardPanel = new AddCardPanel(this);
        quizPanel = new QuizPanel(this);
        resultsPanel = new ResultsPanel(this);

        center.add(mainMenuPanel, "HOME");
        center.add(categoryListPanel, "CATEGORIES");
        center.add(categoryDetailPanel, "CATEGORY_DETAIL");
        center.add(addCardPanel, "ADD_CARD");
        center.add(quizPanel, "QUIZ");
        center.add(resultsPanel, "RESULTS");

        add(center, BorderLayout.CENTER);
        showHome();
    }

    public void showHome() {
        mainMenuPanel.refresh();
        cardLayout.show(center, "HOME");
    }

    public void showCategories() {
        categoryListPanel.refresh();
        cardLayout.show(center, "CATEGORIES");
    }

    public void showCategoryDetail(Category category) {
        appState.setSelectedCategory(category);
        categoryDetailPanel.refresh();
        cardLayout.show(center, "CATEGORY_DETAIL");
    }

    public void showAddCard() {
        addCardPanel.setCategory(appState.getSelectedCategory());
        addCardPanel.resetForm();
        cardLayout.show(center, "ADD_CARD");
    }

    public void startQuiz(boolean shuffle, int limit) {
        Category c = appState.getSelectedCategory();
        if (c == null) {
            JOptionPane.showMessageDialog(this, "Aucune catégorie sélectionnée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<QuizCard> all = c.getCards();
        if (all.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cette catégorie ne contient aucune carte.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<QuizCard> selected = new ArrayList<>(all);
        if (shuffle) Collections.shuffle(selected);
        if (limit > 0 && limit < selected.size()) {
            selected = new ArrayList<>(selected.subList(0, limit));
        }

        appState.setEngine(new QuizEngine(selected, shuffle));
        quizPanel.begin(appState.getEngine(), c.getName());
        cardLayout.show(center, "QUIZ");
    }

    public void showResults() {
        QuizEngine engine = appState.getEngine();
        if (engine == null) return;
        resultsPanel.showResults(engine);
        cardLayout.show(center, "RESULTS");
    }

    public AppState getAppState() {
        return appState;
    }
}
