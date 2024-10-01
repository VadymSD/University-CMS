package vadym.spring.console.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vadym.spring.console.app.dto.UserDto;
import vadym.spring.console.app.entity.Role;
import vadym.spring.console.app.entity.UserEntity;
import vadym.spring.console.app.service.RoleService;
import vadym.spring.console.app.service.UserEntityService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserEntityService userService;

    private final RoleService roleService;

    private static final String ROLE_STRING = "roles";

    private static final String ADMIN_PANEL = "redirect:/users";

    @Autowired
    public UserController(UserEntityService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<UserEntity> users = userService.findAll();
        List<Role> roles = roleService.findAll();

        model.addAttribute("users", users);
        model.addAttribute(ROLE_STRING, roles);
        return "users";
    }

    @GetMapping("/new-user")
    public String addUserForm(Model model) {

        List<Role> roles = roleService.findAll();
        model.addAttribute(ROLE_STRING, roles);
        model.addAttribute("user", new UserDto());

        return "create-user";
    }

    @PostMapping("/new-user/add")
    public String saveUser(@ModelAttribute UserDto user) {
        userService.save(user);
        return ADMIN_PANEL;
    }

    @GetMapping("/edit-user/{id}")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        UserEntity user = userService.findById(id).orElseThrow(RuntimeException::new);

        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roleId(user.getRole().getId())
                .build();

        List<Role> roles = roleService.findAll();

        model.addAttribute("user", userDto);
        model.addAttribute(ROLE_STRING, roles);

        return "update-user";
    }

    @PostMapping("/edit-user/update/{id}")
    public String updateUser(@ModelAttribute UserDto user, @PathVariable Long id) {
        user.setId(id);
        userService.update(user);
        return ADMIN_PANEL;
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ADMIN_PANEL;
    }
}


