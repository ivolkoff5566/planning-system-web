package com.planning_system.entity.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.planning_system.entity.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@Table(name = "tasks")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Task {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;
     private Instant date;
     private String name;
     private String description;
     @Enumerated(EnumType.STRING)
     private TaskStatus status;
     @Enumerated(EnumType.STRING)
     private TaskPriority priority;
     @ManyToOne
     @JsonIgnoreProperties("tasks")
     @JoinColumn(name = "user_id")
     private User user;
     @Column(name = "is_rejected")
     private boolean isRejected;
}