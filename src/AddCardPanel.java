import javax.swing.*;
import java.awt.*;

public class AddCardPanel extends JPanel {
    private final MainFrame frame;
    private Category category;

    private final JTextArea question = new JTextArea(4, 40);
    private final JTextField rep1 = new JTextField();
    private final JTextField rep2 = new JTextField();
    private final JTextField rep3 = new JTextField();
    private final JTextField rep4 = new JTextField();
    private final JSpinner goodIndex = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));

    public AddCardPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(6,6));

        JLabel title = UIStyle.title("Ajouter une carte", 24f);
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5,5,5,5);
        gc.gridx = 0; gc.gridy = 0; gc.anchor = GridBagConstraints.EAST;

        JLabel lq = new JLabel("Question :"); lq.setFont(lq.getFont().deriveFont(15f));
        form.add(lq, gc);
        gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;
        question.setLineWrap(true);
        question.setWrapStyleWord(true);
        question.setFont(question.getFont().deriveFont(15f));
        form.add(new JScrollPane(question), gc);

        gc.gridx = 0; gc.gridy++; gc.fill = GridBagConstraints.NONE; gc.weightx = 0;
        form.add(label("Réponse 1 :"), gc);
        gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;
        rep1.setFont(rep1.getFont().deriveFont(15f));
        form.add(rep1, gc);

        gc.gridx = 0; gc.gridy++; gc.fill = GridBagConstraints.NONE; gc.weightx = 0;
        form.add(label("Réponse 2 :"), gc);
        gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;
        rep2.setFont(rep2.getFont().deriveFont(15f));
        form.add(rep2, gc);

        gc.gridx = 0; gc.gridy++; gc.fill = GridBagConstraints.NONE; gc.weightx = 0;
        form.add(label("Réponse 3 :"), gc);
        gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;
        rep3.setFont(rep3.getFont().deriveFont(15f));
        form.add(rep3, gc);

        gc.gridx = 0; gc.gridy++; gc.fill = GridBagConstraints.NONE; gc.weightx = 0;
        form.add(label("Réponse 4 :"), gc);
        gc.gridx = 1; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;
        rep4.setFont(rep4.getFont().deriveFont(15f));
        form.add(rep4, gc);

        gc.gridx = 0; gc.gridy++; gc.fill = GridBagConstraints.NONE; gc.weightx = 0;
        form.add(label("Numéro de la bonne réponse (1-4) :"), gc);
        gc.gridx = 1; gc.fill = GridBagConstraints.NONE; gc.weightx = 0;
        JComponent editor = goodIndex.getEditor();
        editor.setFont(editor.getFont().deriveFont(15f));
        form.add(goodIndex, gc);

        add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel();
        JButton validate = UIStyle.bigButton("Valider");
        validate.addActionListener(e -> saveCard());
        JButton cancel = UIStyle.mediumButton("Annuler");
        cancel.addActionListener(e -> frame.showCategoryDetail(category));
        actions.add(validate);
        actions.add(cancel);
        add(actions, BorderLayout.SOUTH);
    }

    private JLabel label(String s) {
        JLabel l = new JLabel(s);
        l.setFont(l.getFont().deriveFont(15f));
        return l;
    }

    public void setCategory(Category category) { this.category = category; }

    public void resetForm() {
        question.setText("");
        rep1.setText("");
        rep2.setText("");
        rep3.setText("");
        rep4.setText("");
        goodIndex.setValue(1);
    }

    private void saveCard() {
        try {
            int idx = ((Number) goodIndex.getValue()).intValue() - 1;
            QuizCard card = new QuizCard(
                    question.getText(),
                    rep1.getText(),
                    rep2.getText(),
                    rep3.getText(),
                    rep4.getText(),
                    idx
            );
            category.addCard(card);
            try {
                Persistence.save(frame.getAppState().getRepository());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Carte ajoutée mais sauvegarde disque échouée: " + ex.getMessage(), "Avertissement", JOptionPane.WARNING_MESSAGE);
            }
            JOptionPane.showMessageDialog(this, "Carte ajoutée.");
            frame.showCategoryDetail(category);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
