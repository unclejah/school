package ru.hogwarts.school.service;

import org.springframework.http.ResponseEntity;

public interface InfoControllerInterface {
    ResponseEntity<Integer> getPort();
}
