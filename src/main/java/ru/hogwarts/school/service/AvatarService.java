package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
        logger.info("AvatarService is loaded");
    }
    public List<Avatar> findAll(int number, int size) {
        logger.info("Get all avatars paged by page {} and size {}", number, size);
        return avatarRepository.findAll(PageRequest.of(number - 1, size)).getContent();
    }
}
