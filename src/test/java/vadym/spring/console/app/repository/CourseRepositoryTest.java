package vadym.spring.console.app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import vadym.spring.console.app.entity.Course;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {CourseRepository.class}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class CourseRepositoryTest {

    @Autowired
    CourseRepository courseRepository;

    @Test
    void findCourseAssignedToTeacherByNameTest() {
        String teacherName = "Mark";

        List<Course> courses = courseRepository.findCourseByTeachersName(teacherName);
        assertNotNull(courses);
        assertEquals(0, courses.size());
    }

    @Test
    void findCoursesByTeacherIdTest() {
        Long teacherId = 1L;

        List<Course> courses = courseRepository.findCourseByTeacherId(teacherId);
        assertNotNull(courses);
        assertEquals(0, courses.size());
    }

    @Test
    void findCoursesByStudentIdTest() {
        Long studentId = 1L;

        List<Course> courses = courseRepository.findCourseByStudentId(studentId);
        assertNotNull(courses);
        assertEquals(0, courses.size());
    }
}
