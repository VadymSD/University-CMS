package vadym.spring.console.app.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import vadym.spring.console.app.repository.UserRepository;
import vadym.spring.console.app.entity.Role;
import vadym.spring.console.app.entity.UserEntity;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {UserDetailsServiceImpl.class})
class UserDetailsServiceTest {
    @MockBean
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldReturnUser() {
        String userName = "existingUserName";

        Role role = Role.builder()
                .roleName("role")
                .id((long) 1)
                .build();

        UserEntity user = UserEntity.builder()
                .role(role)
                .username(userName)
                .password("1234")
                .id((long) 1)
                .build();

        try {
            when(userRepository.findByUsername(userName)).thenReturn(Optional.ofNullable(user));
        } catch (SQLException e) {
            fail("Unexpected exception");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
    }
}
