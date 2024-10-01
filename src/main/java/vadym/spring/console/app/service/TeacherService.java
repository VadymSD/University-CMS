package vadym.spring.console.app.service;
import vadym.spring.console.app.dto.TeacherDto;
import vadym.spring.console.app.entity.Teacher;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TeacherService extends CommonService<Teacher, TeacherDto> {
    List<Teacher> findTeacherAssignedToCourseByName(String name) throws SQLException;

    Optional<Teacher> findById(Long id);

    List<Teacher> findAllTeachers(String name);
}
