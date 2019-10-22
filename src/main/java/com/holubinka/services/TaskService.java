package com.holubinka.services;

import com.holubinka.model.TaskManager;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<TaskManager> getAll(String email);

    Optional<TaskManager> getById(Long id, String email);

    Optional<TaskManager> create(TaskManager taskManager, String email);

    Optional<TaskManager> update(Long id, TaskManager taskManager, String email);

    void deleteById(Long id, String email);

    void sendEmail(Long id, String emailFrom, String emailTo);

}
