package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;

import java.sql.SQLException;
import java.util.Map;

public class VerifyingDataFromClient extends State {
//    public VerifyingDataFromClient(Context context) {
//        super(context);
//    }

    //Singleton
//    private static VerifyingDataFromClient instance = new VerifyingDataFromClient();
//    public VerifyingDataFromClient() {
//        super();
//    }
//    public static VerifyingDataFromClient instance() {
//        return instance;
//    }
//    //Business logic and state transition
//    @Override
//    public void updateState(Context context,  Map<String, String> params)  {
//        System.out.println("VerifyingDataFromClient");
//        //zmiana stanu
////        context.setCurrentState([nowy stan].instance());
//    }

    @Override
    public Response handle(Context context, Map<String, String> params)  throws SQLException {

        System.out.println("VerifyingDataFromClient");

        if (params.containsKey("scopes")){
            context.changeState(new CreatingAuthorizationCode());
        }
        //TODO: else - ExchangingAuthorizationCodeForAccessToken oraz RefreshingAccessToken
        else {
            
            context.changeState(new Failure());
        }
        // wywołuję CreatingAuthorizationCode w przypadku gdy powodzenia / Failure w przeciwym przypadku
        // w przyszłości CreatingAuthorizationCode / ExchangingAuthorizationCodeForAccessToken / RefreshingAccessToken
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "VerifyingDataFromClient";
    }
}
