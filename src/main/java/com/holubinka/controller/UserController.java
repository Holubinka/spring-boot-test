package com.holubinka.controller;

import com.holubinka.controller.exception.NotMatchedPasswordsException;
import com.holubinka.controller.model.UserExt;
import com.holubinka.model.User;
import com.holubinka.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll().orElseGet(Collections::emptyList));
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<User> add(@Valid @RequestBody UserExt userExt) {
        if (!userExt.getPassword().equals(userExt.getConfirmPassword())) {
            throw new NotMatchedPasswordsException();
        }
        User user = User.of(userExt);
        return userService.create(user)
                .map(u -> ResponseEntity.created(getUri("user", u.getId())).body(u))
                .orElseGet(ResponseEntity.status(HttpStatus.CONFLICT)::build);
    }

    private URI getUri(String uri, Long id) {
        return URI.create(String.format("/%s/%s", uri, id));
    }
}
