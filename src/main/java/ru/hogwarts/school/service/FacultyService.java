package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

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

    public String getFacultyLongName() {
        return facultyRepository.findAll().stream()
                .map(a -> a.getName())
                .max((a,b) -> a.length() - b.length())
                .orElseThrow();
    }

    public int getSum(int variants) {
        final int size = 1_000_000;
        switch(variants) {
            case 0: // 41ms
                return Stream.iterate(1, a -> a + 1).limit(size).reduce(0, (a, b) -> a + b );
            case 1: // 29ms
                return Stream.iterate(1, a -> a + 1).parallel().limit(size).reduce(0, (a, b) -> a + b );
            case 2: // 28ms
                return Stream.iterate(1, a -> a + 1).parallel().limit(size).mapToInt(a -> a).sum();
            case 3: // 3ms
                int sum = 0;
                for (int i = 0; i < size; i++) {
                    sum += (i + 1);
                }
                return sum;
            case 4: // 5ms (parallel) - 8ms (successively)
                int[] arr = new int[size];
                for (int i = 0; i < size; i++) {
                    arr[i] = (i + 1);
                }
                return Arrays.stream(arr).sum();
        }
        throw new RuntimeException("Doesn't support");
    }
}
