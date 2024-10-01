package vadym.spring.console.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vadym.spring.console.app.dto.StudentDto;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.service.StudentService;
import vadym.spring.console.app.service.GroupService;
import vadym.spring.console.app.service.UserEntityService;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final String STUDENTS_PAGE = "redirect:/students";

    private final StudentService studentService;

    private final GroupService groupService;

    private final UserEntityService userEntityService;


    @Autowired
    public StudentController(StudentService studentService, GroupService groupService, UserEntityService userEntityService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.userEntityService = userEntityService;
    }

    @GetMapping
    public String getAllStudents(Model model, String keyword) {
        List<Student> students = studentService.findAllStudents(keyword);

        model.addAttribute("students", students);

        return "students";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/new-student")
    public String addStudentForm(Model model) {
        model.addAttribute("student", new StudentDto());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("users", userEntityService.findAllUnassignedUsers());

        return "create-student";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/new-student/add")
    public String saveStudent(@ModelAttribute StudentDto student) {
        studentService.save(student);

        return STUDENTS_PAGE;
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/edit-student/{id}")
    public String editStudentForm(@PathVariable("id") Long id, Model model) {
        Student student = studentService.findById(id).orElseThrow(RuntimeException::new);

        StudentDto studentDto =
                StudentDto.builder()
                .id(student.getId())
                .groupId(student.getGroup().getId())
                .firstName(student.getFirstName())
                .surname(student.getSurname())
                .build();

        model.addAttribute("student", studentDto);
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("users", userEntityService.findAllUnassignedUsers());

        return "update-student";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/edit-student/update/{id}")
    public String updateStudent(@ModelAttribute StudentDto student, @PathVariable("id") Long id) {
        student.setId(id);
        studentService.update(student);

        return STUDENTS_PAGE;
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.delete(id);

        return STUDENTS_PAGE;
    }
}


