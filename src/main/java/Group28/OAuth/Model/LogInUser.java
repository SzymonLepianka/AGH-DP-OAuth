package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.Permission;
import Group28.OAuth.Model.State.Context;
import Group28.OAuth.Model.State.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.stream.Collectors;

public class LogInUser {

    // TODO Maybe make interface for model classes
    public static Response handle(String username, String password, String clientID, PasswordEncoder passwordEncoder) throws SQLException {
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
        return context.handle(params);
    }
}
