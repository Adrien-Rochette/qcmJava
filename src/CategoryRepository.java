import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryRepository {
    private final List<Category> categories = new ArrayList<>();

    public boolean addCategory(String name) {
        if (name == null) return false;
        String trimmed = name.trim();
        if (trimmed.isEmpty()) return false;
        if (getByName(trimmed) != null) return false;
        categories.add(new Category(trimmed));
        Collections.sort(categories, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return true;
    }

    public List<Category> list() {
        return Collections.unmodifiableList(categories);
    }

    public Category getByName(String name) {
        for (Category c : categories) {
            if (c.getName().equalsIgnoreCase(name)) return c;
        }
        return null;
    }

    // Chargement depuis Persistence
    public void loadFrom(List<Category> src) {
        categories.clear();
        categories.addAll(src);
        Collections.sort(categories, (a, b) -> a.getName().compareToIgnoreCase(b.getName()));
    }
}
