package com.mixingbowl;

import com.mixingbowl.recipe.repository.RecipeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MixingBowlApplication {

	public static void main(String[] args) {
		SpringApplication.run(MixingBowlApplication.class, args);
	}

	@Bean
	public CommandLineRunner testRecipeRepository(RecipeRepository recipeRepository) {
		return args -> {
			System.out.println("=== 전체 레시피 출력 ===");
			recipeRepository.findAll().forEach(recipe -> {
				System.out.println(recipe.getTitle());
			});

			System.out.println("=== '계란' 포함된 레시피 검색 ===");
			recipeRepository.findByIngredientsIn(List.of("계란")).forEach(System.out::println);
		};
	}

}
