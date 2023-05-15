package com.planning_system.entity.user_statistics;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Entity
@Table(name = "user_stats")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "completed_tasks")
    private int completedTaskCount;
    @Column(name = "rejected_tasks")
    private int rejectedTaskCount;
    @Column(name = "assigned_tasks")
    private int assignedTaskCount;
    @Column(name = "user_id")
    private int userId;
}