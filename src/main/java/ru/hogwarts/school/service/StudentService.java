package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class StudentService {
    @Value("${avatars.dir.path}")
    private String avatarsDir;

    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
        LOGGER.info("StudentService is loaded");
    }

    public Student addStudent(Student student) {
        LOGGER.info("Create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        LOGGER.info("Get student with identified {}", id);
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        LOGGER.info("Save student info {} to the repository.", student);
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        LOGGER.info("Delete student with identifier {}", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        LOGGER.info("Get student by age {}", age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        LOGGER.info("Get student by age between {} and {}", min, max);
        return studentRepository.findByAgeBetween(min, max);
    }
    public Avatar findAvatar(long studentId) {
        LOGGER.info("Find an avatar for student with identifier {}", studentId);
        return avatarRepository.findByStudentId(studentId).orElseThrow();
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        LOGGER.info("upload avatar for student with identifier {}", studentId);
        Student student = findStudent(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElseGet(Avatar::new);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        LOGGER.info("The avatar for student with identifier {} saved", studentId);
        avatarRepository.save(avatar);
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public int getStudentsAmount() {
        LOGGER.info("Get students amount");
        return studentRepository.getStudentsAmount();
    }

    public double getAverageAge() {
        LOGGER.info("Get students average age");
        return studentRepository.getAverageAge();
    }

    public Collection<Student> getFiveLastStudents() {
        LOGGER.info("Get five last students");
        return  studentRepository.getFiveLastStudents();
    }

    public Collection<Student> getAllStudents() {
        LOGGER.info("Get all students");
        return studentRepository.findAll();
    }

    public Collection<String> getStudentsWithNameStartsA() {
        return studentRepository.findAll().stream()
                .map(a -> a.getName().toUpperCase())
                .filter(a -> a.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAgeByStream() {
        return studentRepository.findAll().stream()
                .mapToDouble(a -> a.getAge())
                .average().orElseThrow();
    }

}
