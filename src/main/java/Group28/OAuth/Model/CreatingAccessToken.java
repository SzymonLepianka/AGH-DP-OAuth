package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AccessToken;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.Permission;
import Group28.OAuth.token.AccessTokenBuilder;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CreatingAccessToken extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("CreatingAccessToken");

        //pobieram z params 'code' i 'clientID'
        String code = params.get("code");
        Long clientID = Long.parseLong(params.get("clientID"));

        // pobieram z bazy danych AuthCodes i szukam przekazanego w params 'code'
        IDatabaseEditor db = DatabaseEditor.getInstance();
        List<AuthCode> codesFromDataBase = db.getAuthCodesAccessObject().readAll();
        AuthCode authCode = codesFromDataBase.stream()
                .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Code " + code + " does not exists (while CreatingAccessToken)"));

        //ID użytkownika pryzpisanego do znalezionego AuthCode
        Long userID = authCode.getUser().getId();

        // ustalam createdAt oraz expiresAt (parametry tokenu)
        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        Timestamp expiresAt = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15));

        // tworzę String scopes (parametr tokenu)
        // - pobieram wszsytkie permissions
        // - jeśli w danym permission zgadza się clientID i userID dodaję do 'scopes'
        List<Permission> permissionsFromDataBase = db.getPermissionsAccessObject().readAll();
        StringBuilder scopes = new StringBuilder();
        for (Permission permission : permissionsFromDataBase) {
            if (permission.getClientApp().getId().equals(clientID) && permission.getUser().getId().equals(userID)) {
                String name = permission.getScope().getName();
                // sprawdzam czy danego scope już nie przypisałem
                if (!scopes.toString().contains(name)) {
                    if (!scopes.isEmpty()) {
                        scopes.append(",");
                    }
                    scopes.append(name);
                }
            }
        }

        // tworzę obiekt accessToken - zapisuję do niego parametry i zapisuję do bazy danych
        AccessToken accessToken = new AccessToken();
        accessToken.setClientApp(db.getAppsAccessObject().readById(clientID));
        accessToken.setCreatedAt(createdAt);
        accessToken.setExpiresAt(expiresAt);
        accessToken.setRevoked(false);
        accessToken.setScopes(scopes.toString());
        accessToken.setUser(db.getUsersAccessObject().readById(userID));
        accessToken.setUpdatedAt(createdAt);
        db.getAccessTokensAccessObject().create(accessToken);

        // czytam z danych danych appSecret clienta z danym clientID
        Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();

        // buduję accessToken
        AccessTokenBuilder accessTokenBuilder = new AccessTokenBuilder(createdAt, expiresAt, scopes.toString(), clientID, userID, appSecret);
        String createdAccessToken = accessTokenBuilder.generateToken();
        System.out.println("Created Access Token: " + createdAccessToken);

        // dopisuję do 'params' konieczne dane (w tym stworzony accessToken)
        params.put("createdAt", createdAt.toString());
        params.put("expiresAt", expiresAt.toString());
        params.put("scopes", scopes.toString());
        params.put("createdAccessToken", createdAccessToken);

        // zmieniam stan na CreatingRefreshToken
        context.changeState(new CreatingRefreshToken());
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingAccessToken";
    }
}
