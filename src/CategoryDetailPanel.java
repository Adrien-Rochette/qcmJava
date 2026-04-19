import javax.swing.*;
import java.awt.*;

public class CategoryDetailPanel extends JPanel {
    private final MainFrame frame;
    private final JLabel header = new JLabel("", SwingConstants.CENTER);
    private final JLabel count = new JLabel("", SwingConstants.CENTER);
    private final JCheckBox shuffle = new JCheckBox("Ordre aléatoire", true);
    private final JSpinner questionCount = new JSpinner(new SpinnerNumberModel(1, 1, 1, 1));
    private final JButton start = UIStyle.bigButton("Commencer");

    public CategoryDetailPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(6,6));

        header.setFont(header.getFont().deriveFont(Font.BOLD, 28f));
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.gridx = 0; gc.gridy = 0; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;

        count.setFont(count.getFont().deriveFont(16f));
        center.add(count, gc);

        gc.gridy++;
        JPanel qty = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JLabel qtyLabel = new JLabel("Nombre de questions :");
        qtyLabel.setFont(qtyLabel.getFont().deriveFont(15f));
        questionCount.setFont(questionCount.getFont().deriveFont(15f));
        qty.add(qtyLabel);
        qty.add(questionCount);
        center.add(qty, gc);

        gc.gridy++;
        JButton addCard = UIStyle.bigButton("Ajouter une carte");
        addCard.addActionListener(e -> frame.showAddCard());
        center.add(addCard, gc);

        gc.gridy++;
        shuffle.setFont(shuffle.getFont().deriveFont(15f));
        center.add(shuffle, gc);

        gc.gridy++;
        start.addActionListener(e -> frame.startQuiz(
                shuffle.isSelected(),
                ((Number) questionCount.getValue()).intValue()
        ));
        center.add(start, gc);

        add(center, BorderLayout.CENTER);
    }

    public void refresh() {
        Category c = frame.getAppState().getSelectedCategory();
        if (c == null) return;
        header.setText(c.getName());
        int total = c.count();
        count.setText("Cartes disponibles : " + total);

        if (total <= 0) {
            questionCount.setModel(new SpinnerNumberModel(0, 0, 0, 1));
            questionCount.setEnabled(false);
            start.setEnabled(false);
        } else {
            int current = Math.min(total, (Integer) ((SpinnerNumberModel) questionCount.getModel()).getNumber().intValue());
            questionCount.setModel(new SpinnerNumberModel(current <= 0 ? total : current, 1, total, 1));
            questionCount.setEnabled(true);
            start.setEnabled(true);
        }
    }
}
