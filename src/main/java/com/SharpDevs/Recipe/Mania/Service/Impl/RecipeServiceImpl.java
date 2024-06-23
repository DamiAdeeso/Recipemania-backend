package com.SharpDevs.Recipe.Mania.Service.Impl;

import com.SharpDevs.Recipe.Mania.Repository.CategoryRepository;
import com.SharpDevs.Recipe.Mania.Repository.RecipeRepository;
import com.SharpDevs.Recipe.Mania.Repository.UserRepository;
import com.SharpDevs.Recipe.Mania.Service.AwsService;
import com.SharpDevs.Recipe.Mania.Service.RecipeService;
import com.SharpDevs.Recipe.Mania.domain.DTO.RecipeDto;
import com.SharpDevs.Recipe.Mania.domain.DTO.RecipeOperationsDto;
import com.SharpDevs.Recipe.Mania.domain.Entity.CategoryEntity;
import com.SharpDevs.Recipe.Mania.domain.Entity.RecipeEntity;
import com.SharpDevs.Recipe.Mania.domain.Entity.UserEntity;
import com.SharpDevs.Recipe.Mania.domain.Mappers.Mapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService  {

   private final Mapper<RecipeEntity, RecipeOperationsDto> recipeOperationsMapper;

    private final Mapper<RecipeEntity, RecipeDto> recipeDtoMapper;
   private final UserRepository userRepository;

   private final CategoryRepository categoryRepository;

   private  final RecipeRepository recipeRepository;

   private final JWTServiceImpl jwtService;

   private final AwsService awsService;

    @Override
    public ResponseEntity<RecipeDto> addRecipe(RecipeOperationsDto recipeOperationsDto)  {
        try {
            Long userId = jwtService.getUserId();
            if(userRepository.existsById(userId)){
                UserEntity existingUser  = userRepository.findById(userId).orElse(null);
                RecipeEntity recipeEntity = recipeOperationsMapper.mapFrom(recipeOperationsDto);
                if (recipeEntity != null) {
                   CategoryEntity foundCategory =  categoryRepository.findById(recipeOperationsDto.getCategoryId()).orElse(null);
                    if(foundCategory!=null){
                        recipeEntity.setUser(existingUser);
                        recipeEntity.setCategory(foundCategory);
                    }
                    try{
                    if(recipeOperationsDto.getFeaturedImage()!=null){
                        recipeEntity.setFeatured_image(awsService.saveObjects(recipeOperationsDto.getFeaturedImage()));
                    }
                    }catch(Exception ex){
                        throw new FileUploadException(ex.getMessage());
                    }
                    RecipeEntity  savedRecipeEntity = recipeRepository.save(recipeEntity);
                   RecipeDto recipeDto =  recipeDtoMapper.mapTo(savedRecipeEntity);
                    return new ResponseEntity<>(recipeDto,HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }else{
                throw new RuntimeException("User does not exist");
            }

        }catch(Exception e){
            throw new RuntimeException(e + "Encountered an error while saving recipe");
        }
    }

    @Override
    public ResponseEntity<Iterable<RecipeOperationsDto>> getAllRecipe() {
        try{
            Iterable<RecipeEntity> allRecipeList = recipeRepository.findAll(PageRequest.of(0,2));
            return new ResponseEntity<>(recipeOperationsMapper.mapListTo(allRecipeList),HttpStatus.OK);
        }catch (Exception err) {
            throw new RuntimeException("Failed to fetch Recipes");
        }

    }

    public ResponseEntity<RecipeOperationsDto> getRecipe(Long id) {
            Optional<RecipeEntity> existingRecipe = recipeRepository.findById(id);
        return existingRecipe.map(recipeEntity -> {
            RecipeOperationsDto recipeOperationsDto = recipeOperationsMapper.mapTo(recipeEntity);

            return new ResponseEntity<>(recipeOperationsDto,HttpStatus.FOUND);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

        @Override
    public ResponseEntity<RecipeOperationsDto> updateRecipe(Long user_id, RecipeOperationsDto recipeOperationsDto) {
        if(recipeRepository.existsById(user_id)){
            return recipeRepository.findById(user_id).map(
                    existingRecipe -> {
                        Optional.ofNullable(recipeOperationsDto.getTitle()).ifPresent(existingRecipe::setTitle);
                        Optional.ofNullable(recipeOperationsDto.getDescriptions()).ifPresent(existingRecipe::setDescriptions);
                        Optional.ofNullable(recipeOperationsDto.getCookingTime()).ifPresent(existingRecipe::setCookingTime);
                        Optional.ofNullable(recipeOperationsDto.getDifficult()).ifPresent(existingRecipe::setDifficult);
                        Optional.ofNullable(recipeOperationsDto.getDirection()).ifPresent(existingRecipe::setDirection);
                        Optional.ofNullable(recipeOperationsDto.getServes()).ifPresent(existingRecipe::setServes);
                        Optional.ofNullable(recipeOperationsDto.getMetaDescription()).ifPresent(existingRecipe::setMetaDescription);
                        Optional.ofNullable(recipeOperationsDto.getPermLink()).ifPresent(existingRecipe::setPermLink);
                        Optional.ofNullable(recipeOperationsDto.getVideoLink()).ifPresent(existingRecipe::setVideoLink);
                        Optional.ofNullable(recipeOperationsDto.getPrepareTime()).ifPresent(existingRecipe::setPrepareTime);

                        RecipeOperationsDto updatedRecipe = recipeOperationsMapper.mapTo(recipeRepository.save(existingRecipe));

                        return new ResponseEntity<>(updatedRecipe,HttpStatus.OK);
                    }
            ).orElseThrow(() -> new RuntimeException("Recipe Did Not Update"));
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<HttpStatus> deleteRecipe(Long id) {

        try {
            recipeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(Exception err){
            throw new RuntimeException("Delete failed");
        }

    }

}
