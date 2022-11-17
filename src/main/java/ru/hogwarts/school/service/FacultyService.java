package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
        LOGGER.info("FacultyService is loaded");
    }

    public Faculty addFaculty(Faculty faculty) {
        LOGGER.info("Create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        LOGGER.info("Get faculty with identified {}", id);
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        LOGGER.info("Save faculty info {} to the repository.", faculty);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        LOGGER.info("Delete faculty with identifier {}", id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> findByColor(String color) {
        LOGGER.info("Get faculty with color {}", color);
        return facultyRepository.findByColor(color);
    }

    public Collection<Faculty> findByNameOrColorIgnoreCase(String name, String color) {
        LOGGER.info("Get faculty with name {} and color {}", name, color);
        return facultyRepository.findByNameOrColorIgnoreCase(name, color);
    }
}
