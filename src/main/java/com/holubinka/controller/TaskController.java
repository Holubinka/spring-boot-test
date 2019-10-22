package com.holubinka.controller;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.holubinka.model.TaskManager;
import com.holubinka.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController

public class TaskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public ResponseEntity<List<TaskManager>> getAll(Principal principal) {
        List<TaskManager> categories = taskService.getAll(principal.getName());
        ResponseEntity<List<TaskManager>> result;

        if (categories.isEmpty()) {
            result = ResponseEntity.notFound().build();
        } else {
            result = new ResponseEntity<>(categories, HttpStatus.OK);
        }
        return result;
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public ResponseEntity<TaskManager> getById(@PathVariable Long id, Principal principal) {
        return taskService.getById(id, principal.getName())
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    public ResponseEntity<TaskManager> create(@RequestBody TaskManager taskManager, Principal principal) {
        return taskService.create(taskManager, principal.getName())
                .map(c -> ResponseEntity.created(getUri("taskManager", c.getId())).body(c))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TaskManager> update(@RequestBody TaskManager taskManager, @PathVariable Long id, Principal principal) {
        taskManager.setId(id);
        return taskService.update(id, taskManager, principal.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.CONFLICT));

    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<TaskManager> delete(@PathVariable Long id, Principal principal) {
        taskService.deleteById(id, principal.getName());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/send_task/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TaskManager> sendTask(@RequestBody JsonNode email, @PathVariable Long id, Principal principal) {
        taskService.sendEmail(id, principal.getName(), email.get("email").asText());
        return ResponseEntity.ok().build();
    }

    private URI getUri(String uri, Long id) {
        return URI.create(String.format("/%s/%s", uri, id));
    }
}
