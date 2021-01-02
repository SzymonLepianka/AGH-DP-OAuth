package Group28.OAuth.Controllers;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@Controller
@RequestMapping(path="/web", produces = MediaType.TEXT_HTML_VALUE)
public class WebController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm() {
        return "loginForm";
    }

    @PostMapping("/login")
    public @ResponseBody String handleLogin(@RequestParam String clientID, @RequestParam String username, @RequestParam String password) {
        // TODO Move it to model
        try {
            IDatabaseEditor dbEditor = DatabaseEditor.getInstance();
            var users = dbEditor.getUsersAccessObject().readAll();
            var user = users.stream().filter(x -> x.getUsername() == username).findFirst();
            if (user.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            if(!passwordEncoder.encode(password).equals(user.get().getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            return "Ok";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
