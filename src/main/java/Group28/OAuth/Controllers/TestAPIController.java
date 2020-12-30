package Group28.OAuth.Controllers;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.ClientApp;
import Group28.OAuth.Domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/testApi")
public class TestAPIController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public @ResponseBody String getAllUsers() throws SQLException {
        IDatabaseEditor dbEditor = DatabaseEditor.getInstance();
        StringBuilder sb = new StringBuilder();
        List<User> users = dbEditor.getUsersAccessObject().readAll();
        for (var user : users) {
            sb.append(user.getFirstName());
        }
        return sb.toString();
    }

//    @PostMapping("/users/add")
//    public @ResponseBody String addUser() throws SQLException {
//
//        User newUser = new User();
//        newUser.setFirstName("Bartek2");
//        newUser.setSurname("Kregielewski2");
//        newUser.setEmail("alex@o2.xd");
//        newUser.setPassword("TEST123");
//        newUser.setDeveloper(false);
//        IDatabaseEditor db = DatabaseEditor.getInstance();
//        db.getUsersAccessObject().create(newUser);
//
//        return "ok";
//    }

    //TODO: powinien być POST, ale mi nie działa ~Szymek

    @GetMapping("/users/add")
    public @ResponseBody String addUser(@RequestParam Date birth_date,
                                        @RequestParam String email,
                                        @RequestParam String first_name,
                                        @RequestParam Boolean is_developer,
                                        @RequestParam String password,
                                        @RequestParam String phone_number,
                                        @RequestParam String surname,
                                        @RequestParam String username) throws SQLException {
        User newUser = new User();
        newUser.setBirthDate(birth_date);
        newUser.setEmail(email);
        newUser.setFirstName(first_name);
        newUser.setDeveloper(is_developer);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setPhoneNumber(phone_number);
        newUser.setSurname(surname);
        newUser.setUsername(username);
        IDatabaseEditor db = DatabaseEditor.getInstance();
        db.getUsersAccessObject().create(newUser);
        return "Stworzono użytkownika";
    }

    @GetMapping("/clients")
    public @ResponseBody String getAllApps() throws SQLException {
        IDatabaseEditor dbEditor = DatabaseEditor.getInstance();
        StringBuilder sb = new StringBuilder();
        List<ClientApp> clientAppList = dbEditor.getAppsAccessObject().readAll();
        for (var clientApp : clientAppList) {
            sb.append(clientApp.getRedirectURL());
        }
        return sb.toString();
    }

    @GetMapping("/clients/add")
    public @ResponseBody String addClient() throws SQLException {
        ClientApp clientApp = new ClientApp();
        IDatabaseEditor db = DatabaseEditor.getInstance();
        User user = db.getUsersAccessObject().readById(2L);
        clientApp.setUser(user);
        clientApp.setAppSecret(9878654123L);
        clientApp.setRedirectURL("onet.pl/xd");
        db.getAppsAccessObject().create(clientApp);
        return "new application added!";
    }
}
