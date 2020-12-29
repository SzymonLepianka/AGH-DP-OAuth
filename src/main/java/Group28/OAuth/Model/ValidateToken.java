package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AccessToken;
import Group28.OAuth.token.TokenDecoder;
import io.jsonwebtoken.Claims;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ValidateToken {

    public boolean validateToken(Long clientID, String accessToken) throws SQLException {

        // czytam z danych danych appSecret clienta z danym clientID
        IDatabaseEditor db = DatabaseEditor.getInstance();
        Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();

        //dekoduję z otrzymanego tokenu issuedAt, expiration, scopes i subject(userID)
        TokenDecoder tokenDecoder = new TokenDecoder();
        Claims claims = tokenDecoder.decodeToken(accessToken, appSecret.toString());
        String scopes = (String) claims.get("scopes");
        Long userID = Long.parseLong(claims.getSubject());
        // ustawiam format Timestamp issuedAt i expiration
        String date = String.valueOf(claims.getIssuedAt().toInstant()).substring(0, 10);
        String time = String.valueOf(claims.getIssuedAt().toInstant()).substring(11, 19);
        Timestamp issuedAt = Timestamp.valueOf(Timestamp.valueOf(date + " " + time).toLocalDateTime().plusHours(1));
        date = String.valueOf(claims.getExpiration().toInstant()).substring(0, 10);
        time = String.valueOf(claims.getExpiration().toInstant()).substring(11, 19);
        Timestamp expiration = Timestamp.valueOf(Timestamp.valueOf(date + " " + time).toLocalDateTime().plusHours(1));

        // sprawdzam czy expiration nie minął
        if (!expiration.after(Timestamp.valueOf(LocalDateTime.now()))) {
            return false;
        }
        // pobieram z bazy danych accessTokens i szukam przekazanego w params 'accessToken'
        List<AccessToken> accessTokens = db.getAccessTokensAccessObject().readAll();
        AccessToken accessTokenFound = accessTokens.stream()
                .filter(at -> (userID.equals(at.getUser().getId()) && issuedAt.equals(at.getCreatedAt()) || Timestamp.valueOf(issuedAt.toLocalDateTime().plusSeconds(1)).equals(at.getCreatedAt())) && (expiration.equals(at.getExpiresAt()) || Timestamp.valueOf(expiration.toLocalDateTime().plusSeconds(1)).equals(at.getExpiresAt())) && clientID.equals(at.getClientApp().getId()) && scopes.equals(at.getScopes()))
                .findFirst()
                .orElse(null);

        return accessTokenFound != null;
    }
}