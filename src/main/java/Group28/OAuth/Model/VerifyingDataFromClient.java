package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.Scope;

import java.sql.SQLException;
import java.util.List;
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
        else if(params.containsKey("code")){

            String code = params.get("code");
            Long clientID = Long.parseLong(params.get("clientID"));

            IDatabaseEditor db = DatabaseEditor.getInstance();
            List<AuthCode> codesFromDataBase = db.getAuthCodesAccessObject().readAll();
            AuthCode authCode = codesFromDataBase.stream()
                    .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                    .findFirst()
                    .orElse(null);
//                    .orElseThrow(() -> new IllegalStateException("Code " + code + " does not exists (while VerifyingDataFromClient)"));
            if (authCode != null){
                context.changeState(new ExchangingAuthorizationCodeForAccessToken());
            } else{
                context.changeState(new Failure());
            }
        }
        else {
            context.changeState(new Failure());
        }
        //TODO: else - ExchangingAuthorizationCodeForAccessToken oraz RefreshingAccessToken

        // wywołuję CreatingAuthorizationCode w przypadku gdy powodzenia / Failure w przeciwym przypadku
        // w przyszłości CreatingAuthorizationCode / ExchangingAuthorizationCodeForAccessToken / RefreshingAccessToken
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "VerifyingDataFromClient";
    }
}
