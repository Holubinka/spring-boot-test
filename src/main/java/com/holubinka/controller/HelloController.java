package com.holubinka.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<HelloObject> helloWorld() {
        return new ResponseEntity<>(HelloObject.of("Hello from Spring Boot!"), HttpStatus.OK);
    }
}

class HelloObject {
    private String message;

    public HelloObject(String message) {
        this.message = message;
    }

    public static HelloObject of(String msg) {
        return new HelloObject(msg);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
