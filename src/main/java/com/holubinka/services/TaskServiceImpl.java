package com.holubinka.services;

import com.holubinka.dao.CategoryDao;
import com.holubinka.dao.UserDao;
import com.holubinka.model.TaskManager;
import com.holubinka.model.User;
import com.holubinka.model.UsersToTasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public List<TaskManager> getAll(String email) {
        return categoryDao.findAll().stream()
                .filter(x -> x.getUsersToTasks().stream()
                        .map(x1 -> x1.getUser())
                        .map(x1 -> x1.getEmail())
                        .collect(Collectors.toList())
                        .contains(email))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskManager> getById(Long id, String email) {
        return categoryDao.findById(id)
                .filter(x -> x.getUsersToTasks().stream()
                        .map(x1 -> x1.getUser())
                        .map(x1 -> x1.getEmail())
                        .collect(Collectors.toList())
                        .contains(email));
    }

    @Override
    public Optional<TaskManager> create(TaskManager taskManager, String email) {
        User user = userDao.findByEmail(email).get();
        UsersToTasks usersToTasks = UsersToTasks.of(user, taskManager);
        taskManager.setUsersToTasks(Arrays.asList(usersToTasks));

        return Optional.of(categoryDao.save(taskManager));
    }

    @Override
    public Optional<TaskManager> update(Long id, TaskManager taskManager, String email) {
        TaskManager task = null;
        if (this.getById(id, email).isPresent()) {
            return Optional.of(categoryDao.save(taskManager));
        }
        return Optional.of(task);
    }

    @Override
    public void deleteById(Long id, String email) {
        if (this.getById(id, email).isPresent()) {
            categoryDao.deleteById(id);
        } else {
            System.out.println("Error");
        }
    }

    @Override
    public void sendEmail(Long id, String emailFrom, String emailTo) {
        SimpleMailMessage mail = new SimpleMailMessage();
        Optional<TaskManager> task = getById(id, emailFrom);
        if (task.isPresent()) {

            mail.setTo(emailTo);
            mail.setFrom(emailFrom);
            mail.setSubject("Sharing task " + task.get().getTaskName());
            mail.setText(task.get().getTaskDescription());

            setNewTask(emailFrom, emailTo, task.get());
            javaMailSender.send(mail);
        }
    }

    private void setNewTask(String emailFrom, String emailTo, TaskManager task) {
        if (userDao.findByEmail(emailTo).isPresent()) {
            User user = userDao.findByEmail(emailTo)
                    .get();

            UsersToTasks usersToTasks = UsersToTasks.of(user, task);
            usersToTasks.setSender(emailFrom);
            user.getUsersToTasks().add(usersToTasks);
            userDao.save(user);
        } else {
            ResponseEntity.badRequest().build();
        }
    }
}
