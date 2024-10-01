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
import vadym.spring.console.app.dto.GroupDto;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.helpers.GroupHelper;
import ua.com.foxminded.vadym.spring.console.app.service.*;

import java.util.*;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.GroupService;
import vadym.spring.console.app.service.StudentService;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@WebMvcTest(GroupController.class)
@WithMockUser
class GroupControllerIntegrationTest {

    private final String GROUP_PAGE = "/groups";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GroupService groupService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private GroupHelper groupHelper;

    @Test
    void givenGroups_whenGetGroups_thenReturnJsonArray()
            throws Exception {

        Group group = new Group();

        List<Group> allGroups = Collections.singletonList(group);

        given(groupService.findAllGroups(null)).willReturn(allGroups);

        mvc.perform(get("/groups"))
                .andExpect(status().isOk())
                .andExpect(view().name("groups"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", hasSize(1)));
    }

    @Test
    void testAddGroup_whenAddGet_thenReturnCreatePage() throws Exception {
        given(courseService.findAll()).willReturn(Collections.singletonList(new Course()));
        given(studentService.findAll()).willReturn(Collections.singletonList(new Student()));


        mvc.perform(get("/groups/new-group"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-group"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("courses", hasSize(1)))
                .andExpect(model().attribute("group", notNullValue()));
    }

    @Test
    void testEditGroup_whenEditGroupGet_thenReturnEditPage() throws Exception {
        GroupDto groupDto = createGroupDto(Collections.singleton(1L), Collections.singleton(1L));

        given(groupService.findById(groupDto.getId())).willReturn(Optional.of(new Group()));
        given(groupHelper.buildGroupDto(new Group())).willReturn(groupDto);
        given(courseService.findAll()).willReturn(Collections.singletonList(new Course()));
        given(studentService.findAll()).willReturn(Collections.singletonList(new Student()));

        mvc.perform(get("/groups/edit-group/{id}", groupDto.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("update-group"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("students", hasSize(1)))
                .andExpect(model().attribute("courses", hasSize(1)))
                .andExpect(model().attribute("group", notNullValue()));
    }

    @Test
    void testCreateGroup_whenCreateGroupPost_thenReturnGroup() throws Exception {
        Group course = new Group();
        Student student = new Student();

        GroupDto groupDto = createGroupDto(Collections.singleton(1L), Collections.singleton(1L));

        mvc.perform(post("/groups/new-group/add")
                        .with(csrf())
                        .param("name", groupDto.getName())
                        .param("courses", String.valueOf(Collections.singletonList(course)))
                        .param("students", String.valueOf(Collections.singletonList(student))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(GROUP_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testAddGroup_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        Group course = new Group();
        Student student = new Student();

        GroupDto groupDto = createGroupDto(Collections.singleton(1L), Collections.singleton(1L));

        mvc.perform(post("/groups/new-groups/add")
                        .with(csrf())
                        .param("name", groupDto.getName())
                        .param("courses", String.valueOf(Collections.singletonList(course)))
                        .param("students", String.valueOf(Collections.singletonList(student))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testEditGroup_whenEditGroupPost_thenReturnGroupPage() throws Exception {
        Group course = new Group();
        Student student = new Student();

        GroupDto groupDto = createGroupDto(Collections.singleton(1L), Collections.singleton(1L));

        mvc.perform(post("/groups/edit-group/update/{id}", groupDto.getId())
                        .with(csrf())
                        .param("name", groupDto.getName())
                        .param("courses", String.valueOf(Collections.singletonList(course)))
                        .param("students", String.valueOf(Collections.singletonList(student))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(GROUP_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testEditGroup_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        Group course = new Group();
        Student student = new Student();

        GroupDto groupDto = createGroupDto(Collections.singleton(1L), Collections.singleton(1L));

        mvc.perform(post("/groups/edit-group/update/{id}", groupDto.getId())
                        .with(csrf())
                        .param("name", groupDto.getName())
                        .param("courses", String.valueOf(Collections.singletonList(course)))
                        .param("students", String.valueOf(Collections.singletonList(student))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteGroup_whenDeleteGroup_thenReturnGroups() throws Exception {
        Long userId =  1L;

        doNothing().when(courseService).delete(userId);

        mvc.perform(post("/groups/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(GROUP_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testDeleteGroup_whenGroupIsAnonymous_thenReturnUnauthorized() throws Exception {
        Long userId =  1L;

        doNothing().when(courseService).delete(userId);

        mvc.perform(post("/groups/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    private GroupDto createGroupDto(Set<Long> tIds, Set<Long> gIds) {
        return GroupDto.builder()
                .id(1L)
                .name("course")
                .studentsIds(tIds)
                .coursesIds(gIds)
                .build();
    }
}


