package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import recipes.model.Account;
import recipes.model.Recipe;
import recipes.service.AccountService;
import recipes.service.RecipeService;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/api/recipe")
@Validated
public class RecipeController {

    private RecipeService recipeService;
    private AccountService accountService;

    @Autowired
    public void setRecipeService(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/new", produces = "application/json")
    public ResponseEntity<?> addRecipe(@AuthenticationPrincipal UserDetails user, @Valid @RequestBody Recipe recipe) {
        recipe.setAccount(accountService.getByEmail(user.getUsername()));
        Long id = recipeService.save(recipe);
        return new ResponseEntity<>(Collections.singletonMap("id", id), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeService.get(id);
        return recipe != null ? new ResponseEntity<>(recipe, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<?> getRecipes(@RequestParam(required = false) String category, @RequestParam(required = false) String name) {
        if ((category == null && name == null) || (category != null && name != null)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (category != null) {
            return new ResponseEntity<>(recipeService.getByCategory(category), HttpStatus.OK);
        }

        return new ResponseEntity<>(recipeService.getByName(name), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteRecipe(@AuthenticationPrincipal UserDetails user, @PathVariable Long id) {
        Recipe recipe = recipeService.get(id);
        if (recipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Account account = accountService.getByEmail(user.getUsername());
        if (!recipe.getAccount().getId().equals(account.getId())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        recipeService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateRecipe(@AuthenticationPrincipal UserDetails user, @PathVariable Long id, @Valid @RequestBody Recipe recipe) {
        Recipe curRecipe = recipeService.get(id);
        if (curRecipe == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Account account = accountService.getByEmail(user.getUsername());
        if (!account.containsRecept(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        recipe.setId(id);
        recipe.setAccount(account);
        recipeService.save(recipe);
        return new ResponseEntity<>(Collections.singletonMap("id", recipe.getId()), HttpStatus.NO_CONTENT);
    }
}
