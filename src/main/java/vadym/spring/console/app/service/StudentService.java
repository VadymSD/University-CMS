package vadym.spring.console.app.service;

import vadym.spring.console.app.dto.StudentDto;
import vadym.spring.console.app.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService extends CommonService<Student, StudentDto> {
    List<Student> findAllStudents(String name);

    Optional<Student> findById(Long id);

    List<Student> findStudentsAssignedToGroupByName(String name);

    Student addStudentToGroup(Long studentId, Long groupId);

    Student removeStudentFromGroup(Long studentId);
}
