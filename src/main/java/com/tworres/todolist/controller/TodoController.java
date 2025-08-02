package com.tworres.todolist.controller;

import com.tworres.todolist.model.Todo;
import com.tworres.todolist.service.TodoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // Create a new todo
    @PostMapping
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        Todo createdTodo = todoService.createTodo(todo);
        return new ResponseEntity<>(createdTodo, HttpStatus.CREATED);
    }

    // Get all todos
    @GetMapping
    public ResponseEntity<List<Todo>> getAllTodos(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description) {
        
        List<Todo> todos;
        
        if (completed != null) {
            todos = todoService.getTodosByCompletionStatus(completed);
        } else if (title != null && !title.isEmpty()) {
            todos = todoService.searchTodosByTitle(title);
        } else if (description != null && !description.isEmpty()) {
            todos = todoService.searchTodosByDescription(description);
        } else {
            todos = todoService.getAllTodos();
        }
        
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    // Get todo by id
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return todoService.getTodoById(id)
                .map(todo -> new ResponseEntity<>(todo, HttpStatus.OK))
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
    }

    // Update todo
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @Valid @RequestBody Todo todoDetails) {
        Todo updatedTodo = todoService.updateTodo(id, todoDetails);
        return new ResponseEntity<>(updatedTodo, HttpStatus.OK);
    }

    // Delete todo
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Mark todo as completed
    @PatchMapping("/{id}/toggle-completed")
    public ResponseEntity<Todo> toggleCompleted(@PathVariable Long id) {
        Todo completedTodo = todoService.toggleCompleted(id);
        return new ResponseEntity<>(completedTodo, HttpStatus.OK);
    }

    // Exception handler for EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}