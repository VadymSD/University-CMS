package vadym.spring.console.app.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.StudentRepository;
import vadym.spring.console.app.repository.UserRepository;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.service.impl.StudentServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {StudentServiceImpl.class})
class StudentServiceImplTest {
    @MockBean
    StudentRepository studentRepository;

    @MockBean
    GroupRepository groupRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    StudentServiceImpl studentServiceImpl;

    @Test
    void shouldReturnListOfStudentsAssignedToGroup() {
        List<Student> students = generateListOfStudent();
        String groupName = "Math";

        try {
            when(studentRepository.findStudentsAssignedToGroupByName(groupName)).thenReturn(students);

            List<Student> studentList = studentServiceImpl.findStudentsAssignedToGroupByName(groupName);

            assertEquals(students, studentList);

            verify(studentRepository).findStudentsAssignedToGroupByName(groupName);
        } catch (SQLException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    void shouldBeAddedToGroup() {
        Student student = spy(new Student());
        Group group = new Group();

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        studentServiceImpl.removeStudentFromGroup(student.getId());
        verify(studentRepository).save(student);
    }

    @Test
    void shouldBeRemovedFromGroup() {
        Student student = spy(new Student());

        when(studentRepository.findById(student.getId())).thenReturn(Optional.of(student));

        studentServiceImpl.removeStudentFromGroup(student.getId());
        verify(studentRepository).save(student);
    }

    private List<Student> generateListOfStudent() {
        return IntStream.rangeClosed(1, 5)
                .mapToObj(i -> Student.builder().firstName("name" + i).surname("surname" + i).build())
                .collect(Collectors.toList());
    }
}

