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
import vadym.spring.console.app.dto.TeacherDto;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Faculty;
import vadym.spring.console.app.entity.Teacher;
import vadym.spring.console.app.helpers.TeacherHelper;
import ua.com.foxminded.vadym.spring.console.app.service.*;

import java.util.*;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.FacultyService;
import vadym.spring.console.app.service.TeacherService;
import vadym.spring.console.app.service.UserEntityService;

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
@WebMvcTest(TeacherController.class)
@WithMockUser
class TeacherControllerIntegrationTest {

    private final String TEACHER_PAGE = "/teachers";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    private TeacherHelper teacherHelper;

    @MockBean
    private UserEntityService userEntityService;

    @Test
    void givenTeachers_whenGetTeachers_thenReturnJsonArray() throws Exception {
        Faculty faculty = new Faculty();
        Teacher teacher = new Teacher();

        faculty.setFacultyName("Math");
        teacher.setFaculty(faculty);

        given(teacherService.findAllTeachers(null)).willReturn(Collections.singletonList(teacher));

        mvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers"))
                .andExpect(model().attributeExists("teachers"))
                .andExpect(model().attribute("teachers", hasSize(1)));
    }

    @Test
    void testAddTeacher_whenAddGet_thenReturnCreatePage() throws Exception {
        given(courseService.findAll()).willReturn(Collections.singletonList(new Course()));
        given(facultyService.findAll()).willReturn(Collections.singletonList(new Faculty()));


        mvc.perform(get("/teachers/new-teacher"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-teacher"))
                .andExpect(model().attributeExists("teacher"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attributeExists("faculties"))
                .andExpect(model().attribute("courses", hasSize(1)))
                .andExpect(model().attribute("teacher", notNullValue()));
    }

    @Test
    void testEditTeacher_whenEditTeacherGet_thenReturnEditPage() throws Exception {
        TeacherDto teacherDto = createTeacherDto(Collections.singleton(1L));

        given(teacherService.findById(teacherDto.getId())).willReturn(Optional.of(new Teacher()));
        given(teacherHelper.buildTeacherDto(new Teacher())).willReturn(teacherDto);
        given(courseService.findAll()).willReturn(Collections.singletonList(new Course()));
        given(facultyService.findAll()).willReturn(Collections.singletonList(new Faculty()));

        mvc.perform(get("/teachers/edit-teacher/{id}", teacherDto.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("update-teacher"))
                .andExpect(model().attributeExists("teacher"))
                .andExpect(model().attributeExists("faculties"))
                .andExpect(model().attributeExists("courses"))
                .andExpect(model().attribute("faculties", hasSize(1)))
                .andExpect(model().attribute("courses", hasSize(1)))
                .andExpect(model().attribute("teacher", notNullValue()));
    }

    @Test
    void testCreateTeacher_whenCreateTeacherPost_thenReturnTeachers() throws Exception {
        TeacherDto teacherDto = createTeacherDto(Collections.singleton(1L));

        mvc.perform(post("/teachers/new-teacher/add")
                        .with(csrf())
                        .param("firstName", teacherDto.getFirstName())
                        .param("surname", teacherDto.getSurname())
                        .param("courses", String.valueOf(Collections.singletonList(new Course())))
                        .param("faculties", String.valueOf(Collections.singletonList(new Faculty()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TEACHER_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testAddTeacher_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        TeacherDto teacherDto = createTeacherDto(Collections.singleton(1L));

        mvc.perform(post("/teachers/new-teachers/add")
                        .with(csrf())
                        .param("firstName", teacherDto.getFirstName())
                        .param("surname", teacherDto.getSurname())
                        .param("courses", String.valueOf(Collections.singletonList(new Course())))
                        .param("faculties", String.valueOf(Collections.singletonList(new Faculty()))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testEditTeacher_whenEditTeacherPost_thenReturnTeacherPage() throws Exception {
        TeacherDto teacherDto = createTeacherDto(Collections.singleton(1L));

        mvc.perform(post("/teachers/edit-teacher/update/{id}", teacherDto.getId())
                        .with(csrf())
                        .param("firstName", teacherDto.getFirstName())
                        .param("surname", teacherDto.getSurname())
                        .param("courses", String.valueOf(Collections.singletonList(new Course())))
                        .param("faculties", String.valueOf(Collections.singletonList(new Faculty()))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TEACHER_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testEditTeacher_whenUserIsAnonymous_thenReturnUnauthorized() throws Exception {
        TeacherDto teacherDto = createTeacherDto(Collections.singleton(1L));

        mvc.perform(post("/teachers/edit-teacher/update/{id}", teacherDto.getId())
                        .with(csrf())
                        .param("firstName", teacherDto.getFirstName())
                        .param("surname", teacherDto.getSurname())
                        .param("courses", String.valueOf(Collections.singletonList(new Course())))
                        .param("faculties", String.valueOf(Collections.singletonList(new Faculty()))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteTeacher_whenDeleteTeacher_thenReturnTeachers() throws Exception {
        Long userId =  1L;

        doNothing().when(courseService).delete(userId);

        mvc.perform(post("/teachers/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(TEACHER_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testDeleteTeacher_whenTeacherIsAnonymous_thenReturnUnauthorized() throws Exception {
        Long userId =  1L;

        doNothing().when(courseService).delete(userId);

        mvc.perform(post("/teachers/delete/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    private TeacherDto createTeacherDto(Set<Long> courseIds) {
        return TeacherDto.builder()
                .id(1L)
                .firstName("first name")
                .surname("surname")
                .coursesIds(courseIds)
                .facultyId(1L)
                .build();
    }
}


