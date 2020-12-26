package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AccessToken;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.RefreshToken;
import Group28.OAuth.Domain.Scope;
import Group28.OAuth.token.AccessTokenBuilder;
import Group28.OAuth.token.RefreshTokenBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class RedirectingToAppRedirectURL extends State {


//    //Singleton
//    private static RedirectingToAppRedirectURL instance = new RedirectingToAppRedirectURL();
//
//    private RedirectingToAppRedirectURL() {}
//
//    public static RedirectingToAppRedirectURL instance() {
//        return instance;
//    }
//
//    //Business logic and state transition
//    @Override
//    public void updateState(Context context, Map<String, String> params)
//    {
////        System.out.println("RedirectingToAppRedirectURL");
//        //zmiana stanu
////        context.setCurrentState([nowy stan].instance());
//    }
//
    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("RedirectingToAppRedirectURL");

        IDatabaseEditor db = DatabaseEditor.getInstance();

        // wyciągam clientID z params
        Long clientID = Long.parseLong(params.get("clientID"));

        // biorę z bazy danych redirectURL danego klienta
        String redirectURL = db.getAppsAccessObject().readById(clientID).getRedirectURL();

        if (params.containsKey("code") && !params.containsKey("createdRefreshToken") ) {
            // wyciągam code z params
            String code = params.get("code");

            // biorę wszystkie AuthCodes z bazy danych i sprawdzam czy istnieje o takich parametrach jak w params
            List<AuthCode> authCodes = db.getAuthCodesAccessObject().readAll();
            AuthCode authCode = authCodes.stream()
                    .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Code " + code + " does not exists (thrown in RedirectingToAppRedirectURL)"));



            // zwracam obiekt Response z pobranym redirectURL i pobranym obiektem authCode
            return new Response(redirectURL, authCode);
        }
        if (params.containsKey("createdRefreshToken") && params.containsKey("createdAccessToken")) {

            String createdRefreshToken = params.get("createdRefreshToken");
            String createdAccessToken = params.get("createdAccessToken");


//            Long createdRefreshTokenID = Long.parseLong(params.get("createdRefreshTokenID"));
//            Long createdAccessTokenID = Long.parseLong(params.get("createdAccessTokenID"));
//            AccessToken accessToken = db.getAccessTokensAccessObject().readById(createdAccessTokenID);
//            RefreshToken refreshToken = db.getRefreshTokensAccessObject().readById(createdRefreshTokenID);
//            Long appSecret = db.getAppsAccessObject().readById(clientID).getAppSecret();
//            AccessTokenBuilder accessTokenBuilder = new AccessTokenBuilder(accessToken.getCreatedAt(), accessToken.getExpiresAt(), accessToken.getScopes(), clientID, accessToken.getUser().getId(), appSecret);
//            String at = accessTokenBuilder.generateToken();
//            System.out.println(at);
//
//            RefreshTokenBuilder refreshTokenBuilder = new RefreshTokenBuilder(refreshToken.getExpiresAt(), refreshToken.getAccessToken().getId(), appSecret);
//            String rt = accessTokenBuilder.generateToken();
//            System.out.println(rt);


            return new Response(redirectURL, "accesstoken="+createdAccessToken+"refreshtoken"+createdRefreshToken);
        }
        return new Response(null, null);
    }

    @Override
    public String toString() {
        return "RedirectingToAppRedirectURL";
    }
}
