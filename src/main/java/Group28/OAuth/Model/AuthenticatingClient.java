package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;

import java.sql.SQLException;
import java.util.Map;

public class AuthenticatingClient extends State {

//    Singleton
//    private static AuthenticatingClient instance = new AuthenticatingClient();
//    private AuthenticatingClient() {}
//    public static AuthenticatingClient instance() {
//        return instance;
//    }
//
//    // Business logic and state transition
//    @Override
//    public void updateState(Context context,  Map<String, String> params)
//    {
////        System.out.println("AuthenticatingClient");
////        context.setCurrentState([nowy stan].instance());
//    }

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("AuthenticatingClient");

        // sprawdzam czy klient o danych clientId w params istnieje w bazie danych
        IDatabaseEditor dbEditor = DatabaseEditor.getInstance();
        if (dbEditor.getAppsAccessObject().readById((Long.parseLong(params.get("clientID")))) != null) {
            context.changeState(new VerifyingDataFromClient());
        } else {
            context.changeState(new Failure());
            params.put("failIn", "AuthenticatingClient(nie ma takiego klienta)");
        }
        // wywołuję VerifyingDataFromClient w przypadku gdy powodzenia / Failure w przeciwym przypadku
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "AuthenticatingClient";
    }
}
