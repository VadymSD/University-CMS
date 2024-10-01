package vadym.spring.console.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vadym.spring.console.app.dto.TeacherDto;
import vadym.spring.console.app.entity.Teacher;
import vadym.spring.console.app.helpers.TeacherHelper;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.FacultyService;
import vadym.spring.console.app.service.TeacherService;
import vadym.spring.console.app.service.UserEntityService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private static final String TEACHER_PAGE = "redirect:/teachers";

    private final TeacherService teacherService;

    private final FacultyService facultyService;

    private final CourseService courseService;

    private final TeacherHelper teacherHelper;

    private final UserEntityService userEntityService;

    @Autowired
    public TeacherController(TeacherService teacherService, FacultyService facultyService, CourseService courseService, TeacherHelper teacherHelper, UserEntityService userEntityService) {
        this.teacherService = teacherService;
        this.facultyService = facultyService;
        this.courseService = courseService;
        this.teacherHelper = teacherHelper;
        this.userEntityService = userEntityService;
    }

    @GetMapping
    public String getAllTeachers(Model model, String keyword) {
        model.addAttribute("teachers", teacherService.findAllTeachers(keyword));
        model.addAttribute("faculties", facultyService.findAll());

        return "teachers";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/new-teacher")
    public String addTeacherForm(Model model) {
        model.addAttribute("teacher", new TeacherDto());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("faculties", facultyService.findAll());
        model.addAttribute("users", userEntityService.findAllUnassignedUsers());

        return "create-teacher";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/new-teacher/add")
    public String saveTeacher(@ModelAttribute TeacherDto teacher) {
        teacherService.save(teacher);
        return TEACHER_PAGE;
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/edit-teacher/{id}")
    public String editTeacherForm(@PathVariable("id") Long id, Model model) {
        Teacher teacher = teacherService.findById(id).orElseThrow(RuntimeException::new);

        TeacherDto teacherDto = teacherHelper.buildTeacherDto(teacher);

        model.addAttribute("teacher", teacherDto);
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("faculties", facultyService.findAll());
        model.addAttribute("users", userEntityService.findAllUnassignedUsers());

        return "update-teacher";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/edit-teacher/update/{id}")
    public String updateTeacher(@ModelAttribute TeacherDto teacher, @PathVariable("id") Long id) {
        teacher.setId(id);
        teacherService.update(teacher);
        return TEACHER_PAGE;
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/delete/{id}")
    public String deleteTeacher(@PathVariable Long id) {
        teacherService.delete(id);

        return TEACHER_PAGE;
    }
}


