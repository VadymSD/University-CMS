package vadym.spring.console.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vadym.spring.console.app.dto.CourseDto;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.helpers.CourseHelper;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.GroupService;
import vadym.spring.console.app.service.TeacherService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final String COURSES_PAGE = "redirect:/courses";

    private final CourseService courseService;

    private final GroupService groupService;

    private final TeacherService teacherService;

    private final CourseHelper courseHelper;

    @Autowired
    public CourseController(CourseService courseService, GroupService groupService,
                            TeacherService teacherService, CourseHelper courseHelper) {
        this.courseService = courseService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.courseHelper = courseHelper;
    }

    @GetMapping
    public String getAllCourses(Model model, String keyword) {
        List<Course> courses = courseService.findAllCourses(keyword);

        model.addAttribute("courses", courses);

        return "courses";
    }

    @GetMapping("/my-courses")
    public String getMyCourses(Model model, Principal principal) {
        String username = principal.getName();
        List<Course> courses = courseService.getCoursesByUsername(username);
        model.addAttribute("courses", courses);
        return "courses";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/new-course")
    public String addCourseForm(Model model) {
        model.addAttribute("course", new CourseDto());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("teachers", teacherService.findAll());

        return "create-course";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/new-course/add")
    public String saveCourse(@ModelAttribute CourseDto course) {
        courseService.save(course);

        return COURSES_PAGE;
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/edit-course/{id}")
    public String editCourseForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.findById(id).orElseThrow(RuntimeException::new);

        CourseDto courseDto = courseHelper.buildCourseDto(course);

        model.addAttribute("course", courseDto);
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("teachers", teacherService.findAll());

        return "update-course";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/edit-course/update/{id}")
    public String updateCourse(@ModelAttribute CourseDto course, @PathVariable("id") Long id) {
        course.setId(id);
        courseService.update(course);

        return COURSES_PAGE;
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.delete(id);

        return COURSES_PAGE;
    }
}



