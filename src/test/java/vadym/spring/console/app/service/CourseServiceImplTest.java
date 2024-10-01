package vadym.spring.console.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.TeacherRepository;
import vadym.spring.console.app.repository.UserRepository;
import ua.com.foxminded.vadym.spring.console.app.entity.*;
import vadym.spring.console.app.helpers.CourseHelper;
import vadym.spring.console.app.service.impl.CourseServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Role;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.entity.Teacher;
import vadym.spring.console.app.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {CourseServiceImpl.class})
class CourseServiceImplTest {

    @MockBean
    GroupRepository groupRepository;

    @MockBean
    CourseRepository courseRepository;

    @MockBean
    CourseHelper courseHelper;

    @MockBean
    TeacherRepository teacherRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    CourseServiceImpl courseService;

    @Test
    void shouldBeRemovedFromGroup() {
        Group group = new Group();
        Course course = spy(new Course());

        List<Group> groups = new ArrayList<>();
        groups.add(group);
        course.setGroups(groups);

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(course.getGroups()).thenReturn(groups);

        courseService.removeCourseFromGroup(group.getId(), course.getId());
        verify(courseRepository).save(course);
    }

    @Test
    void shouldBeAddedToGroup() {
        Group group = new Group();
        Course course = spy(new Course());

        List<Group> groups = new ArrayList<>();
        groups.add(group);

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(course.getGroups()).thenReturn(groups);

        courseService.addCourseToGroup(group.getId(), course.getId());
        verify(courseRepository).save(course);
    }

    @Test
    void shouldBeRemovedFromTeacher() {
        Teacher teacher = new Teacher();
        Course course = spy(new Course());

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);
        course.setTeachers(teachers);

        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(course.getTeachers()).thenReturn(teachers);

        courseService.removeCourseFromTeacher(teacher.getId(), course.getId());
        verify(courseRepository).save(course);
    }

    @Test
    void shouldBeAddedToTeacher() {
        Teacher teacher = new Teacher();
        Course course = spy(new Course());

        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher);

        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(course.getTeachers()).thenReturn(teachers);

        courseService.addCourseToTeacher(teacher.getId(), course.getId());
        verify(courseRepository).save(course);
    }

    @Test
    void shouldReturnListOfCoursesByUsername() {
        Course course = spy(new Course());

        UserEntity userEntity = spy(new UserEntity());

        Role role = spy(new Role());
        role.setRoleName("ROLE_STUDENT");
        userEntity.setRole(role);

        Student student = new Student();
        userEntity.setStudent(student);

        try {
            when(userRepository.findByUsername("name")).thenReturn(Optional.of(userEntity));
            when(userEntity.getRole()).thenReturn(role);
            when(role.getRoleName()).thenReturn("ROLE_STUDENT");
            when(userEntity.getStudent()).thenReturn(student);
            when(courseRepository.findCourseByStudentId(student.getId())).thenReturn(Collections.singletonList(course));
        } catch (SQLException e) {
            fail("Unexpected exception");
        }

        List<Course> courses = courseService.getCoursesByUsername("name");
        assertEquals(1, courses.size());
    }
}
