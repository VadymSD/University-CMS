package vadym.spring.console.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vadym.spring.console.app.dto.LectureDto;
import vadym.spring.console.app.entity.Lecture;
import vadym.spring.console.app.helpers.LectureHelper;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.LectureService;
import vadym.spring.console.app.service.GroupService;
import vadym.spring.console.app.service.TeacherService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/lectures")
public class LectureController {

    private static final String LECTURES_PAGE = "redirect:/lectures";

    private final LectureService lectureService;

    private final GroupService groupService;

    private final TeacherService teacherService;

    private final CourseService courseService;

    private final LectureHelper lectureHelper;

    @Autowired
    public LectureController(LectureService lectureService, GroupService groupService,
                             TeacherService teacherService, CourseService courseService, LectureHelper lectureHelper) {
        this.lectureService = lectureService;
        this.groupService = groupService;
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.lectureHelper = lectureHelper;
    }

    @GetMapping
    public String getAllLectures(Model model, String keyword) {
        List<Lecture> lectures = lectureService.findAllLectures(keyword);

        model.addAttribute("lectures", lectures);

        return "lectures";
    }

    @GetMapping("/my-lectures")
    public String getSelectionPage() {
        return "timetable";
    }

    @PostMapping("/my-lectures/retrieve")
    public String getMyLectures(@RequestParam("startDate") LocalDate startDate, Model model,
                                @RequestParam("endDate") LocalDate endDate, Principal principal) {
        String username = principal.getName();
        List<Lecture> lectures = lectureService.getLecturesByUsernameWithinDateRange(username, startDate, endDate);
        model.addAttribute("lectures", lectures);
        return LECTURES_PAGE;
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/new-lecture")
    public String addLectureForm(Model model) {
        model.addAttribute("lecture", new LectureDto());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("courses", courseService.findAll());

        return "create-lecture";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/new-lecture/add")
    public String saveLecture(@ModelAttribute LectureDto lecture) {
        lectureService.save(lecture);

        return LECTURES_PAGE;
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/edit-lecture/{id}")
    public String editLectureForm(@PathVariable("id") Long id, Model model) {
        Lecture lecture = lectureService.findById(id).orElseThrow(RuntimeException::new);

        model.addAttribute("lecture", lectureHelper.buildLectureDto(lecture));
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("courses", courseService.findAll());

        return "update-lecture";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/edit-lecture/update/{id}")
    public String updateLecture(@ModelAttribute LectureDto lecture, @PathVariable("id") Long id) {
        lecture.setId(id);
        lectureService.update(lecture);

        return LECTURES_PAGE;
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/delete/{id}")
    public String deleteLecture(@PathVariable Long id) {
        lectureService.delete(id);

        return LECTURES_PAGE;
    }
}


