package vadym.spring.console.app.service;

import vadym.spring.console.app.dto.UserDto;
import vadym.spring.console.app.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserEntityService extends CommonService<UserEntity, UserDto> {
    Optional<UserEntity> findById(Long id);

    List<UserEntity> findAllUnassignedUsers();
}
