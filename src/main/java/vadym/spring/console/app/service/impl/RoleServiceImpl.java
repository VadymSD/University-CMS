package vadym.spring.console.app.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vadym.spring.console.app.repository.RoleRepository;
import vadym.spring.console.app.dto.RoleDto;
import vadym.spring.console.app.entity.Role;
import vadym.spring.console.app.service.RoleService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findByName(String name) {
        try {
            logger.info("Role with name {} found", name);
            return roleRepository.findByRoleName(name);
        } catch (SQLException e) {
            logger.error("Error while finding role with name {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Role> findById(Long id) throws RuntimeException {
        try {
            logger.info("Role with id {} found", id);
            return roleRepository.findById(id);
        } catch (RuntimeException e) {
            logger.error("Error while finding role with id {}", id, e);
            return Optional.empty();
        }
    }


    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public RoleDto save(RoleDto roleDto) throws RuntimeException {
        Role role = Role.builder()
                .id(roleDto.getId())
                .roleName(roleDto.getRoleName())
                .build();

        roleRepository.save(role);

        return roleDto;
    }

    @Override
    @Transactional
    public void update(RoleDto roleDto) throws RuntimeException {
        Role role = Role.builder()
                .id(roleDto.getId())
                .roleName(roleDto.getRoleName())
                .build();

        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void delete(Long id) throws RuntimeException {
        roleRepository.deleteById(id);
    }
}


