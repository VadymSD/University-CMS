package vadym.spring.console.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vadym.spring.console.app.repository.UserRepository;
import vadym.spring.console.app.entity.UserEntity;

import java.sql.SQLException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = null;
        try {
            user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Username not found" + username));
        } catch (SQLException e) {
            throw new RuntimeException("Problem while loading user by name", e);
        }

        return new User(user.getUsername(), user.getPassword(), user.getAuthorities());
    }
}
