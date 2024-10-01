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
import vadym.spring.console.app.dto.StudentDto;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.service.GroupService;
import vadym.spring.console.app.service.StudentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
@WebMvcTest(StudentController.class)
@WithMockUser
class StudentControllerIntegrationTest {

    private static final String STUDENTS_PAGE = "/students";

    private static final String FIRST_NAME = "test";

    private static final String SURNAME = "test";

    private static final String GROUP_ID = "groupId";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private GroupService groupService;

    @Test
    void givenStudents_whenGetStudents_thenReturnStudentPage()
            throws Exception {

        Student alex = new Student();

        alex.setGroup(new Group());

        List<Student> allStudents = Arrays.asList(alex);

        given(studentService.findAllStudents(null)).willReturn(allStudents);

        mvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students"))
                .andExpect(model().attributeExists("students"))
                .andExpect(model().attribute("students", hasSize(1)));
    }

    @Test
    void testAddStudent_whenAddGet_thenReturnCreatePage() throws Exception {
        given(groupService.findAll()).willReturn(Collections.singletonList(new Group()));

        mvc.perform(get("/students/new-student"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-student"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", hasSize(1)))
                .andExpect(model().attribute("student", notNullValue()));
    }

    @Test
    void testEditStudent_whenEditStudentGet_thenReturnEditPage() throws Exception {
        Group group = createGroup();
        Student student = createStudent(group);

        given(studentService.findById(student.getId())).willReturn(Optional.of(student));
        given(groupService.findAll()).willReturn(Collections.singletonList(group));

        mvc.perform(get("/students/edit-student/{id}", student.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("update-student"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", hasSize(1)))
                .andExpect(model().attribute("student", notNullValue()));
    }

    @Test
    void testEditStudent_whenEditStudentPost_thenReturnAdminPage() throws Exception {
        Group group = createGroup();
        StudentDto studentDto = createStudentDto(group);

        mvc.perform(post("/students/edit-student/update/{id}", studentDto.getId())
                        .with(csrf())
                        .param(FIRST_NAME, studentDto.getFirstName())
                        .param(SURNAME, studentDto.getSurname())
                        .param(GROUP_ID, String.valueOf(studentDto.getGroupId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(STUDENTS_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testEditStudent_whenStudentIsAnonymous_thenReturnUnauthorized() throws Exception {
        Group group = createGroup();
        StudentDto studentDto = createStudentDto(group);

        mvc.perform(post("/students/edit-student/update/{id}", studentDto.getId())
                        .with(csrf())
                        .param(FIRST_NAME, studentDto.getFirstName())
                        .param(SURNAME, studentDto.getSurname())
                        .param(GROUP_ID, String.valueOf(studentDto.getGroupId())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateStudent_whenCreateStudentPost_thenReturnAdminPage() throws Exception {
        Group group = createGroup();
        StudentDto studentDto = createStudentDto(group);

        mvc.perform(post("/students/new-student/add", studentDto.getId())
                        .with(csrf())
                        .param(FIRST_NAME, studentDto.getFirstName())
                        .param(SURNAME, studentDto.getSurname())
                        .param(GROUP_ID, String.valueOf(studentDto.getGroupId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(STUDENTS_PAGE));
    }

    @Test
    @WithAnonymousUser
    void testAddStudent_whenStudentIsAnonymous_thenReturnUnauthorized() throws Exception {
        Group group = createGroup();
        StudentDto studentDto = createStudentDto(group);

        mvc.perform(post("/students/new-student/add", studentDto.getId())
                        .with(csrf())
                        .param(FIRST_NAME, studentDto.getFirstName())
                        .param(SURNAME, studentDto.getSurname())
                        .param(GROUP_ID, String.valueOf(studentDto.getGroupId())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteStudent_whenDeleteStudent_thenReturnAdminPage() throws Exception {
        Long studentId =  1L;

        doNothing().when(studentService).delete(studentId);

        mvc.perform(post("/students/delete/{id}", studentId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(STUDENTS_PAGE));

        verify(studentService, times(1)).delete(studentId);
    }

    @Test
    @WithAnonymousUser
    void testDeleteStudent_whenStudentIsAnonymous_thenReturnUnauthorized() throws Exception {
        Long studentId =  1L;

        doNothing().when(studentService).delete(studentId);

        mvc.perform(post("/students/delete/{id}", studentId)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }


    private Group createGroup() {
        return Group.builder()
                .groupName("group")
                .id(1L)
                .build();
    }

    private Student createStudent(Group group) {
        return Student.builder()
                .group(group)
                .firstName(FIRST_NAME)
                .surname(SURNAME)
                .id(1L)
                .build();
    }

    private StudentDto createStudentDto(Group group) {
        return StudentDto.builder()
                .groupId(group.getId())
                .firstName(FIRST_NAME)
                .surname(SURNAME)
                .id(1L)
                .build();
    }
}


