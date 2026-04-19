import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CategoryListPanel extends JPanel {
    private final MainFrame frame;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> list = new JList<>(model);

    public CategoryListPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(8,8));

        JLabel title = UIStyle.title("Toutes les catégories", 26f);
        add(title, BorderLayout.NORTH);

        UIStyle.tuneList(list);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton open = UIStyle.primaryButton("Ouvrir la catégorie"); // fond bleu
        open.setForeground(Color.BLACK);                              // écriture noire
        open.setMargin(new Insets(12, 36, 12, 36));                   // ~2× plus large (padding horizontal x2)
        open.addActionListener(e -> openSelected());
        add(open, BorderLayout.SOUTH);
    }

    public void refresh() {
        model.clear();
        List<Category> categories = frame.getAppState().getRepository().list();
        for (Category c : categories) {
            model.addElement(c.getName() + "  (" + c.count() + " cartes)");
        }
        list.clearSelection();
    }

    private void openSelected() {
        int idx = list.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une catégorie.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String line = model.getElementAt(idx);
        String name = line.substring(0, line.indexOf("  (")).trim();
        Category c = frame.getAppState().getRepository().getByName(name);
        if (c != null) frame.showCategoryDetail(c);
    }
}
