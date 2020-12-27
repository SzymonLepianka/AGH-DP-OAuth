package Group28.OAuth.Model;

import java.sql.SQLException;
import java.util.Map;

public class RefreshingAccessToken extends State {

    @Override
    public Response handle(Context context, Map<String, String> params) throws SQLException {

        System.out.println("RefreshingAccessToken");

        //TODO

        return null;
    }

    @Override
    public String toString() {
        return "RefreshingAccessToken";
    }
}
