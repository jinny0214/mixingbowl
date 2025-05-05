package com.mixingbowl.recipe.service;

import com.mixingbowl.recipe.domain.Recipe;
import com.mixingbowl.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> findByIngredients(List<String> ingredients) {
        return recipeRepository.findByIngredientsIn(ingredients);
    }
}
