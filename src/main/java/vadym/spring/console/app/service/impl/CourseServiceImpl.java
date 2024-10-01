package vadym.spring.console.app.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Role;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.entity.Teacher;
import vadym.spring.console.app.entity.UserEntity;
import vadym.spring.console.app.repository.UserRepository;
import ua.com.foxminded.vadym.spring.console.app.entity.*;
import vadym.spring.console.app.helpers.CourseHelper;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.TeacherRepository;
import vadym.spring.console.app.dto.CourseDto;
import vadym.spring.console.app.service.CourseService;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    private final TeacherRepository teacherRepository;

    private final GroupRepository groupRepository;

    private UserRepository userRepository;

    private final CourseHelper courseHelper;

    Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, TeacherRepository teacherRepository,
                             GroupRepository groupRepository, CourseHelper courseHelper, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.groupRepository = groupRepository;
        this.courseHelper = courseHelper;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Course> findByName(String name) throws RuntimeException {
        try {
            logger.info("Course with name {} found", name);
            return courseRepository.findByCourseName(name);
        } catch (RuntimeException e) {
            logger.error("Error while finding course with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Course> findById(Long id) {
        try {
            logger.info("Course with id {} found", id);
            return courseRepository.findById(id);
        } catch (RuntimeException e) {
            logger.error("Error while finding course with id {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Course> findAllCourses(String name) throws  RuntimeException {
        if(name!= null) {
            try {
                logger.info("Students assigned to group {} found", name);
                return courseRepository.findCourseByTeachersName(name);
            } catch (RuntimeException e) {
                logger.error("Error while finding students assigned to group {}", name, e);
                return Collections.emptyList();
            }
        }

        return courseRepository.findAll();
    }
    @Override
    public List<Course> findAll() throws RuntimeException {
        return courseRepository.findAll();
    }

    @Override
    @Transactional
    public CourseDto save(CourseDto courseDto) throws RuntimeException {
        Course course = courseHelper.buildCourse(courseDto, groupRepository, teacherRepository);

        courseRepository.save(course);

        return courseDto;
    }

    @Override
    @Transactional
    public void update(CourseDto courseDto) throws RuntimeException {
        Course course = courseHelper.buildCourse(courseDto, groupRepository, teacherRepository);

        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void delete(Long id) throws RuntimeException {
        courseRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Course addCourseToGroup(Long courseId, Long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<Course> course = courseRepository.findById(courseId);

        if(group.isPresent() && course.isPresent()) {
            course.get().getGroups().add(group.get());
            courseRepository.save(course.get());
            return course.get();
        }

        throw new NotFoundException("Group or course not found");
    }

    @Override
    @Transactional
    public Course removeCourseFromGroup(Long courseId, Long groupId) {
        Optional<Group> group = groupRepository.findById(groupId);
        Optional<Course> course = courseRepository.findById(courseId);

        if(group.isPresent() && course.isPresent()) {
            course.get().getGroups().remove(group.get());
            courseRepository.save(course.get());
            return course.get();
        }

        throw new NotFoundException("Group or course not found");
    }

    @Override
    @Transactional
    public Course addCourseToTeacher(Long courseId, Long teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        Optional<Course> course = courseRepository.findById(courseId);

        if(teacher.isPresent() && course.isPresent()) {
            course.get().getTeachers().add(teacher.get());
            courseRepository.save(course.get());
            return course.get();
        }

        throw new NotFoundException("Teacher or course not found");
    }

    @Override
    @Transactional
    public Course removeCourseFromTeacher(Long courseId, Long teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        Optional<Course> course = courseRepository.findById(courseId);

        if(teacher.isPresent() && course.isPresent()) {
            course.get().getTeachers().remove(teacher.get());
            courseRepository.save(course.get());
            return course.get();
        }

        throw new NotFoundException("Teacher or course not found");
    }

    @Override
    @Transactional
    public List<Course> getCoursesByUsername(String username) {
        try {
            UserEntity user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);

            Role role = user.getRole();
            if (role == null) {
                throw new IllegalStateException("User role is not defined");
            }

            switch (role.getRoleName()) {
                case "ROLE_STUDENT":
                    Student student = user.getStudent();
                    if (student == null) {
                        throw new IllegalStateException("Student information not found for the user");
                    }
                    return courseRepository.findCourseByStudentId(student.getId());
                case "ROLE_TEACHER":
                    Teacher teacher = user.getTeacher();
                    if (teacher == null) {
                        throw new IllegalStateException("Teacher information not found for the user");
                    }
                    return courseRepository.findCourseByTeacherId(teacher.getId());
                default:
                    return Collections.emptyList();
            }
        } catch (SQLException | IllegalStateException e) {
            return Collections.emptyList();
        }
    }
}


