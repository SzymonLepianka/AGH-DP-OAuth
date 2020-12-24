package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;

import java.sql.SQLException;
import java.util.Map;

public class VerifyingDataFromClient extends State {
    public VerifyingDataFromClient(Context context) {
        super(context);
    }

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

        // sprawdzam czy klient o danych clientId w params istnieje w bazei danych
        IDatabaseEditor dbEditor = DatabaseEditor.getInstance();
        if(dbEditor.getAppsAccessObject().readById((Long.parseLong(params.get("clientID")))) != null) {
            context.changeState(new CreatingAuthorizationCode());
        }
        else {
            context.changeState(new Failure());
            params.put("failIn", "VerifyingDataFromClient(nie ma takiego klienta)");
        }

        // wywołuję CreatingAuthorizationCode w przypadku gdy powodzenia / Failure w przeciwym przypadku
        return context.handle(params);
    }

    @Override
    public String toString() {
        return "VerifyingDataFromClient";
    }
}
