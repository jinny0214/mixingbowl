package com.mixingbowl.recipe.repository;

import com.mixingbowl.recipe.domain.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecipeRepository extends MongoRepository<Recipe, String> {

    List<Recipe> findByTitleContaining(String keyword);

    List<Recipe> findByIngredientsIn(List<String> ingredients);

}
