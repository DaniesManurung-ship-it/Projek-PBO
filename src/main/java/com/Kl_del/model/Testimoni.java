package com.Kl_del.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "testimonials")
public class Testimoni {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false, length = 1000)
    private String message;
    
    @Column(nullable = false)
    private Integer rating;
    
    private boolean approved = false;
    private boolean archived = false;
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    // Getters
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getName() { return name; }
    public String getMessage() { return message; }
    public Integer getRating() { return rating; }
    public boolean isApproved() { return approved; }
    public boolean isArchived() { return archived; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setName(String name) { this.name = name; }
    public void setMessage(String message) { this.message = message; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setApproved(boolean approved) { this.approved = approved; }
    public void setArchived(boolean archived) { this.archived = archived; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}