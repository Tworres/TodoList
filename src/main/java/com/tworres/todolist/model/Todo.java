package com.tworres.todolist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private boolean completed = false;
    
    private String createdAt = LocalDateTime.now().toString();
    
    private String updatedAt;
    
    private String completedAt;
    
    // Method to mark todo as completed
    public void toggleCompleted() {
        this.completed = !this.completed;
        this.completedAt = this.completed ? LocalDateTime.now().toString() : null;
        this.updateTimestamp();
    }
    
    // Method to update the updatedAt timestamp
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now().toString();
    }
}