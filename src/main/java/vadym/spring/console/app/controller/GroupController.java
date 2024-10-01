package vadym.spring.console.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vadym.spring.console.app.dto.GroupDto;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.helpers.GroupHelper;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.GroupService;
import vadym.spring.console.app.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private static final String GROUP_PAGE = "redirect:/groups";

    private final GroupService groupService;

    private final CourseService courseService;

    private final StudentService studentService;

    private final GroupHelper groupHelper;

    @Autowired
    public GroupController(GroupService groupService, CourseService courseService,
                           StudentService studentService, GroupHelper groupHelper) {
        this.groupService = groupService;
        this.courseService = courseService;
        this.studentService = studentService;
        this.groupHelper = groupHelper;
    }

    @GetMapping
    public String getAllGroups(Model model, String keyword) {
        List<Group> groups = groupService.findAllGroups(keyword);

        model.addAttribute("groups", groups);

        return "groups";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/new-group")
    public String addGroupForm(Model model) {
        model.addAttribute("group", new GroupDto());
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("students", studentService.findAll());

        return "create-group";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/new-group/add")
    public String saveGroup(@ModelAttribute GroupDto group) {
        groupService.save(group);
        return GROUP_PAGE;
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @GetMapping("/edit-group/{id}")
    public String editGroupForm(@PathVariable("id") Long id, Model model) {
        Group group = groupService.findById(id).orElseThrow(RuntimeException::new);

        GroupDto groupDto = groupHelper.buildGroupDto(group);

        model.addAttribute("group", groupDto);
        model.addAttribute("courses", courseService.findAll());
        model.addAttribute("students", studentService.findAll());

        return "update-group";
    }

    @Secured({"ROLE_ADMIN", "ROLE_STAFF"})
    @PostMapping("/edit-group/update/{id}")
    public String updateGroup(@ModelAttribute GroupDto group, @PathVariable("id") Long id) {
        group.setId(id);
        groupService.update(group);
        return GROUP_PAGE;
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/delete/{id}")
    public String deleteGroup(@PathVariable Long id) {
        groupService.delete(id);

        return GROUP_PAGE;
    }
}


