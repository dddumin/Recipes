package recipes.service;

import recipes.model.Recipe;

import java.util.List;

public interface RecipeService {
    Long save(Recipe recipe);
    Recipe get(Long id);
    List<Recipe> getByCategory(String category);
    List<Recipe> getByName(String name);
    void delete(Long id);
    List<Recipe> getAll();
    void deleteAll();
}
