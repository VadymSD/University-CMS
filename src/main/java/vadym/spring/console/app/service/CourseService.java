package vadym.spring.console.app.service;

import vadym.spring.console.app.dto.CourseDto;
import vadym.spring.console.app.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService extends CommonService<Course, CourseDto> {
    List<Course> findAllCourses(String name);

    Optional<Course> findById(Long id);

    Course addCourseToGroup(Long courseId, Long groupId);

    Course removeCourseFromGroup(Long courseId, Long groupId);

    Course addCourseToTeacher(Long courseId, Long teacherId);

    Course removeCourseFromTeacher(Long courseId, Long teacherId);

    List<Course> getCoursesByUsername(String username);
}
