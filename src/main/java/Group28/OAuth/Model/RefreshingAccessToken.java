package Group28.OAuth.Model;

import java.sql.SQLException;
import java.util.Map;

public class RefreshingAccessToken extends State {

//    //Singleton
//    private static RefreshingAccessToken instance = new RefreshingAccessToken();
//
//    private RefreshingAccessToken() {}
//
//    public static RefreshingAccessToken instance() {
//        return instance;
//    }
//
//    //Business logic and state transition
//    @Override
//    public void updateState(Context context,  Map<String, String> params)
//    {
////        System.out.println("RefreshingAccessToken");
//        //zmiana stanu
////        context.setCurrentState([nowy stan].instance());
//    }

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {
        return null;
    }

    @Override
    public String toString() {
        return "RefreshingAccessToken";
    }
}
