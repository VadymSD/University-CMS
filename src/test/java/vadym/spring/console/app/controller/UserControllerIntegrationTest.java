package vadym.spring.console.app.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import vadym.spring.console.app.dto.UserDto;
import vadym.spring.console.app.entity.Role;
import vadym.spring.console.app.entity.UserEntity;
import vadym.spring.console.app.service.RoleService;
import vadym.spring.console.app.service.UserEntityService;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@WithMockUser(roles = "ADMIN")
class UserControllerIntegrationTest {

    private static final String ADMIN_PANEL = "/users";

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";

    private static final String ROLE_ID = "roleId";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserEntityService userEntityService;

    @MockBean
    private RoleService roleService;

    @Test
    void testGetAllUsers_whenGetAllUsers_thenReturnAdminPage() throws Exception {
        Role role = createRole();
        UserEntity user = createUser(role);

        given(userEntityService.findAll()).willReturn(Collections.singletonList(user));
        given(roleService.findAll()).willReturn(Collections.singletonList(role));

        mvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attribute("users", hasSize(1)))
                .andExpect(model().attribute("roles", hasSize(1)));
    }

    @Test
    void testAddUser_whenAddGet_thenReturnCreatePage() throws Exception {
        given(roleService.findAll()).willReturn(Collections.singletonList(new Role()));

        mvc.perform(get("/users/new-user"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attribute("roles", hasSize(1)))
                .andExpect(model().attribute("user", notNullValue()));
    }

    @Test
    void testEditUser_whenEditUserGet_thenReturnEditPage() throws Exception {
        Role role = createRole();
        UserEntity user = createUser(role);

        given(userEntityService.findById(user.getId())).willReturn(Optional.of(user));
        given(roleService.findAll()).willReturn(Collections.singletonList(role));

        mvc.perform(get("/users/edit-user/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("update-user"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attribute("roles", hasSize(1)))
                .andExpect(model().attribute("user", notNullValue()));
    }

    @Test
    void testEditUser_whenEditUserPost_thenReturnAdminPage() throws Exception {
        Role role = createRole();
        UserDto userDto = createUserDto(role);

        mvc.perform(post("/users/edit-user/update/{id}", userDto.getId())
                    .with(csrf())
                    .param(USERNAME, userDto.getUsername())
                    .param(PASSWORD, userDto.getPassword())
                    .param(ROLE_ID, String.valueOf(userDto.getRoleId())))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(ADMIN_PANEL));
    }

    @Test
    @WithAnonymousUser
    void testEditUser_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        Role role = createRole();
        UserDto userDto = createUserDto(role);

        mvc.perform(post("/users/edit-user/update/{id}", userDto.getId())
                        .with(csrf())
                        .param(USERNAME, userDto.getUsername())
                        .param(PASSWORD, userDto.getPassword())
                        .param(ROLE_ID, String.valueOf(userDto.getRoleId())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateUser_whenCreateUserPost_thenReturnAdminPage() throws Exception {
        Role role = createRole();
        UserDto userDto = createUserDto(role);

        mvc.perform(post("/users/new-user/add", userDto.getId())
                        .with(csrf())
                        .param(USERNAME, userDto.getUsername())
                        .param(PASSWORD, userDto.getPassword())
                        .param(ROLE_ID, String.valueOf(userDto.getRoleId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ADMIN_PANEL));
    }

    @Test
    @WithAnonymousUser
    void testAddUser_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        Role role = createRole();
        UserDto userDto = createUserDto(role);

        mvc.perform(post("/users/new-user/add", userDto.getId())
                        .with(csrf())
                        .param(USERNAME, userDto.getUsername())
                        .param(PASSWORD, userDto.getPassword())
                        .param(ROLE_ID, String.valueOf(userDto.getRoleId())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteUser_whenDeleteUser_thenReturnAdminPage() throws Exception {
        Long userId =  1L;

        doNothing().when(userEntityService).delete(userId);

        mvc.perform(post("/users/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ADMIN_PANEL));

        verify(userEntityService, times(1)).delete(userId);
    }

    @Test
    @WithAnonymousUser
    void testDeleteUser_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        Long userId =  1L;

        doNothing().when(userEntityService).delete(userId);

        mvc.perform(post("/users/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }


    private Role createRole() {
        return Role.builder()
                .roleName("role")
                .id(1L)
                .build();
    }

    private UserEntity createUser(Role role) {
        return UserEntity.builder()
                .role(role)
                .username(USERNAME)
                .password(PASSWORD)
                .id(1L)
                .build();
    }

    private UserDto createUserDto(Role role) {
        return UserDto.builder()
                .roleId(role.getId())
                .username(USERNAME)
                .password(PASSWORD)
                .id(1L)
                .build();
    }
}
