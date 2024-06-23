package com.SharpDevs.Recipe.Mania.Service.Impl;

import com.SharpDevs.Recipe.Mania.Repository.CategoryRepository;
import com.SharpDevs.Recipe.Mania.Repository.UserRepository;
import com.SharpDevs.Recipe.Mania.Service.CategoryService;
import com.SharpDevs.Recipe.Mania.domain.DTO.CategoryDto;
import com.SharpDevs.Recipe.Mania.domain.DTO.CategoryOperationsDto;
import com.SharpDevs.Recipe.Mania.domain.Entity.CategoryEntity;
import com.SharpDevs.Recipe.Mania.domain.Entity.UserEntity;
import com.SharpDevs.Recipe.Mania.domain.Mappers.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final Mapper<CategoryEntity, CategoryOperationsDto> categoryOperationsMapper;

    private final Mapper<CategoryEntity, CategoryDto> categoryMapper;

    @Override
    public ResponseEntity<HttpStatus> createCategory(CategoryOperationsDto categoryOperationsDto) throws RuntimeException {
        try {
            UserEntity userEntity  = userRepository.findById(categoryOperationsDto.getUserId()).orElse(null);
            if (userEntity != null) {
                CategoryEntity newCategory = categoryOperationsMapper.mapFrom(categoryOperationsDto);
                newCategory.setUser(userEntity);
                categoryOperationsDto = categoryOperationsMapper.mapTo(categoryRepository.save(newCategory));
                return new ResponseEntity(categoryOperationsDto, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception err) {
            throw new RuntimeException(err + "Error while creating Category");
        }
    }

    @Override
    public ResponseEntity<Iterable<CategoryOperationsDto>> getAllCategory() {
        Iterable<CategoryEntity> categoryEntities = categoryRepository.findAll();
        Iterable<CategoryOperationsDto> categoryDtos = categoryOperationsMapper.mapListTo(categoryEntities);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CategoryDto> getCategory(Long id) {
        CategoryEntity foundCategory = categoryRepository.findById(id).orElse(null);
        CategoryDto categoryDto = categoryMapper.mapTo(foundCategory);
        if (foundCategory != null) {
            return new ResponseEntity<>(categoryDto, HttpStatus.OK);

        }
        return null;
    }
}