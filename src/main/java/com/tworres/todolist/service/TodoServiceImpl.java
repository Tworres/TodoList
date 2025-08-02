package com.tworres.todolist.service;

import com.tworres.todolist.model.Todo;
import com.tworres.todolist.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Todo createTodo(Todo todo) {
        todo.setCreatedAt(LocalDateTime.now().toString());
        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    @Override
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    @Override
    public Todo updateTodo(Long id, Todo todoDetails) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
        
        todo.setTitle(todoDetails.getTitle());
        todo.setDescription(todoDetails.getDescription());
        todo.setCompleted(todoDetails.isCompleted());
        todo.setUpdatedAt(LocalDateTime.now().toString());
        
        if (todoDetails.isCompleted() && todo.getCompletedAt() == null) {
            todo.setCompletedAt(LocalDateTime.now().toString());
        } else if (!todoDetails.isCompleted()) {
            todo.setCompletedAt(null);
        }
        
        return todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
        todoRepository.delete(todo);
    }

    @Override
    public Todo toggleCompleted(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Todo not found with id: " + id));
        
        todo.toggleCompleted();
        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> getTodosByCompletionStatus(boolean completed) {
        return todoRepository.findByCompleted(completed);
    }

    @Override
    public List<Todo> searchTodosByTitle(String title) {
        return todoRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Todo> searchTodosByDescription(String description) {
        return todoRepository.findByDescriptionContainingIgnoreCase(description);
    }
}