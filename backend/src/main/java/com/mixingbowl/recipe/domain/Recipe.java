package com.mixingbowl.recipe.domain;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "recipes")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class Recipe {

    @Id
    private String id;

    private String title;
    private String description;
    private List<String> ingredients;
    private String imageUrl;

    // 추후 AI 추천용 벡터
    private List<Double> embedding;


}