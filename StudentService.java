package com.exam.student.service;

import com.exam.student.dto.ScoreSkillUpdateRequest;
import com.exam.student.model.Student;
import com.exam.student.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public Student addStudent(Student student) {
        if (repository.existsById(student.getStudentId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student ID already exists");
        }
        return repository.save(student);
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public Student getStudent(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    public Student updateScoresAndSkills(String id, ScoreSkillUpdateRequest request) {
        Student student = getStudent(id);
        student.setProgrammingScore(request.getProgrammingScore());
        student.setMathematicsScore(request.getMathematicsScore());
        student.setTechnicalSkills(request.getTechnicalSkills());
        return repository.save(student);
    }

    public void deleteStudent(String id) {
        Student student = getStudent(id);
        repository.delete(student);
    }

    public List<Student> getMeritList() {
        // ArrayList + Comparator: highest average first.
        List<Student> students = new ArrayList<>(repository.findAll());
        students.sort(Comparator.comparingDouble(Student::getAverageScore).reversed());
        return students;
    }

    public List<Student> search(String id, String email, String course) {
        if (id != null && !id.isBlank()) return List.of(getStudent(id));
        if (email != null && !email.isBlank()) return repository.findByEmailIgnoreCase(email);
        if (course != null && !course.isBlank()) return repository.findByCourseNameContainingIgnoreCase(course);
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide id, email or course");
    }

    public Map<String, List<String>> findStudentsWithSameSkills() {
        // Group students by a normalized, sorted skills key using Java collections.
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (Student student : repository.findAll()) {
            List<String> normalized = student.getTechnicalSkills().stream()
                    .map(String::trim).map(String::toLowerCase).sorted().toList();
            if (normalized.isEmpty()) continue;
            String key = String.join(", ", normalized);
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(student.getStudentId());
        }
        return groups.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    public List<Student> programmingNeedsImprovementMathStrong() {
        // "Needs improvement" in programming interpreted as programming score below 50.
        return repository.findByProgrammingScoreLessThanAndMathematicsScoreGreaterThanEqual(50.0, 75.0);
    }
}
