import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private final MainFrame frame;
    private final JTextField newCategoryField = new JTextField();

    public MainMenuPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(8,8));

        JLabel title = UIStyle.title("Choisir catégorie", 30f);
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6,6,6,6);
        gc.gridx = 0; gc.gridy = 0; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1.0;

        JButton openCategories = UIStyle.bigButton("Voir les catégories");
        openCategories.addActionListener(e -> frame.showCategories());
        center.add(openCategories, gc);

        gc.gridy++;
        JPanel addPanel = new JPanel(new BorderLayout(8,0));
        newCategoryField.setToolTipText("Nom de la catégorie");
        newCategoryField.setFont(newCategoryField.getFont().deriveFont(15f));
        addPanel.add(newCategoryField, BorderLayout.CENTER);
        JButton add = UIStyle.bigButton("Ajouter une catégorie");
        add.addActionListener(e -> addCategory());
        addPanel.add(add, BorderLayout.EAST);
        center.add(addPanel, gc);

        add(center, BorderLayout.CENTER);
    }

    private void addCategory() {
        String name = newCategoryField.getText();
        boolean ok = frame.getAppState().getRepository().addCategory(name);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Nom invalide ou déjà utilisé.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            Persistence.save(frame.getAppState().getRepository());
            newCategoryField.setText("");
            frame.showCategories();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Échec de sauvegarde: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {}
}
