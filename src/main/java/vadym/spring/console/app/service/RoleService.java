package vadym.spring.console.app.service;

import vadym.spring.console.app.dto.RoleDto;
import vadym.spring.console.app.entity.Role;

import java.util.Optional;

public interface RoleService extends CommonService<Role, RoleDto> {
    Optional<Role> findById(Long id);
}
