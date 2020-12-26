package Group28.OAuth.Model;

import java.sql.SQLException;
import java.util.Map;

public class Failure extends State {

//    //Singleton
//    private static Failure instance = new Failure();
//
//    private Failure() {}
//
//    public static Failure instance() {
//        return instance;
//    }
//
//    //Business logic and state transition
//    @Override
//    public void updateState(Context context,  Map<String, String> params)
//    {
////        System.out.println("Failure");
//        // zmiana stanu
////        context.setCurrentState([nowy stan].instance());
//    }

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("Failure");

        return null;
    }

    @Override
    public String toString() {
        return "Failure";
    }

}
