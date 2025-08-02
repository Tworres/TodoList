package com.tworres.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tworres.todolist.model.Todo;
import com.tworres.todolist.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldCreateTodo() throws Exception {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Test Todo")))
                .andExpect(jsonPath("$.description", is("Test Description")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void shouldGetAllTodos() throws Exception {
        // Create test todos
        Todo todo1 = new Todo();
        todo1.setTitle("Todo 1");
        todo1.setDescription("Description 1");
        todoRepository.save(todo1);

        Todo todo2 = new Todo();
        todo2.setTitle("Todo 2");
        todo2.setDescription("Description 2");
        todoRepository.save(todo2);

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Todo 1")))
                .andExpect(jsonPath("$[1].title", is("Todo 2")));
    }

    @Test
    void shouldGetTodoById() throws Exception {
        // Create test todo
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        Todo savedTodo = todoRepository.save(todo);

        mockMvc.perform(get("/api/todos/" + savedTodo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedTodo.getId())))
                .andExpect(jsonPath("$.title", is("Test Todo")))
                .andExpect(jsonPath("$.description", is("Test Description")));
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        // Create test todo
        Todo todo = new Todo();
        todo.setTitle("Original Title");
        todo.setDescription("Original Description");
        Todo savedTodo = todoRepository.save(todo);

        // Update data
        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("Updated Title");
        updatedTodo.setDescription("Updated Description");
        updatedTodo.setCompleted(true);

        mockMvc.perform(put("/api/todos/" + savedTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedTodo.getId())))
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.description", is("Updated Description")))
                .andExpect(jsonPath("$.completed", is(true)));
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        // Create test todo
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        Todo savedTodo = todoRepository.save(todo);

        mockMvc.perform(delete("/api/todos/" + savedTodo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deleted", is(true)));

        // Verify it's deleted
        mockMvc.perform(get("/api/todos/" + savedTodo.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldMarkTodoAsCompleted() throws Exception {
        // Create test todo
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        todo.setCompleted(false);
        Todo savedTodo = todoRepository.save(todo);

        mockMvc.perform(patch("/api/todos/" + savedTodo.getId() + "/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(savedTodo.getId())))
                .andExpect(jsonPath("$.completed", is(true)))
                .andExpect(jsonPath("$.completedAt", notNullValue()));
    }
}