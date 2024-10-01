package vadym.spring.console.app.service;

import vadym.spring.console.app.dto.LectureDto;
import vadym.spring.console.app.entity.Lecture;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureService extends CommonService<Lecture, LectureDto> {
    List<Lecture> findAllLectures(String username);

    List<Lecture> getLecturesByUsernameWithinDateRange(String lectureName, LocalDate startDate, LocalDate endDate);

    Optional<Lecture> findById(Long id);
}
