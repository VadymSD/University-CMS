package vadym.spring.console.app.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;
import vadym.spring.console.app.entity.Lecture;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = {LectureRepository.class}))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"/sql/clear_tables.sql", "/sql/sample_data.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class LectureRepositoryTest {

    @Autowired
    LectureRepository lectureRepository;

    @Test
    void findLecturesByTeacherNameTest() {
        String name = "Mark";

        List<Lecture> lectureList = lectureRepository.findLectureByTeacherName(name);
        assertNotNull(lectureList);
        assertEquals(1, lectureList.size());
    }

    @Test
    void findLecturesByStudentIdTest() {
        Long id = 1L;

        List<Lecture> lectureList = lectureRepository.findLecturesByStudentId(id);
        assertNotNull(lectureList);
        assertEquals(1, lectureList.size());
    }
}
