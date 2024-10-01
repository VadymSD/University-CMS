package vadym.spring.console.app.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.vadym.spring.console.app.repository.*;
import vadym.spring.console.app.dto.LectureDto;
import ua.com.foxminded.vadym.spring.console.app.entity.*;
import vadym.spring.console.app.helpers.LectureHelper;
import vadym.spring.console.app.service.LectureService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import vadym.spring.console.app.entity.Lecture;
import vadym.spring.console.app.entity.Role;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.entity.Teacher;
import vadym.spring.console.app.entity.UserEntity;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.LectureRepository;
import vadym.spring.console.app.repository.TeacherRepository;
import vadym.spring.console.app.repository.UserRepository;

@Service
public class LectureServiceImpl implements LectureService {
    private final LectureRepository lectureRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    private final GroupRepository groupRepository;

    private final TeacherRepository teacherRepository;

    private final LectureHelper lectureHelper;

    Logger logger = LoggerFactory.getLogger(LectureServiceImpl.class);

    @Autowired
    public LectureServiceImpl(LectureRepository lectureRepository, UserRepository userRepository, CourseRepository courseRepository,
                              GroupRepository groupRepository, TeacherRepository teacherRepository, LectureHelper lectureHelper) {
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
        this.teacherRepository = teacherRepository;
        this.lectureHelper = lectureHelper;
    }

    @Override
    public Optional<Lecture> findByName(String name) {
        try {
            logger.info("Lecture with name {} found", name);
            return lectureRepository.findByLectureName(name);
        } catch (SQLException e) {
            logger.error("Error while finding lecture with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Lecture> findById(Long id) {
        return lectureRepository.findById(id);
    }

    @Override
    public List<Lecture> findAll() {
        return lectureRepository.findAll();
    }

    @Override
    public List<Lecture> findAllLectures(String name) throws  RuntimeException {
        if(name!= null) {
            try {
                logger.info("Students assigned to group {} found", name);
                return lectureRepository.findLectureByTeacherName(name);
            } catch (RuntimeException e) {
                logger.error("Error while finding students assigned to group {}", name, e);
                return Collections.emptyList();
            }
        }

        return lectureRepository.findAll();
    }

    @Override
    @Transactional
    public LectureDto save(LectureDto lectureDto) throws RuntimeException {
        Lecture lecture = lectureHelper.buildLecture(lectureDto, groupRepository, courseRepository, teacherRepository);

        lectureRepository.save(lecture);

        return lectureDto;
    }

    @Override
    @Transactional
    public void update(LectureDto lectureDto) throws RuntimeException {
        Lecture lecture = lectureHelper.buildLecture(lectureDto, groupRepository, courseRepository, teacherRepository);

        lectureRepository.save(lecture);
    }

    @Override
    @Transactional
    public void delete(Long id) throws RuntimeException {
        lectureRepository.deleteById(id);
    }

    @Override
    public List<Lecture> getLecturesByUsernameWithinDateRange(String lectureName, LocalDate startDate, LocalDate endDate) {
        List<Lecture> selectedLectures = lectureRepository.findLecturesByDateBetween(startDate, endDate);
        List<Lecture> userLectures = getLecturesByUsername(lectureName);

        List<Long> selectedLectureIds = selectedLectures.stream()
                .map(Lecture::getId)
                .toList();

        return userLectures.stream()
                .filter(lecture -> selectedLectureIds.contains(lecture.getId()))
                .toList();
    }

    private List<Lecture> getLecturesByUsername(String lectureName) {
        try {
            UserEntity user = userRepository.findByUsername(lectureName)
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            Role role = user.getRole();
            if (role == null) {
                throw new IllegalStateException("User role is not defined");
            }

            switch (role.getRoleName()) {
                case "ROLE_STUDENT":
                    Student student = user.getStudent();
                    if (student == null) {
                        throw new IllegalStateException("Student information not found for the lecture");
                    }
                    return lectureRepository.findLecturesByStudentId(student.getId());
                case "ROLE_TEACHER":
                    Teacher teacher = user.getTeacher();
                    if (teacher == null) {
                        throw new IllegalStateException("Teacher information not found for the lecture");
                    }
                    return lectureRepository.findLecturesByTeacherId(teacher.getId());
                default:
                    return Collections.emptyList();
            }
        } catch (SQLException | IllegalStateException e) {
            return Collections.emptyList();
        }
    }
}


