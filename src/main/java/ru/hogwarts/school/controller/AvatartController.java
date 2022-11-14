package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.util.List;
@RestController
@RequestMapping("avatars")
public class AvatartController {

    private final AvatarService avatarService;

    public AvatartController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping("/findAll")
    public List<Avatar> findAll(@RequestParam int number, @RequestParam int size) {
        return avatarService.findAll(number, size);
    }
}
