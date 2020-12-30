package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.token.TokenDecoder;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

public class GetUserData {

    public JSONObject getUserData(Long clientID, String accessToken) throws SQLException {

        // tworzę JSONObject (zostanie zwrócony)
        JSONObject userData = new JSONObject();

        // waliduję token
        ValidateToken validateToken = new ValidateToken();
        if (!validateToken.validateToken(clientID, accessToken)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Access Token");
        }

        // czytam z bazy danych appSecret clienta z danym clientID
        IDatabaseEditor db = DatabaseEditor.getInstance();
        Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();

        // dekoduję otrzymany token (scopes i userID)
        TokenDecoder tokenDecoder = new TokenDecoder();
        Claims claims = tokenDecoder.decodeToken(accessToken, appSecret.toString());
        String scopes = (String) claims.get("scopes");
        Long userID = Long.parseLong(claims.getSubject());

        // rozdzielam scopes do tablicy
        String[] scopesSeparated = scopes.split(",");

        // identyfikuję dany scope i zapisuję daną informację do JSONObject
        for (String scope: scopesSeparated) {
            switch (scope) {
                case "user_birthdate" -> userData.put("user_birthdate", db.getUsersAccessObject().readById(userID).getBirthDate());
                case "user_email" -> userData.put("user_email", db.getUsersAccessObject().readById(userID).getEmail());
                case "user_firstname" -> userData.put("user_firstname", db.getUsersAccessObject().readById(userID).getFirstName());
                case "user_phonenumber" -> userData.put("user_phonenumber", db.getUsersAccessObject().readById(userID).getPhoneNumber());
                case "user_surname" -> userData.put("user_surname", db.getUsersAccessObject().readById(userID).getSurname());
                case "user_username" -> userData.put("user_username", db.getUsersAccessObject().readById(userID).getUsername());
                default -> throw new IllegalStateException("Invalid scope: " + scope + " (while GetUserData)");
            }
        }
        // Zwraca JSONObject
        return userData;

    }

}
