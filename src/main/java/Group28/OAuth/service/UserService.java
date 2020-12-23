package Group28.OAuth.service;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    public UserService( ) {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            IDatabaseEditor db = DatabaseEditor.getInstance();
            List<Group28.OAuth.Domain.User> users = db.getUsersAccessObject().readAll();
            Group28.OAuth.Domain.User user1 = users.stream()
                    .filter(user -> username.equals(user.getUsername()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Student " + username + " does not exists"));
            return buildUserFromUserEntity(user1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    private User buildUserFromUserEntity(Group28.OAuth.Domain.User userEntity) {
        // convert model user to spring security user
        String username               = userEntity.getUsername();
        String password               = userEntity.getPassword();
        boolean enabled               = true;
        boolean accountNonExpired     = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked      = true;
        GrantedAuthority[] authorities = new SimpleGrantedAuthority[1];
        authorities[0] = new SimpleGrantedAuthority("ROLE_USER");

        User springUser = new User(username,
                password,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                Arrays.asList(authorities));
        return springUser;
    }
}
