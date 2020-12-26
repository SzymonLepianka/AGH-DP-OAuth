package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AccessToken;
import Group28.OAuth.Domain.RefreshToken;
import Group28.OAuth.token.RefreshTokenBuilder;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class CreatingRefreshToken extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("CreatingRefreshToken");

        // odczytuję expiresAt z params
        // pozbywam się z nanosekund, trzeba było zaokrąglić
        Timestamp expiresAtTemp = Timestamp.valueOf(params.get("expiresAt"));
        if (expiresAtTemp.getNanos() >= 500000000) {
            expiresAtTemp = Timestamp.valueOf(expiresAtTemp.toLocalDateTime().plusSeconds(1));
        }
        expiresAtTemp.setNanos(0);
        Timestamp expiresAt = expiresAtTemp;

        // odczytuję potrzebne parametry z 'params'
        Timestamp createdAt = Timestamp.valueOf(params.get("createdAt"));
        String scopes = params.get("scopes");
        Long clientID = Long.parseLong(params.get("clientID"));

        // odczytuję stworzony w CreatingAccessToken accessToken
        IDatabaseEditor db = DatabaseEditor.getInstance();
        List<AccessToken> accessTokenList = db.getAccessTokensAccessObject().readAll();
        AccessToken accessToken = accessTokenList.stream()
                .filter(at -> expiresAt.equals(at.getExpiresAt()) && clientID.equals(at.getClientApp().getId()) && scopes.equals(at.getScopes()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Access Token with expiresAt=" + expiresAt + " does not exists (while CreatingRefreshToken)"));

        // tworzę obiekt refreshToken - zapisuję do niego parametry i zapisuję do bazy danych
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccessToken(accessToken);
        refreshToken.setExpiresAt(Timestamp.valueOf(createdAt.toLocalDateTime().plusDays(1)));
        refreshToken.setRevoked(false);
        db.getRefreshTokensAccessObject().create(refreshToken);

        // czytam z danych danych appSecret clienta z danym clientID
        Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();

        // buduję refreshToken
        RefreshTokenBuilder refreshTokenBuilder = new RefreshTokenBuilder(refreshToken.getExpiresAt(), refreshToken.getAccessToken().getId(), appSecret);
        String createdRefreshToken = refreshTokenBuilder.generateToken();
        System.out.println("Created Refresh Token: " + createdRefreshToken);

        // dopisuję do 'params' stworzony refreshToken
        params.put("createdRefreshToken", createdRefreshToken);

        // zmieniam stan na RedirectingToAppRedirectURL
        context.changeState(new RedirectingToAppRedirectURL());
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "CreatingRefreshToken";
    }
}
