package Group28.OAuth.Model;

/*Provides and interface to client to perform some action
and delegates state specific requests to the  subclass
that defines the current state.*/

import java.sql.SQLException;
import java.util.Map;

public class Context {

    private State currentState;

//    public Context(State currentState)
//    {
//        this.currentState = currentState;
//        if(currentState == null) {
//            this.currentState = Failure.instance();
//        }
//    }

    public Context() {
        this.currentState = new AuthenticatingClient();
    }

    public Response handle(Map<String, String> params) throws SQLException {
        return currentState.handle(this, params);
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    public void changeState(State state) {
        this.currentState = state;
    }
}