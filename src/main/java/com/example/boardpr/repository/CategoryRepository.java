package com.example.boardpr.repository;

import com.example.boardpr.domain.Category;
import com.example.boardpr.util.type.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT C FROM Category C WHERE C.categoryName = ?1")
    List<Category> findByCategoryName(CategoryName categoryName);
}
