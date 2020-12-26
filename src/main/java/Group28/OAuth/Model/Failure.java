package Group28.OAuth.Model;

import java.sql.SQLException;
import java.util.Map;

public class Failure extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("Failure");

        //TODO

        return null;
    }

    @Override
    public String toString() {
        return "Failure";
    }

}
