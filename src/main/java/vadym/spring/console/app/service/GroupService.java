package vadym.spring.console.app.service;

import vadym.spring.console.app.dto.GroupDto;
import vadym.spring.console.app.entity.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService extends CommonService<Group, GroupDto> {
    List<Group> findGroupsWithLessOrEqualStudents(int studentCount);

    List<Group> findGroupsAssignedToCourseByName(String name);

    Optional<Group> findById(Long id);

    List<Group> findAllGroups(String name);
}
