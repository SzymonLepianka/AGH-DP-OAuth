package Group28.OAuth.Controllers;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.Permission;
import Group28.OAuth.Model.State.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Collectors;

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
    public @ResponseBody String handleLogin(@RequestParam String clientID, @RequestParam String username, @RequestParam String password, HttpServletResponse httpServletResponse) {
        // TODO Move it to model
        try {
            IDatabaseEditor dbEditor = DatabaseEditor.getInstance();
            var users = dbEditor.getUsersAccessObject().readAll();
            var user = users.stream().filter(x -> x.getUsername().equals(username)).findFirst();
            if (user.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            if(!passwordEncoder.matches(password, user.get().getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            var context = new Context();
            var params = new HashMap<String, String>();
            params.put("clientID", clientID);
            var allPermission = dbEditor.getPermissionsAccessObject().readAll();
            var userPermissionForApp = allPermission.stream()
                    .filter(x -> x.getUser().getId().equals(user.get().getId())
                            && String.valueOf(x.getClientApp().getId()).equals(clientID))
                    .collect(Collectors.toList());

            // Add all permissions for user, if user doesn't have any
            if (userPermissionForApp.isEmpty()) {
                var scopes = dbEditor.getScopesAccessObject().readAll();
                var clientApp = dbEditor.getAppsAccessObject().readById(Long.parseLong(clientID));
                for (var scope : scopes) {
                    var permission = new Permission();
                    permission.setClientApp(clientApp);
                    permission.setUser(user.get());
                    permission.setScope(scope);
                    dbEditor.getPermissionsAccessObject().create(permission);
                    userPermissionForApp.add(permission);
                }
            }
            var scopesBuilder = new StringBuilder();
            for(var permission : userPermissionForApp) {
                scopesBuilder.append(permission.getScope().getName()).append(",");
            }
            scopesBuilder.delete(scopesBuilder.length()-1, scopesBuilder.length());
            params.put("scopes", scopesBuilder.toString());
            params.put("userID", String.valueOf(user.get().getId()));
            var modelResponse = context.handle(params);
            // TODO Move it to view
            var authCode = (AuthCode) modelResponse.content;
            var cookieAuthCode = new Cookie("AuthCode", authCode.getContent());
            httpServletResponse.addCookie(cookieAuthCode);
            return "Ok";
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
