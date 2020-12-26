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
import java.util.Random;

public class CreatingAccessToken extends State {

//    //Singleton
//    private static CreatingAccessToken instance = new CreatingAccessToken();
//
//    private CreatingAccessToken() {}
//
//
//    public static CreatingAccessToken instance() {
//        return instance;
//    }
//
//    //Business logic and state transition
//    @Override
//    public void updateState(Context context,  Map<String, String>params)
//    {
////        System.out.println("CreatingAccessToken");
//        //zmiana stanu
////        context.setCurrentState([nowy stan].instance());
//    }

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("CreatingAccessToken");

        String code = params.get("code");
        Long clientID = Long.parseLong(params.get("clientID"));

        IDatabaseEditor db = DatabaseEditor.getInstance();
        List<AuthCode> codesFromDataBase = db.getAuthCodesAccessObject().readAll();
        AuthCode authCode = codesFromDataBase.stream()
                .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Code " + code + " does not exists (while CreatingAccessToken)"));

        Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
        Timestamp expiresAt = Timestamp.valueOf(LocalDateTime.now().plusMinutes(15));
        Long userID = authCode.getUser().getId();

        List<Permission> permissionsFromDataBase = db.getPermissionsAccessObject().readAll();
        StringBuilder scopes = new StringBuilder();
        for (Permission permission: permissionsFromDataBase) {
            if (permission.getClientApp().getId().equals(clientID) && permission.getUser().getId().equals(userID)){
                String name = permission.getScope().getName();
                if (!scopes.toString().contains(name)) {
                    if (!scopes.isEmpty()) {
                    scopes.append(",");
                    }
                    scopes.append(name);
                }
            }
        }
//        Long newAccessTokenID = new Random().nextLong();
        AccessToken accessToken = new AccessToken();
//        accessToken.setId(newAccessTokenID);
        accessToken.setClientApp(db.getAppsAccessObject().readById(clientID));
        accessToken.setCreatedAt(createdAt);
        accessToken.setExpiresAt(expiresAt);
        accessToken.setRevoked(false);
        accessToken.setScopes(scopes.toString());
        accessToken.setUser(db.getUsersAccessObject().readById(userID));
        accessToken.setUpdatedAt(createdAt);
        db.getAccessTokensAccessObject().create(accessToken);

//        List<AccessToken> accessTokens = db.getAccessTokensAccessObject().readAll();
//        AccessToken accessTokenFromDataBase = accessTokens.stream()
//                .filter(accessToken1 -> clientID.equals(accessToken1.getClientApp().getId()) && createdAt.equals(accessToken1.getCreatedAt()))
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("Access Token " + accessToken + " does not exists (while CreatingAccessToken)"));
//        System.out.println(createdAccessToken.getId());

        params.put("createdAt", createdAt.toString());
        params.put("expiresAt", expiresAt.toString());
        params.put("scopes", scopes.toString());

        Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();

        AccessTokenBuilder accessTokenBuilder = new AccessTokenBuilder(createdAt, expiresAt, scopes.toString(), clientID, userID, appSecret);
        String s = accessTokenBuilder.generateToken();
        System.out.println(s);

        params.put("createdAccessToken", s);



        context.changeState(new CreatingRefreshToken());
        return context.handle(params);
    }
    @Override
    public String toString() {
        return "CreatingAccessToken";
    }
}
