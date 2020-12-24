package Group28.OAuth.Model;

import java.sql.SQLException;
import java.util.Map;

public class CreatingRefreshToken extends State {


//    //Singleton
//    private static CreatingRefreshToken instance = new CreatingRefreshToken();
//
//    private CreatingRefreshToken() {}
//
//    public static CreatingRefreshToken instance() {
//        return instance;
//    }
//
//    //Business logic and state transition
//    @Override
//    public void updateState(Context context,  Map<String, String> params)
//    {
////        System.out.println("CreatingAccessToken");
//        //zmiana stanu
////        context.setCurrentState([nowy stan].instance());
//    }

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {
        return null;
    }

    @Override
    public String toString() {
        return "CreatingRefreshToken";
    }
}
