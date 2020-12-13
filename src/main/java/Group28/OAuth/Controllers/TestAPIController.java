package Group28.OAuth.Controllers;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.ClientApp;
import Group28.OAuth.Domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/api")
public class TestAPIController {

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

    @GetMapping("/users/add")
    public @ResponseBody String addUser() throws SQLException {

        User newUser = new User();
        newUser.setFirstName("Bartek");
        newUser.setSurname("Kregielewski");
        newUser.setEmail("alex@o2.xd");
        newUser.setPassword("TEST123");
        newUser.setDeveloper(false);
        IDatabaseEditor db = DatabaseEditor.getInstance();
        db.getUsersAccessObject().create(newUser);

        return "ok";
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
//        clientApp.setId(1L);
        IDatabaseEditor db = DatabaseEditor.getInstance();
        User user = db.getUsersAccessObject().readById(1L);
        clientApp.setUser(user);
        clientApp.setAppSecret(69L);
        clientApp.setRedirectURL("pornhub.com");
        db.getAppsAccessObject().create(clientApp);
        return "dodano apke mordo";
    }




}
