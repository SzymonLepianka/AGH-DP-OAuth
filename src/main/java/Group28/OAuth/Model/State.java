package Group28.OAuth.Model;

import java.sql.SQLException;
import java.util.Map;

public abstract class State {
    Context context;
    State(Context context) {
        this.context = context;
    }

    public State() {

    }
//
//    protected State() {}

//    public abstract void updateState(Context context,  Map<String, String>params) throws SQLException;
    public abstract Response handle(Context context, Map<String, String> params) throws SQLException;
    public abstract String toString();
}

