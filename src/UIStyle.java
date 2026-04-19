import javax.swing.*;
import java.awt.*;

public class UIStyle {

    public static JButton bigButton(String text) {
        JButton b = new JButton(text);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 16f));
        b.setMargin(new Insets(10, 18, 10, 18));
        b.setFocusable(false);
        return b;
    }

    public static JButton mediumButton(String text) {
        JButton b = new JButton(text);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 15f));
        b.setMargin(new Insets(8, 14, 8, 14));
        b.setFocusable(false);
        return b;
    }

    public static JButton primaryButton(String text) {
        JButton b = bigButton(text);
        b.setBackground(new Color(25, 118, 210));
        b.setForeground(Color.WHITE);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        return b;
    }

    public static void tuneList(JList<?> list) {
        list.setFont(list.getFont().deriveFont(15f));
        list.setFixedCellHeight(28);
    }

    public static void tuneTable(JTable table) {
        table.setFont(table.getFont().deriveFont(14f));
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD, 14f));
        table.setRowHeight(28);
    }

    /** Radios sensiblement plus grands, avec icône personnalisée */
    public static void largeRadio(AbstractButton rb) {
        rb.setFont(rb.getFont().deriveFont(18f));
        rb.setFocusable(false);
        rb.setIcon(new RadioIcon(22, false));
        rb.setSelectedIcon(new RadioIcon(22, true));
        rb.setIconTextGap(10);
    }

    public static JLabel title(String text, float size) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(l.getFont().deriveFont(Font.BOLD, size));
        return l;
    }

    /** Icône radio ronde, 22 px par défaut */
    private static class RadioIcon implements Icon {
        private final int size;
        private final boolean selected;

        RadioIcon(int size, boolean selected) {
            this.size = size;
            this.selected = selected;
        }

        @Override public int getIconWidth() { return size; }
        @Override public int getIconHeight() { return size; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int r = size - 1;
            g2.setColor(new Color(80, 80, 80));
            g2.drawOval(x, y, r, r);

            if (selected) {
                int inset = size / 4;
                g2.setColor(new Color(25, 118, 210));
                g2.fillOval(x + inset, y + inset, size - 2 * inset, size - 2 * inset);
            }
            g2.dispose();
        }
    }
}
