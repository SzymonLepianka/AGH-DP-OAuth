package Group28.OAuth.Model;

import Group28.OAuth.DAO.DatabaseEditor;
import Group28.OAuth.DAO.IDatabaseEditor;
import Group28.OAuth.Domain.AuthCode;
import Group28.OAuth.Domain.Scope;

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

        // wyciągam code i clientID z params
        String code = params.get("code");
        Long clientID = Long.parseLong(params.get("clientID"));

        // biorę wszystkie AuthCodes z bazy danych i sprawdzam czy istnieje o takich parametrach jak w params
        IDatabaseEditor db = DatabaseEditor.getInstance();
        List<AuthCode> authCodes = db.getAuthCodesAccessObject().readAll();
        AuthCode authCode = authCodes.stream()
                .filter(c -> code.equals(c.getContent()) && clientID.equals(c.getClientApp().getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Code " + code + " does not exists (thrown in RedirectingToAppRedirectURL)"));

        // biorę z bazy danych redirectURL danego klienta
        String redirectURL = db.getAppsAccessObject().readById(clientID).getRedirectURL();

        // zwracam obiekt Response z pobranym redirectURL i pobranym obiektem authCode
        return new Response(redirectURL, authCode);
    }

    @Override
    public String toString() {
        return "RedirectingToAppRedirectURL";
    }
}
