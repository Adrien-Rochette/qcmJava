public class AppState {
    private final CategoryRepository repository;
    private Category selectedCategory;
    private QuizEngine engine;

    public AppState() {
        this.repository = Persistence.load();
    }

    public CategoryRepository getRepository() {
        return repository;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public QuizEngine getEngine() {
        return engine;
    }

    public void setEngine(QuizEngine engine) {
        this.engine = engine;
    }
}
