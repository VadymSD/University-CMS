package vadym.spring.console.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.TeacherRepository;
import vadym.spring.console.app.entity.Teacher;
import vadym.spring.console.app.service.impl.TeacherServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TeacherServiceImpl.class})
class TeacherServiceImplTest {
    @MockBean
    TeacherRepository teacherRepository;

    @MockBean
    CourseRepository courseRepository;

    @Autowired
    TeacherServiceImpl teacherServiceImpl;

    @Test
    void shouldReturnListOfTeachersAssignedToCourse() {
        List<Teacher> teachers = generateListOfTeacher();
        String courseName = "Math";

        try {
            when(teacherRepository.findTeachersAssignedToCourseByName(courseName)).thenReturn(teachers);
        } catch (SQLException e) {
            fail("Unexpected exception");
        }

        List<Teacher> teacherList = teacherServiceImpl.findTeacherAssignedToCourseByName(courseName);

        assertEquals(teachers, teacherList);

        try {
            verify(teacherRepository).findTeachersAssignedToCourseByName(courseName);
        } catch (SQLException e) {
            fail("Unexpected exception");
        }
    }

    private List<Teacher> generateListOfTeacher() {
        return IntStream.rangeClosed(1, 5)
                .mapToObj(i -> Teacher.builder().firstName("name" + i).surname("surname" + i).build())
                .collect(Collectors.toList());
    }
}
