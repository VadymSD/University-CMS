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
import vadym.spring.console.app.dto.CourseDto;
import ua.com.foxminded.vadym.spring.console.app.entity.*;
import vadym.spring.console.app.helpers.CourseHelper;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.GroupService;
import vadym.spring.console.app.service.TeacherService;

import java.util.*;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Teacher;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
@WithMockUser(roles = "ADMIN")
class CourseControllerIntegrationTest {

    private static final String COURSES_PAGE = "/courses";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private CourseHelper courseHelper;

    @Test
    void givenCourses_whenGetCourses_thenReturnCoursesPage()
            throws Exception {

        Course course = new Course();

        List<Course> allCourses = Collections.singletonList(course);

        given(courseService.findAllCourses(null)).willReturn(allCourses);

        mvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("courses", hasSize(1)));
    }

    @Test
    void testGetMyCourses_whenGetMyCourses_thenReturnCoursePage() throws Exception {
        given(courseService.getCoursesByUsername(anyString())).willReturn(Collections.singletonList(new Course()));

        mvc.perform(get("/courses/my-courses"))
                .andExpect(status().isOk())
                .andExpect(view().name("courses"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("courses", hasSize(1)));
    }

    @Test
    void testAddCourse_whenAddGet_thenReturnCreatePage() throws Exception {
        given(groupService.findAll()).willReturn(Collections.singletonList(new Group()));
        given(teacherService.findAll()).willReturn(Collections.singletonList(new Teacher()));

        mvc.perform(get("/courses/new-course"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-course"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("teachers", hasSize(1)))
                .andExpect(model().attribute("groups", hasSize(1)))
                .andExpect(model().attribute("course", notNullValue()));
    }

    @Test
    void testEditCourse_whenEditCourseGet_thenReturnEditPage() throws Exception {
        CourseDto courseDto = createCourseDto(Collections.singleton(1L), Collections.singleton(1L));

        given(courseService.findById(courseDto.getId())).willReturn(Optional.of(new Course()));
        given(courseHelper.buildCourseDto(new Course())).willReturn(courseDto);
        given(groupService.findAll()).willReturn(Collections.singletonList(new Group()));
        given(teacherService.findAll()).willReturn(Collections.singletonList(new Teacher()));

        mvc.perform(get("/courses/edit-course/{id}", courseDto.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("update-course"))
                .andExpect(model().attributeExists("course"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("teachers", hasSize(1)))
                .andExpect(model().attribute("groups", hasSize(1)))
                .andExpect(model().attribute("course", notNullValue()));
    }

    @Test
    void testCreateCourse_whenCreateCoursePost_thenReturnCourse() throws Exception {
        Group group = new Group();
        Teacher teacher = new Teacher();

        CourseDto courseDto = createCourseDto(Collections.singleton(1L), Collections.singleton(1L));

        mvc.perform(post("/courses/new-course/add")
                        .with(csrf())
                        .param("name", courseDto.getName())
                        .param("description", courseDto.getDescription())
                        .param("groups", String.valueOf(Collections.singletonList(group)))
                        .param("teachers", String.valueOf(Collections.singletonList(teacher))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(COURSES_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testAddCourse_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        Group group = new Group();
        Teacher teacher = new Teacher();

        CourseDto courseDto = createCourseDto(Collections.singleton(1L), Collections.singleton(1L));

        mvc.perform(post("/courses/new-course/add")
                        .with(csrf())
                        .param("name", courseDto.getName())
                        .param("description", courseDto.getDescription())
                        .param("groups", String.valueOf(Collections.singletonList(group)))
                        .param("teachers", String.valueOf(Collections.singletonList(teacher))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testEditCourse_whenEditCoursePost_thenReturnCoursePage() throws Exception {
        Group group = new Group();
        Teacher teacher = new Teacher();

        CourseDto courseDto = createCourseDto(Collections.singleton(1L), Collections.singleton(1L));

        mvc.perform(post("/courses/edit-course/update/{id}", courseDto.getId())
                        .with(csrf())
                        .param("name", courseDto.getName())
                        .param("description", courseDto.getDescription())
                        .param("groups", String.valueOf(Collections.singletonList(group)))
                        .param("teachers", String.valueOf(Collections.singletonList(teacher))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(COURSES_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testEditCourse_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        Group group = new Group();
        Teacher teacher = new Teacher();

        CourseDto courseDto = createCourseDto(Collections.singleton(1L), Collections.singleton(1L));

        mvc.perform(post("/courses/edit-course/update/{id}", courseDto.getId())
                        .with(csrf())
                        .param("name", courseDto.getName())
                        .param("description", courseDto.getDescription())
                        .param("groups", String.valueOf(Collections.singletonList(group)))
                        .param("teachers", String.valueOf(Collections.singletonList(teacher))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteCourse_whenDeleteCourse_thenReturnCourses() throws Exception {
        Long userId =  1L;

        doNothing().when(courseService).delete(userId);

        mvc.perform(post("/courses/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(COURSES_PAGE));

        verify(courseService, times(1)).delete(userId);
    }

    @Test
    @WithAnonymousUser
    void testDeleteCourse_whenCourseIsAnonymous_thenReturnUnauthorized() throws Exception {
        Long userId =  1L;

        doNothing().when(courseService).delete(userId);

        mvc.perform(post("/course/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    private CourseDto createCourseDto(Set<Long> tIds, Set<Long> gIds) {
        return CourseDto.builder()
                .id(1L)
                .name("course")
                .description("desc")
                .teachersIds(tIds)
                .groupsIds(gIds)
                .build();
    }
}


