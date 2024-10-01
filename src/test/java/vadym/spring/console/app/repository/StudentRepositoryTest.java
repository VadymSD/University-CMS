package vadym.spring.console.app.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import vadym.spring.console.app.entity.Student;

import java.sql.SQLException;
import java.util.List;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        StudentRepository.class
}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(
        scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class StudentRepositoryTest {

    @Autowired
    private StudentRepository dao;

    @Test
    void findStudentsAssignedToGroupByNameTest() {
        String groupName = "AA";

        try {
            List<Student> studentList = dao.findStudentsAssignedToGroupByName(groupName);

            assertNotNull(studentList);
            assertEquals(0, studentList.size());
        } catch (SQLException e) {
            fail("Unexpected Exception");
        }
    }
}
