package vadym.spring.console.app.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vadym.spring.console.app.repository.CourseRepository;
import vadym.spring.console.app.repository.GroupRepository;
import vadym.spring.console.app.repository.StudentRepository;
import vadym.spring.console.app.dto.GroupDto;
import vadym.spring.console.app.entity.Course;
import vadym.spring.console.app.entity.Group;
import vadym.spring.console.app.entity.Student;
import vadym.spring.console.app.helpers.GroupHelper;
import vadym.spring.console.app.service.CourseService;
import vadym.spring.console.app.service.GroupService;
import vadym.spring.console.app.service.StudentService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    private final CourseRepository courseRepository;

    private final StudentRepository studentRepository;

    private final StudentService studentService;

    private final CourseService courseService;

    private final GroupHelper groupHelper;

    Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, CourseRepository courseRepository, StudentRepository studentRepository, StudentService studentService, CourseService courseService, GroupHelper groupHelper) {
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.courseService = courseService;
        this.groupHelper = groupHelper;
    }

    @Override
    public Optional<Group> findByName(String name) {
        try {
            logger.info("Group with name {} found", name);
            return  groupRepository.findByGroupName(name);
        } catch (SQLException e) {
            logger.error("Error while finding group with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Group> findById(Long id) {
        try {
            logger.info("Group with id {} found", id);
            return  groupRepository.findById(id);
        } catch (RuntimeException e) {
            logger.error("Error while finding group with id {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<Group> findAllGroups(String name) {
        if(name!= null) {
            try {
                logger.info("Students assigned to group {} found", name);
                return groupRepository.findGroupsAssignedToCourseByName(name);
            } catch (SQLException e) {
                logger.error("Error while finding students assigned to group {}", name, e);
                return Collections.emptyList();
            }
        }

        return groupRepository.findAll();
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public List<Group> findGroupsWithLessOrEqualStudents(int studentCount) {
        try {
            logger.info("Groups with less or equal to {} students found", studentCount);
            return groupRepository.findGroupsWithLessOrEqualStudents(studentCount);
        } catch (SQLException e) {
            logger.error("Error while finding groups with less or equal to {} students", studentCount, e);
            return Collections.emptyList();
        }
    }

    @Override
    public List<Group> findGroupsAssignedToCourseByName(String name) {
        try {
            logger.info("Groups assigned to course {} found", name);
            return groupRepository.findGroupsAssignedToCourseByName(name);
        } catch (SQLException e) {
            logger.error("Error while finding groups assigned to course {}", name, e);
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public GroupDto save(GroupDto groupDto) throws RuntimeException {
        Group group = groupHelper.buildGroup(groupDto, courseRepository, studentRepository);

        addCoursesOnSave(group);
        addStudentsOnSave(group);

        groupRepository.save(group);

        return groupDto;
    }

    @Override
    @Transactional
    public void update(GroupDto groupDto) throws RuntimeException {
        Group groupToUpdate = groupRepository.findById(groupDto.getId())
                .orElseThrow(() -> new RuntimeException("Group not found"));

        Group updatedGroup = groupHelper.buildGroup(groupDto, courseRepository, studentRepository);

        updateCourses(groupToUpdate, updatedGroup);
        updateStudents(groupToUpdate, updatedGroup);

        groupRepository.save(updatedGroup);
    }

    @Override
    @Transactional
    public void delete(Long id) throws RuntimeException {
        Group groupToDelete = groupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        removeCourses(groupToDelete.getCourses(), id);

        removeStudents(groupToDelete);

        groupRepository.deleteById(id);
    }

    private void updateCourses(Group groupToUpdate, Group updatedGroup) {
        List<Course> existingCourses = new ArrayList<>(groupToUpdate.getCourses());
        List<Course> updatedCourses = new ArrayList<>(updatedGroup.getCourses());

        existingCourses.removeAll(updatedCourses);
        removeCourses(existingCourses, groupToUpdate.getId());

        updatedCourses.removeAll(groupToUpdate.getCourses());
        addCoursesOnUpdate(updatedCourses, groupToUpdate.getId());
    }

    private void updateStudents(Group groupToUpdate, Group updatedGroup) {
        removeStudents(groupToUpdate);
        addStudentsOnUpdate(updatedGroup);
    }

    private void removeStudents(Group group) {
        List<Student> existingStudents = new ArrayList<>(group.getStudents());

        if(!existingStudents.isEmpty()) {
            group.getStudents().forEach(student ->
                    studentService.removeStudentFromGroup(student.getId()));
        }
    }

    private void addStudentsOnUpdate(Group group) {
        List<Student> updatedStudents = new ArrayList<>(group.getStudents());

        if (!updatedStudents.isEmpty()) {
            group.getStudents().forEach(student ->
                    studentService.addStudentToGroup(student.getId(), group.getId()));
        }
    }

    private void removeCourses(List<Course> existingCourses, Long groupId) {

        if (!existingCourses.isEmpty()) {
            existingCourses.forEach(course ->
                    courseService.removeCourseFromGroup(course.getId(), groupId));
        }
    }

    private void addCoursesOnUpdate(List<Course> updatedCourses, Long groupId) {
        if (!updatedCourses.isEmpty()) {
            updatedCourses.forEach(course ->
                    courseService.addCourseToGroup(course.getId(), groupId));
        }
    }

    private void addCoursesOnSave(Group group) {
        group.getCourses().forEach(course ->{
            course.getGroups().add(group);
            courseRepository.save(course);
        });
    }

    private void addStudentsOnSave(Group group) {
        group.getStudents().forEach(student -> {
            studentService.removeStudentFromGroup(student.getId());
            student.setGroup(group);
            studentRepository.save(student);
        });
    }
}

