package com.SharpDevs.Recipe.Mania.Service;

import com.SharpDevs.Recipe.Mania.domain.DTO.RecipeDto;
import com.SharpDevs.Recipe.Mania.domain.Entity.RecipeEntity;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface RecipeService {
    public ResponseEntity<RecipeDto> addRecipe(RecipeDto recipeDto);

    public ResponseEntity<Iterable<RecipeDto>> getAllRecipe();

    public ResponseEntity<RecipeDto> getRecipe(Long id);

    public ResponseEntity<RecipeDto> updateRecipe (Long id, RecipeDto recipeDto);


    @Transactional
    ResponseEntity<HttpStatus> deleteRecipe(Long id);
}
