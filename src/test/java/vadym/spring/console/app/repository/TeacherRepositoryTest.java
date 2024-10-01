package vadym.spring.console.app.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import vadym.spring.console.app.entity.Teacher;

import java.sql.SQLException;
import java.util.List;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        TeacherRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository dao;

    @Test
    void findTeachersAssignedToCourseByNameTest() {
        String courseName = "Math";

        try {
            List<Teacher> teacherList = dao.findTeachersAssignedToCourseByName(courseName);

            assertNotNull(teacherList);
            assertEquals(0, teacherList.size());
        } catch (SQLException e) {
            fail("Unexpected Exception");
        }
    }
}