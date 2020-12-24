package Group28.OAuth.Model;

import java.sql.SQLException;
import java.util.Map;

public class ExchangingAuthorizationCodeForAccessToken extends State {


//    //Singleton
//    private static ExchangingAuthorizationCodeForAccessToken instance = new ExchangingAuthorizationCodeForAccessToken();
//
//    private ExchangingAuthorizationCodeForAccessToken() {}
//    public static ExchangingAuthorizationCodeForAccessToken instance() {
//        return instance;
//    }
//
//    //TODO: Business logic and state transition
//    @Override
//    public void updateState(Context context,  Map<String, String> params)
//    {
////        System.out.println("ExchangingAuthorizationCodeForAccessToken");
//        //TODO: zmiana stanu
////        context.setCurrentState([nowy stan].instance());
//    }

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {
        return null;
    }

    @Override
    public String toString() {
        return "ExchangingAuthorizationCodeForAccessToken";
    }
}
