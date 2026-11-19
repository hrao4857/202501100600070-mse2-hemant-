package com.exam.student.controller;

import com.exam.student.dto.ScoreSkillUpdateRequest;
import com.exam.student.model.Student;
import com.exam.student.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    // Q3.1 Add a student
    @PostMapping
    public Student addStudent(@Valid @RequestBody Student student) {
        return service.addStudent(student);
    }

    // Q3.2 Retrieve all students
    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    // Q3.3 Search by ID (also supports email/course for Q2)
    @GetMapping("/search")
    public List<Student> search(@RequestParam(required = false) String id,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String course) {
        return service.search(id, email, course);
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable String id) {
        return service.getStudent(id);
    }

    // Q3.4 Update student scores and skills
    @PutMapping("/{id}/scores-skills")
    public Student updateScoresAndSkills(@PathVariable String id,
                                         @Valid @RequestBody ScoreSkillUpdateRequest request) {
        return service.updateScoresAndSkills(id, request);
    }

    // Q3.5 Delete a student
    @DeleteMapping("/{id}")
    public Map<String, String> deleteStudent(@PathVariable String id) {
        service.deleteStudent(id);
        return Map.of("message", "Student deleted successfully");
    }

    // Q3.6 Merit list
    @GetMapping("/merit-list")
    public List<Student> meritList() {
        return service.getMeritList();
    }

    // Additional Q2 business-rule endpoints
    @GetMapping("/same-skills")
    public Map<String, List<String>> sameSkills() {
        return service.findStudentsWithSameSkills();
    }

    @GetMapping("/programming-needs-improvement")
    public List<Student> programmingNeedsImprovement() {
        return service.programmingNeedsImprovementMathStrong();
    }
}
