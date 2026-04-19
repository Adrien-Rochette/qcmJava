import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Persistence {
    // Modifiez au besoin. Utilisable aussi via -Dqcm.root="C:\\chemin"
    public static final String ROOT = System.getProperty(
            "qcm.root",
            "C:\\\\Users\\\\adrie\\\\IdeaProjects\\\\QCM\\\\src"
    );

    private static final String INDEX = "categories.index";
    private static final String PREFIX = "category_";
    private static final String SUFFIX = ".qcm";
    private static final String MAGIC = "#QCM v1";

    /* ---------- API publique ---------- */

    public static CategoryRepository load() {
        ensureRoot();
        CategoryRepository repo = new CategoryRepository();
        Path indexPath = Paths.get(ROOT, INDEX);
        if (!Files.exists(indexPath)) return repo;

        try (BufferedReader br = Files.newBufferedReader(indexPath, StandardCharsets.UTF_8)) {
            String line;
            List<Category> loaded = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String name = line;
                Category c = loadCategoryFile(name);
                if (c != null) loaded.add(c);
            }
            repo.loadFrom(loaded);
        } catch (IOException e) {
            // Lecture échouée: on retourne ce qui est possible
        }
        return repo;
    }

    public static void save(CategoryRepository repo) throws IOException {
        ensureRoot();
        // Index
        Path indexPath = Paths.get(ROOT, INDEX);
        try (BufferedWriter bw = Files.newBufferedWriter(indexPath, StandardCharsets.UTF_8)) {
            bw.write("# Index des catégories\n");
            for (Category c : repo.list()) {
                bw.write(c.getName());
                bw.write("\n");
            }
        }
        // Fichiers de catégories
        for (Category c : repo.list()) {
            saveCategoryFile(c);
        }
    }

    /* ---------- Détails format fichier catégorie ---------- */

    private static void saveCategoryFile(Category c) throws IOException {
        String base = sanitize(c.getName());
        Path p = Paths.get(ROOT, PREFIX + base + SUFFIX);
        try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8)) {
            bw.write(MAGIC); bw.write("\n");
            bw.write("name="); bw.write(escape(c.getName())); bw.write("\n");
            for (QuizCard card : c.getCards()) {
                bw.write("card\n");
                bw.write("Q=");  bw.write(escape(card.getQuestion())); bw.write("\n");
                String[] ch = card.getChoices();
                bw.write("A1="); bw.write(escape(ch[0])); bw.write("\n");
                bw.write("A2="); bw.write(escape(ch[1])); bw.write("\n");
                bw.write("A3="); bw.write(escape(ch[2])); bw.write("\n");
                bw.write("A4="); bw.write(escape(ch[3])); bw.write("\n");
                bw.write("C=");  bw.write(String.valueOf(card.getCorrectIndex()+1)); bw.write("\n");
            }
        }
    }

    private static Category loadCategoryFile(String nameFromIndex) {
        String base = sanitize(nameFromIndex);
        Path p = Paths.get(ROOT, PREFIX + base + SUFFIX);
        if (!Files.exists(p)) return null;

        try (BufferedReader br = Files.newBufferedReader(p, StandardCharsets.UTF_8)) {
            String line = br.readLine();
            if (line == null || !MAGIC.equals(line.trim())) return null;

            String realName = nameFromIndex;
            List<QuizCard> cards = new ArrayList<>();
            String q = null, a1 = null, a2 = null, a3 = null, a4 = null;
            Integer cidx = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("name=")) {
                    realName = unescape(line.substring("name=".length()));
                } else if (line.equals("card")) {
                    // marqueur début de carte, réinitialiser tampons
                    q = a1 = a2 = a3 = a4 = null; cidx = null;
                } else if (line.startsWith("Q=")) {
                    q = unescape(line.substring(2));
                } else if (line.startsWith("A1=")) {
                    a1 = unescape(line.substring(3));
                } else if (line.startsWith("A2=")) {
                    a2 = unescape(line.substring(3));
                } else if (line.startsWith("A3=")) {
                    a3 = unescape(line.substring(3));
                } else if (line.startsWith("A4=")) {
                    a4 = unescape(line.substring(3));
                } else if (line.startsWith("C=")) {
                    try {
                        int oneBased = Integer.parseInt(line.substring(2).trim());
                        cidx = oneBased - 1;
                        // Dès qu'on a C, la carte est complète
                        if (q != null && a1 != null && a2 != null && a3 != null && a4 != null && cidx != null) {
                            cards.add(new QuizCard(q, a1, a2, a3, a4, cidx));
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
            Category cat = new Category(realName);
            for (QuizCard card : cards) cat.addCard(card);
            return cat;
        } catch (Exception e) {
            return null;
        }
    }

    /* ---------- Utilitaires ---------- */

    private static void ensureRoot() {
        try {
            Files.createDirectories(Paths.get(ROOT));
        } catch (IOException ignored) {}
    }

    private static String sanitize(String name) {
        String s = name.trim().toLowerCase(Locale.ROOT);
        s = s.replaceAll("[^a-z0-9_-]+", "_");
        if (s.isEmpty()) s = "categorie";
        return s;
    }

    private static String escape(String s) {
        // Échapper \ et \n pour rester monoligne
        return s.replace("\\", "\\\\").replace("\n", "\\n").trim();
    }

    private static String unescape(String s) {
        // Inverse de escape, en deux passes
        String tmp = s.replace("\\n", "\n");
        return tmp.replace("\\\\", "\\");
    }
}
