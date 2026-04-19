import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ResultsPanel extends JPanel {
    private final MainFrame frame;
    private final JLabel title = new JLabel("Correction", SwingConstants.CENTER);
    private final JTable table = new StatusColoredTable();
    private final JLabel scoreLabel = new JLabel("", SwingConstants.CENTER);

    public ResultsPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(6,6));

        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        add(title, BorderLayout.NORTH);

        table.setFillsViewportHeight(true);
        UIStyle.tuneTable(table);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        scoreLabel.setFont(scoreLabel.getFont().deriveFont(Font.BOLD, 18f));
        south.add(scoreLabel, BorderLayout.CENTER);

        JButton back = UIStyle.mediumButton("Retour catégorie");
        back.addActionListener(e -> frame.showCategoryDetail(frame.getAppState().getSelectedCategory()));
        south.add(back, BorderLayout.EAST);

        add(south, BorderLayout.SOUTH);
    }

    public void showResults(QuizEngine engine) {
        String[] cols = {"#", "Question", "Votre réponse", "Bonne réponse", "Statut"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        int[] ua = engine.getUserAnswers();
        int score = 0;

        for (int i = 0; i < engine.getCards().size(); i++) {
            QuizCard card = engine.getCards().get(i);
            int correct = card.getCorrectIndex();
            int user = ua[i];

            String userTxt = (user >= 0) ? ((user + 1) + ") " + card.getChoices()[user]) : "(non répondu)";
            String corrTxt = (correct >= 0) ? ((correct + 1) + ") " + card.getChoices()[correct]) : "?";
            boolean ok = user == correct;
            if (ok) score++;

            model.addRow(new Object[]{
                    (i + 1),
                    card.getQuestion(),
                    userTxt,
                    corrTxt,
                    ok ? "Correct" : "Faux"
            });
        }

        table.setModel(model);
        scoreLabel.setText("Score : " + score + " / " + engine.size());
    }

    private static class StatusColoredTable extends JTable {
        @Override
        public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
            Component c = super.prepareRenderer(renderer, row, column);
            if (!isRowSelected(row)) {
                Object statusObj = getModel().getValueAt(row, 4);
                boolean ok = statusObj != null && "Correct".equals(statusObj.toString());
                Color bg = ok ? new Color(232, 255, 232) : new Color(255, 232, 232);
                Color fg = ok ? new Color(0, 120, 0) : new Color(160, 0, 0);
                c.setBackground(bg);
                c.setForeground(fg);
            } else {
                c.setBackground(getSelectionBackground());
                c.setForeground(getSelectionForeground());
            }
            return c;
        }
    }
}
