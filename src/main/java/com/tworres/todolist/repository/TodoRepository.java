package com.tworres.todolist.repository;

import com.tworres.todolist.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    // Find todos by completion status
    List<Todo> findByCompleted(boolean completed);
    
    // Find todos containing title (case insensitive)
    List<Todo> findByTitleContainingIgnoreCase(String title);
    
    // Find todos containing description (case insensitive)
    List<Todo> findByDescriptionContainingIgnoreCase(String description);
}