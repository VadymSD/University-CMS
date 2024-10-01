package vadym.spring.console.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.com.foxminded.vadym.spring.console.app.repository.*;
import ua.com.foxminded.vadym.spring.console.app.entity.*;
import vadym.spring.console.app.entity.Lecture;
import vadym.spring.console.app.entity.Role;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.entity.Teacher;
import vadym.spring.console.app.entity.UserEntity;
import vadym.spring.console.app.helpers.LectureHelper;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.LectureRepository;
import vadym.spring.console.app.repository.UserRepository;
import vadym.spring.console.app.service.impl.LectureServiceImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import vadym.spring.console.app.repository.TeacherRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {LectureServiceImpl.class})
class LectureServiceImplTest {
    @MockBean
    LectureRepository lectureRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    CourseRepository courseRepository;

    @MockBean
    GroupRepository groupRepository;

    @MockBean
    TeacherRepository teacherRepository;

    @MockBean
    LectureHelper lectureHelper;

    @Autowired
    LectureServiceImpl lectureService;

    @Test
    void getLecturesByUsernameWithinDateRangeTest() {
        Lecture lecture = new Lecture();
        UserEntity user = spy(new UserEntity());
        Student student = new Student();
        Teacher teacher = new Teacher();
        Role role = new Role();
        role.setRoleName("ROLE_STUDENT");

        try {
            when(lectureRepository.findLecturesByDateBetween(LocalDate.now(), LocalDate.now()))
                    .thenReturn(Collections.singletonList(lecture));
            when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
            when(user.getRole()).thenReturn(role);
            when(user.getStudent()).thenReturn(student);
            when(user.getTeacher()).thenReturn(teacher);
            when(lectureRepository.findLecturesByStudentId(student.getId())).thenReturn(Collections.singletonList(lecture));
        } catch (SQLException e) {
            fail("Unexpected exception");
        }

        List<Lecture> result = lectureService.getLecturesByUsernameWithinDateRange("username", LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
