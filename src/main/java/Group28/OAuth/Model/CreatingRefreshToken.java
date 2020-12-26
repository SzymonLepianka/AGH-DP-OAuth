package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AccessToken;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.RefreshToken;
import Group28.OAuth.token.RefreshTokenBuilder;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CreatingRefreshToken extends State {


//    //Singleton
//    private static CreatingRefreshToken instance = new CreatingRefreshToken();
//
//    private CreatingRefreshToken() {}
//
//    public static CreatingRefreshToken instance() {
//        return instance;
//    }
//
//    //Business logic and state transition
//    @Override
//    public void updateState(Context context,  Map<String, String> params)
//    {
////        System.out.println("CreatingAccessToken");
//        //zmiana stanu
////        context.setCurrentState([nowy stan].instance());
//    }

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("CreatingRefreshToken");

        IDatabaseEditor db = DatabaseEditor.getInstance();

//        Timestamp expiresAt = Timestamp.valueOf(params.get("expiresAt").substring(0,19));
        Timestamp expiresAt = Timestamp.valueOf(params.get("expiresAt"));
        System.out.println(expiresAt);

        if (expiresAt.getNanos() >= 500000000) {
            expiresAt = Timestamp.valueOf(expiresAt.toLocalDateTime().plusSeconds(1));
        }
        expiresAt.setNanos(0);
        Timestamp expiresAtFinal = expiresAt;

        Timestamp createdAt = Timestamp.valueOf(params.get("createdAt"));
        String scopes = params.get("scopes");
        Long clientID = Long.parseLong(params.get("clientID"));

        List<AccessToken> accessTokenList = db.getAccessTokensAccessObject().readAll();
        AccessToken accessToken = accessTokenList.stream()
                .filter(at -> expiresAtFinal.equals(at.getExpiresAt()) && clientID.equals(at.getClientApp().getId()) && scopes.equals(at.getScopes()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Access Token with expiresAt=" + expiresAtFinal + " does not exists (while CreatingRefreshToken)"));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccessToken(accessToken);
        refreshToken.setExpiresAt(Timestamp.valueOf(createdAt.toLocalDateTime().plusDays(1)));
        refreshToken.setRevoked(false);
        db.getRefreshTokensAccessObject().create(refreshToken);

        Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();

        RefreshTokenBuilder refreshTokenBuilder = new RefreshTokenBuilder(refreshToken.getExpiresAt(), refreshToken.getAccessToken().getId(), appSecret);
        String rt = refreshTokenBuilder.generateToken();
        System.out.println(rt);

        params.put("createdRefreshToken", rt);

        context.changeState(new RedirectingToAppRedirectURL());
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingRefreshToken";
    }
}
