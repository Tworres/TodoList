package com.tworres.todolist.service;

import com.tworres.todolist.model.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    
    // Create a new todo
    Todo createTodo(Todo todo);
    
    // Get all todos
    List<Todo> getAllTodos();
    
    // Get todo by id
    Optional<Todo> getTodoById(Long id);
    
    // Update todo
    Todo updateTodo(Long id, Todo todoDetails);
    
    // Delete todo
    void deleteTodo(Long id);
    
    // Mark todo as completed
    Todo toggleCompleted(Long id);
    
    // Get todos by completion status
    List<Todo> getTodosByCompletionStatus(boolean completed);
    
    // Search todos by title
    List<Todo> searchTodosByTitle(String title);
    
    // Search todos by description
    List<Todo> searchTodosByDescription(String description);
}