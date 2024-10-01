package vadym.spring.console.app.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.FacultyRepository;
import vadym.spring.console.app.repository.TeacherRepository;
import vadym.spring.console.app.repository.UserRepository;
import vadym.spring.console.app.dto.TeacherDto;
import ua.com.foxminded.vadym.spring.console.app.entity.*;
import vadym.spring.console.app.helpers.TeacherHelper;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.TeacherService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Teacher;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    private final CourseRepository courseRepository;

    private final FacultyRepository facultyRepository;

    private final UserRepository userRepository;

    private final TeacherHelper teacherHelper;

    private final CourseService courseService;

    Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Autowired
    public TeacherServiceImpl(TeacherRepository teacherRepository, CourseRepository courseRepository, UserRepository userRepository, CourseService courseService, FacultyRepository facultyRepository, TeacherHelper teacherHelper) {
        this.teacherRepository = teacherRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseService = courseService;
        this.facultyRepository = facultyRepository;
        this.teacherHelper = teacherHelper;
    }

    @Override
    public Optional<Teacher> findByName(String name) throws RuntimeException {
        try {
            logger.info("Teacher with name {} found", name);
            return teacherRepository.findByFirstName(name);
        } catch (SQLException e) {
            logger.error("Error while finding teacher with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        try {
            logger.info("Student with id {} found", id);
            return teacherRepository.findById(id);
        } catch (RuntimeException e) {
            logger.error("Error while finding student with id {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Teacher> findAllTeachers(String name) {
        if(name!= null) {
            try {
                logger.info("Teachers assigned to course {} found", name);
                return teacherRepository.findTeachersAssignedToCourseByName(name);
            } catch (SQLException e) {
                logger.error("Error while finding teachers assigned to course {}", name, e);
                return Collections.emptyList();
            }
        }

        return teacherRepository.findAll();
    }

    @Override
    public List<Teacher> findAll() throws RuntimeException {
        return teacherRepository.findAll();
    }

    @Override
    public  List<Teacher> findTeacherAssignedToCourseByName(String name) throws RuntimeException {
        try {
            logger.info("Teacher assigned to course with name {} found.", name);
            return teacherRepository.findTeachersAssignedToCourseByName(name);
        } catch (SQLException e) {
            logger.error("Error while finding teacher with name {}", name, e);
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public TeacherDto save(TeacherDto teacherDto) throws RuntimeException {
        Teacher teacher = teacherHelper.buildTeacher(teacherDto, courseRepository, facultyRepository, userRepository);

        addCoursesOnSave(teacher);

        teacherRepository.save(teacher);

        return teacherDto;
    }

    @Override
    @Transactional
    public void update(TeacherDto teacherDto) throws RuntimeException {
        Teacher teacherToUpdate = teacherRepository.findById(teacherDto.getFacultyId()).orElseThrow(RuntimeException::new);

        Teacher updatedTeacher = teacherHelper.buildTeacher(teacherDto, courseRepository, facultyRepository, userRepository);

        updateCourses(teacherToUpdate, updatedTeacher);

        teacherRepository.save(updatedTeacher);
    }

    @Override
    @Transactional
    public void delete(Long id) throws RuntimeException {
        teacherRepository.deleteById(id);
    }

    private void updateCourses(Teacher teacherToUpdate, Teacher updatedTeacher) {
        List<Course> existingCourses = new ArrayList<>(teacherToUpdate.getCourses());
        List<Course> updatedCourses = new ArrayList<>(updatedTeacher.getCourses());

        existingCourses.removeAll(updatedCourses);
        removeCourses(existingCourses, teacherToUpdate.getId());

        updatedCourses.removeAll(teacherToUpdate.getCourses());
        addCoursesOnUpdate(updatedCourses, teacherToUpdate.getId());
    }

    private void removeCourses(List<Course> existingCourses, Long teacherId) {

        if (!existingCourses.isEmpty()) {
            existingCourses.forEach(course ->
                    courseService.removeCourseFromTeacher(course.getId(), teacherId));
        }
    }

    private void addCoursesOnUpdate(List<Course> updatedCourses, Long teacherId) {
        if (!updatedCourses.isEmpty()) {
            updatedCourses.forEach(course ->
                    courseService.addCourseToTeacher(course.getId(), teacherId));
        }
    }

    private void addCoursesOnSave(Teacher teacher) {
        teacher.getCourses().forEach(course ->{
            course.getTeachers().add(teacher);
            courseRepository.save(course);
        });
    }
}
