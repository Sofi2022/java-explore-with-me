package ru.practicum.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.categories.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> {


    @Query("SELECT c from Category c " +
            "WHERE c.name LIKE :name")
    Optional<List<Category>> findByName(String name);
}
