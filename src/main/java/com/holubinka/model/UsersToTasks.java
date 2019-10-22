package com.holubinka.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "USERS_TO_TASKS")
public class UsersToTasks implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties("usersToTasks")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_USER_ID")
    private User user;

    @JsonIgnoreProperties("usersToTasks")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FK_TASK_ID")
    private TaskManager task;

    @Column(name = "SENDER", columnDefinition = "varchar(255) default 'It is my task'")
    private String sender;

    public UsersToTasks() {
    }

    public UsersToTasks(TaskManager task, User user, String sender) {
        this.task = task;
        this.user = user;
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TaskManager getTask() {
        return task;
    }

    public void setTask(TaskManager task) {
        this.task = task;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public static UsersToTasks of(User user, TaskManager task) {
        UsersToTasks usersToTasks = new UsersToTasks();
        usersToTasks.setUser(user);
        usersToTasks.setTask(task);
        return usersToTasks;
    }

}
