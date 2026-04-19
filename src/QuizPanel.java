import javax.swing.*;
import java.awt.*;

public class QuizPanel extends JPanel {
    private final MainFrame frame;

    private JLabel header = new JLabel("", SwingConstants.CENTER);
    private JTextArea questionArea = new JTextArea(3, 40);
    private final ButtonGroup group = new ButtonGroup();
    private final JRadioButton[] options = new JRadioButton[4];
    private final JButton prev = UIStyle.bigButton("Précédent");
    private final JButton next = UIStyle.bigButton("Suivant");
    private final JButton finish = UIStyle.bigButton("Terminer");

    private QuizEngine engine;

    public QuizPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(6,6));

        header.setFont(header.getFont().deriveFont(Font.BOLD, 22f));
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(6,6));
        questionArea.setEditable(false);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setFont(questionArea.getFont().deriveFont(19f));
        center.add(new JScrollPane(questionArea), BorderLayout.NORTH);

        JPanel answers = new JPanel(new GridLayout(4,1,6,6));
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setActionCommand(String.valueOf(i));
            UIStyle.largeRadio(options[i]);
            group.add(options[i]);
            answers.add(options[i]);
        }
        center.add(answers, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        prev.addActionListener(e -> onPrev());
        next.addActionListener(e -> onNext());
        finish.addActionListener(e -> onFinish());
        nav.add(prev);
        nav.add(next);
        nav.add(finish);
        add(nav, BorderLayout.SOUTH);
    }

    public void begin(QuizEngine engine, String categoryName) {
        this.engine = engine;
        header.setText("Catégorie : " + categoryName);
        loadCurrent();
    }

    private void loadCurrent() {
        QuizCard card = engine.getCurrent();
        questionArea.setText("Question " + (engine.getPosition()+1) + "/" + engine.size() + " : " + card.getQuestion());
        String[] c = card.getChoices();
        group.clearSelection();
        for (int i = 0; i < 4; i++) {
            options[i].setText((i+1) + ") " + c[i]);
        }
        Integer saved = engine.getCurrentAnswer();
        if (saved != null && saved >= 0 && saved <= 3) {
            options[saved].setSelected(true);
        }
        prev.setEnabled(engine.hasPrevious());
        next.setEnabled(engine.hasNext());
        finish.setEnabled(true);
        next.setVisible(engine.hasNext());
        finish.setVisible(!engine.hasNext());
        revalidate();
        repaint();
    }

    private void recordSelection() {
        ButtonModel bm = group.getSelection();
        if (bm != null) {
            int idx = Integer.parseInt(bm.getActionCommand());
            engine.setAnswer(idx);
        } else {
            engine.setAnswer(-1);
        }
    }

    private void onPrev() { recordSelection(); engine.previous(); loadCurrent(); }
    private void onNext() { recordSelection(); engine.next(); loadCurrent(); }
    private void onFinish() { recordSelection(); frame.showResults(); }
}
