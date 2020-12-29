package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.Permission;
import Group28.OAuth.Domain.Scope;
import Group28.OAuth.Domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CreatingAuthorizationCode extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("CreatingAuthorizationCode");

        // pobieram 'username' aktualnie zalogowanego użytkownika:
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        // pobieram z bazy obiekt User dla danego 'username'
        IDatabaseEditor db = DatabaseEditor.getInstance();
        List<User> users = db.getUsersAccessObject().readAll();
        Group28.OAuth.Domain.User user1 = users.stream()
                .filter(user -> username.equals(user.getUsername()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
//        "Student " + username + " does not exists (while creating auth code)"

        // tworzę treść AuthCode - ciąg losowych znaków o zadanej długości codeLength
        int codeLength = 10;
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        String generatedString = sb.toString();

        // tworzę obiekt AuthCode
        AuthCode authCode = new AuthCode();
        authCode.setContent(generatedString);
        authCode.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusHours(1)));
        authCode.setRevoked(false);
        authCode.setClientApp(db.getAppsAccessObject().readById(Long.parseLong(params.get("clientID"))));
        authCode.setUser(user1);

        // zapisuję stworzony obiekt authCode do bazy danych
        db.getAuthCodesAccessObject().create(authCode);

        //zmieniam stan na RedirectingToAppRedirectURL (tam wyślę code do klienta)
        context.changeState(new RedirectingToAppRedirectURL());

        ///////////////////////
        //PERMISSIONS i SCOPE//
        ///////////////////////

        // biorę 'scopes' (jest jednym polem w 'params') i robię sobie listę z tego
        String scopesRaw = params.get("scopes");
        String[] scopesSeparated;
        if (scopesRaw.contains(",")) {
            scopesSeparated = scopesRaw.split(",");
        } else {
            scopesSeparated = new String[]{scopesRaw};
            scopesSeparated[0] = scopesRaw;
        }

        // biorę wszystkie scopes z database i tych o ten samej treści co w scopesSeparated
        List<Scope> scopesFromDataBase = db.getScopesAccessObject().readAll();
        for (String scope : scopesSeparated) {
            Scope scope1 = scopesFromDataBase.stream()
                    .filter(s -> scope.equals(s.getName()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)); // new IllegalStateException("Scope " + scope + " does not exists (thrown in CreatingAuthorizationCode)"));

            // tworzę obiekt Permission
            Permission permission = new Permission();
            permission.setClientApp(db.getAppsAccessObject().readById(Long.parseLong(params.get("clientID"))));
            permission.setScope(scope1);
            permission.setUser(user1);

            // zapisuję stworzony obiekt permission do bazy danych
            db.getPermissionsAccessObject().create(permission);
        }

        // dopisuję 'code' (content) do params
        params.put("code", authCode.getContent());

        return context.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingAuthorizationCode";
    }
}
